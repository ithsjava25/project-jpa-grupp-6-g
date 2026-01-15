package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import org.hibernate.jpa.HibernatePersistenceConfiguration;

/**
 * Run to start to Hotel CLI application. Configures persistence, creates an instance of each repository class then
 * starts the Menu CLI user interface.
 */
public class App {
    static void main() {

        final PersistenceConfiguration cfg = new HibernatePersistenceConfiguration("emf")
            .jdbcUrl("jdbc:mysql://localhost:3306/booking_db")
            .jdbcUsername("root")
            .jdbcPassword("root")
            .property("hibernate.hbm2ddl.auto", "update")
            .property("hibernate.show_sql", "false")
            .property("hibernate.format_sql", "false")
            .property("hibernate.highlight_sql", "false")
            .managedClasses(Hotel.class, Room.class, Guest.class, Booking.class);

        try (EntityManagerFactory emf = cfg.createEntityManagerFactory()) {
            GuestRepository guest = new GuestRepository(emf);
            BookingRepository booking = new BookingRepository(emf, guest);
            BookingService bookingService = new BookingService(booking);

            Menu menu = new Menu(guest, booking, bookingService);
            menu.start();
        }
    }

}
