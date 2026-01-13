package org.example;

import jakarta.persistence.EntityManagerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingService {
    private EntityManagerFactory emf;
    private BookingRepository bookingRepository;
    private BigDecimal priceClass;

    public BookingService (EntityManagerFactory emf, BookingRepository bookingRepository) {
        this.emf = emf;
        this.bookingRepository = bookingRepository;
        priceClass = BigDecimal.valueOf(300);
    }

    /**
     * Calculates to the total cost of a booking for the specified days and number of guests.
     * @param numberOfGuests How many guests that are staying
     * @param startDate Starting date of booking
     * @param endDate Ending date of booking
     * @return BigDecimal The total price for the room for the selected dates
     */
    public BigDecimal calculateTotalPrice(int numberOfGuests, LocalDate startDate, LocalDate endDate){
        BigDecimal totalprice = BigDecimal.valueOf(-1);

        if (validateValues(numberOfGuests, startDate, endDate)) {
            long numberOfNights = ChronoUnit.DAYS.between(startDate, endDate);
            totalprice = priceClass.multiply(BigDecimal.valueOf(numberOfGuests)).multiply(BigDecimal.valueOf(numberOfNights));
            if (totalprice.signum() != 1)
                throw new RuntimeException("Critical error in hotel pricing logic, unrecoverable");
        }

        return totalprice;
    }

    private boolean validateValues(int numberOfGuests, LocalDate startDate, LocalDate endDate) {

        if (numberOfGuests < 1) {
            System.out.println("Number of guests can't be less than 1.");
            return false;
        } else if (bookingRepository.getMaxGuests() < numberOfGuests) {
            System.out.println("Number of guests exceeds largest room capacity.");
            return false;
        } else if (startDate.isAfter(endDate) || startDate.isBefore(LocalDate.now())) {
            System.out.println("Start date must from today and before end date.");
            return false;
        } else if (endDate.isEqual(startDate)) {
            System.out.println("End date must be at least one day after start date.");
            return false;
        } else return true;
    }
}
