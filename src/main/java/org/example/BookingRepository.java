package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;

import java.util.Date;
import java.util.List;

public class BookingRepository {
    private EntityManagerFactory emf;

    public BookingRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void getEmptyRooms(String startDate, String endDate, String guests){
        //todo: return a list of all empty rooms from DB
        emf.runInTransaction(em -> {
                Query query = em.createNativeQuery(
                    "select roomNumber from Room r left join Booking b on r.id = b.room_id where (b.id is null) or (:startDate not between b.startDate and date_add(b.endDate, interval -1 day) and :endDate not between date_add(b.startDate, interval 1 day) and b.endDate and r.roomCapacity >= :guests)");
                query.setParameter("startDate", startDate);
                query.setParameter("endDate", endDate);
                query.setParameter("guests", guests);
                List<String> availableRooms = query.getResultList();
                availableRooms.forEach(System.out::println);
        });
    }

    public void getBookings(){
        //todo: return all bookings form DB
    }

    public void getGuestsByBooking(String bookingId){
        //todo: return all guests connected to booking_id
    }

    public void getBookingsByGuest(String email){
        //todo: return all bookings connected to email
    }

    public void create(List<Guest> guests, Date startTime, Date endTime){

        //todo: create a new booking in the Booking table
    }

    public void remove(String bookingId){
        //todo: remove a booking from the Booking table
    }
}
