package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import org.hibernate.jpa.HibernatePersistenceConfiguration;


public class App {
    static void main(String[] args) {

        final PersistenceConfiguration cfg = new HibernatePersistenceConfiguration("emf")
            .jdbcUrl("jdbc:mysql://localhost:3306/booking_db")
            .jdbcUsername("root")
            .jdbcPassword("root")
            .property("hibernate.hbm2ddl.auto", "update")
            .property("hibernate.show_sql", "true")
            .property("hibernate.format_sql", "true")
            .property("hibernate.highlight_sql", "true")
            .managedClasses(Hotel.class, Room.class, Guest.class, Booking.class);

        try (EntityManagerFactory emf = cfg.createEntityManagerFactory()) {
            GuestRepository guest = new GuestRepository(emf);
            BookingRepository booking = new BookingRepository(emf, guest);
            BookingService bookingService = new BookingService(emf, booking);

            Menu menu = new Menu(guest, booking, bookingService);
            menu.start();

        }
    }

}
