package org.example;

import jakarta.persistence.EntityManagerFactory;

import java.util.Date;
import java.util.List;

public class BookingRepository {
    private EntityManagerFactory emf;

    public BookingRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void getEmptyRooms(String startDate, String endDate){
        //todo: return a list of all empty rooms from DB
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
