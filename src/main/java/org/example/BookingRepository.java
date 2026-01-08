package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookingRepository {
    private final EntityManagerFactory emf;
    private List<BookingInfo> bookingInfo;
    private List<GuestInfo> guestInfo;

    public BookingRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<Room> getEmptyRooms(LocalDate start, LocalDate end, long guests) {
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

//    public Room getEmptyRooms(String startDate, String endDate, String guests){
//        //todo: return a list of all available rooms from DB
//        emf.runInTransaction(em -> {
//                Query query = em.createNativeQuery(
//                    "select r.* from Room r left join Booking b on r.id = b.room_id " +
//                        "where (b.id is null) or " +
//                        "(? not between b.startDate and date_add(b.endDate, interval -1 day) " +
//                        "and ? not between date_add(b.startDate, interval 1 day) and b.endDate " +
//                        "and r.roomCapacity >= ?)",
//                        Room.class)
//                    .setParameter(1, startDate)
//                    .setParameter(2, endDate)
//                    .setParameter(3, guests);
//                availableRooms = query.getResultList();
//                availableRooms.forEach(System.out::println);
//        });
//        return availableRooms.availableRooms.isEmpty() ? null: availableRooms.getFirst();
//    }

    public List<BookingInfo> getBookings(){
        emf.runInTransaction(em -> {
            Query query = em.createQuery("select b from Booking b");
            List<Booking> bookings = query.getResultList();
            bookingInfo = bookings.stream()
                .map(booking -> {
                    return new BookingInfo(
                        booking.getId(),
                        booking.getBookedRoom().getRoomNumber(),
                        booking.getStartDate(),
                        booking.getEndDate());})
                .toList();
        });
        return bookingInfo;
        //todo: return all bookings form DB
    }

    public List<GuestInfo> getGuestsByBooking(String bookingId){
        emf.runInTransaction(em -> {
            Query query = em.createNativeQuery(
                "select * from Guest g " +
                "join guestBooking gb " +
                    "on g.id = gb.guest_id " +
                "where gb.booking_id = :bookingId",
                Guest.class);
            query.setParameter("bookingId", bookingId);
            List<Guest> guests = query.getResultList();
            guestInfo = guests.stream().map(guest -> {
                return new GuestInfo(
                    guest.getEmail(),
                    guest.getFirstName(),
                    guest.getLastName());})
                .toList();
        });
        return guestInfo;
        //todo: return all guests connected to booking_id
    }

    public List<BookingInfo> getBookingsByGuest(String email){
//        long guestId = GuestRepository.get(email).getId();
        long guestId = 1; // placeholder until GuestRepository.get() implemented
        emf.runInTransaction(em -> {
            Query query = em.createNativeQuery(
                "select * from Booking b " +
                "join guestBooking gb " +
                    "on b.id = gb.booking_id " +
                "where gb.guest_id = :guestId",
                Booking.class);
            query.setParameter("guestId", guestId);
            List<Booking> bookings = query.getResultList();
            bookingInfo = bookings.stream()
                .map(booking -> {
                    return new BookingInfo(
                        booking.getId(),
                        booking.getBookedRoom().getRoomNumber(),
                        booking.getStartDate(),
                        booking.getEndDate());})
                .toList();
            bookingInfo.forEach(System.out::println);
        });
        //todo: return all bookings connected to email
        return bookingInfo;
    }

    public boolean create(List<String> emailList, String startDate, String endDate, String guests){
        AtomicBoolean status = new AtomicBoolean(false);
        emf.runInTransaction(em -> {
            Room room = getEmptyRooms(LocalDate.parse(startDate), LocalDate.parse(endDate), Long.parseLong(guests)).getFirst();
            Booking booking = createBooking(startDate, endDate, room);

            em.persist(booking);

            Query query = em.createQuery("select max(b.id) from Booking b");
            Long bookingId = (Long) query.getSingleResult();

//            Query query = em.createQuery("select b.id from Booking b order by id desc limit 1", Booking.class);
//            List<Booking> result = query.getResultList();
//            long bookingId = result.getFirst().getId();

            for (String email : emailList)
                em.createNativeQuery("insert into guestBooking (guest_id, booking_id) values (?, ?)")
//                    .setParameter(1, GuestRepository.get(email).getId())
                    .setParameter(1, 1) // placeholder until GuestRepository.get() implemented
                    .setParameter(2, bookingId)
                    .executeUpdate();
            status.set(true);
        });
        return status.get();
    }

    private static Booking createBooking(String startDate, String endDate, Room room) {
        LocalDate startingDate;
        LocalDate endingDate;

        startingDate = LocalDate.parse(startDate);
        endingDate = LocalDate.parse(endDate);


        Booking booking = new Booking();
        booking.setStartDate(startingDate);
        booking.setEndDate(endingDate);
        booking.setBookedRoom(room);
        return booking;
    }

    public boolean remove(long bookingId){
        AtomicBoolean status = new AtomicBoolean(false);
        emf.runInTransaction(em -> {
            em.createNativeQuery("delete b.* from Booking b where id = ?")
                .setParameter(1, bookingId)
                .executeUpdate();
            status.set(true);
        });
        return status.get();
        //todo: remove a booking from the Booking table
    }

    record BookingInfo(long id, String roomNumber, LocalDate startDate, LocalDate endDate){}

    record GuestInfo(String email, String firstName, String lastName){}
}
