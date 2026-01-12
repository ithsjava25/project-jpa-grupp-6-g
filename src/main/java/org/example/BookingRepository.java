package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BookingRepository {
    private final EntityManagerFactory emf;
    private final GuestRepository guestRepository;

    public BookingRepository(EntityManagerFactory emf, GuestRepository guest) {
        this.emf = emf;
        guestRepository = guest;
    }

    public List<Room> getEmptyRooms(LocalDate start, LocalDate end, int guests) {
        return emf.callInTransaction(em ->
            em.createQuery("""
            select r
            from Room r
            where r.roomCapacity >= :guests
              and not exists (
                  select 1
                  from Booking b
                  where b.bookedRoom = r
                    and b.startDate <= :end
                    and b.endDate >= :start
              )
            order by r.id
            """, Room.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .setParameter("guests", guests)
                .getResultList()
        );
    }

    public List<BookingInfo> getBookings(){
        return emf.callInTransaction(em -> {
            //noinspection unchecked
            List<Booking> bookings = em.createNativeQuery("select b.* from Booking b", Booking.class).getResultList();
            return bookings.stream()
                .map(booking -> {
                    return new BookingInfo(
                        booking.getId(),
                        booking.getBookedRoom().getRoomNumber(),
                        booking.getStartDate(),
                        booking.getEndDate(),
                        booking.getTotalPrice());})
                .toList();
        });
    }

    private List<Guest> getGuestsByBooking(String bookingId){
        return emf.callInTransaction(em ->
            em.createNativeQuery("""
                select * from Guest g
                join guestBooking gb on g.id = gb.guest_id
                where gb.booking_id = :bookingId
                """,
                Guest.class).setParameter("bookingId", bookingId)
                .getResultList()
        );
    }

    public List<GuestInfo> getGuestInfoByBooking(String bookingId){
        return getGuestsByBooking(bookingId).stream()
            .map(guest -> {
                return new GuestInfo(
                    guest.getEmail(),
                    guest.getFirstName(),
                    guest.getLastName());})
            .toList();
    }

    public List<BookingInfo> getBookingInfoByGuest(String email){
        long guestId = guestRepository.get(email).getId();
        return emf.callInTransaction(em -> {
            //noinspection unchecked
            List<Booking> bookings = em.createNativeQuery("""
                select * from Booking b
                join guestBooking gb
                on b.id = gb.booking_id
                where gb.guest_id = :guestId
                """, Booking.class)
                .setParameter("guestId", guestId)
                .getResultList();
            return bookings.stream()
                .map(booking -> {
                    return new BookingInfo(
                        booking.getId(),
                        booking.getBookedRoom().getRoomNumber(),
                        booking.getStartDate(),
                        booking.getEndDate(),
                        booking.getTotalPrice());})
                .toList();
        });
    }

    public boolean create(List<String> emailList, LocalDate startDate, LocalDate endDate, int numberOfGuests, BigDecimal totalPrice){
        return emf.callInTransaction(em -> {
            Room room = getEmptyRooms(startDate, endDate, numberOfGuests).getFirst();

            Booking booking = new Booking();
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setTotalPrice(totalPrice);
            booking.setBookedRoom(room);

            em.persist(booking);

            Query query = em.createQuery("select max(b.id) from Booking b");
            Long bookingId = (Long) query.getSingleResult();

            for (String email : emailList)
                em.createNativeQuery("insert into guestBooking (guest_id, booking_id) values (?, ?)")
                    .setParameter(1, guestRepository.get(email).getId())
                    .setParameter(2, bookingId)
                    .executeUpdate();

            return true;
        });
    }

    public boolean remove(String bookingId){
        return emf.callInTransaction(em -> {

            em.createNativeQuery("delete gb.* from guestBooking gb where booking_id = ?")
                .setParameter(1, bookingId)
                .executeUpdate();

            em.createNativeQuery("delete b.* from Booking b where id = ?")
                .setParameter(1, bookingId)
                .executeUpdate();
            return true;
        });
    }

    public int getMaxGuests(){
        return emf.callInTransaction(em ->
            Optional.ofNullable(
                em.createQuery("""
                select max(r.roomCapacity)
                from Room r
            """, Integer.class)
                    .getSingleResult()
            ).orElse(0)
        );
    }

    record BookingInfo(long id, String roomNumber, LocalDate startDate, LocalDate endDate, BigDecimal totalPrice){ }

    record GuestInfo(String email, String firstName, String lastName){}
}
