package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import org.hibernate.jpa.HibernatePersistenceConfiguration;

import java.awt.*;
import java.time.LocalDate;
import java.util.Scanner;

public class App {
    static void main(String[] args) {

        //todo CLI i app and Database communication in supplementary class.
        final PersistenceConfiguration cfg = new HibernatePersistenceConfiguration("emf")
            .jdbcUrl("jdbc:mysql://localhost:3306/booking_db")
            .jdbcUsername("root")
            .jdbcPassword("root")
            .property("hibernate.hbm2ddl.auto","update")
            .property("hibernate.show_sql","true")
            .property("hibernate.format_sql","true")
            .property("hibernate.highlight_sql","true")
            .managedClasses(Hotel.class, Room.class, Guest.class, Booking.class);

        try(EntityManagerFactory emf = cfg.createEntityManagerFactory()) {
            BookingRepository booking = new BookingRepository(emf);
            GuestRepository guest = new GuestRepository(emf);
            Scanner scanner = new Scanner(System.in);

            booking.getEmptyRooms(LocalDate.of(2026, 1, 20),
                LocalDate.of(2026, 1, 23 ),2)
                .forEach(System.out::println);
            // booking.remove(5);
            //booking.getEmptyRooms("2026-01-20", "2026-01-23", "2");
            //booking.getBookings().forEach(System.out::println);
            //booking.getGuestsByBooking("1");
            //booking.getBookingsByGuest("anna.svensson@example.se");
        }
    }

    public void printBookings(BookingRepository booking){
        //todo: call BookingRepository to get all bookings and print to console
    }

    public void printBookingsByGuest(BookingRepository booking){
        //todo: call BookingRepository to get all bookings connected to guest email and print to console
    }

    public void printGuestsByBooking(BookingRepository booking){
        //todo: call BookingRepository to get all guests connected to booking and print to console
    }

    public void createBooking(BookingRepository booking){
        //todo: enter guest and booking information
        //todo: check for available rooms and choose room(s)
        //todo: create booking through BookingRepository
    }

    public void removeBooking(BookingRepository booking){
        //todo: call BookingRepository to remove a booking
    }
}
