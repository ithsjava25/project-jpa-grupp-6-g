package org.example;

import jakarta.persistence.EntityManagerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingService {
    private EntityManagerFactory emf;
    private GuestRepository guestRepository;

    public BookingService (EntityManagerFactory emf, GuestRepository guestRepository) {
        this.emf = emf;
        this.guestRepository = guestRepository;
    }

    /**
     *
     * @param priceClass default value is 300
     * @param numberOfGuests How many guests that are staying
     * @param startDate Starting date
     * @param endDate Ending date
     * @return The total price for the room for the selected dates
     */
    public BigDecimal calculateTotalPrice(BigDecimal priceClass, long numberOfGuests, LocalDate startDate, LocalDate endDate){
        BigDecimal totalprice = BigDecimal.valueOf(-1);

        if (validateValues(priceClass, numberOfGuests, startDate, endDate)) {
            long numberOfNights = ChronoUnit.DAYS.between(startDate, endDate);
            totalprice = priceClass.multiply(BigDecimal.valueOf(numberOfGuests)).multiply(BigDecimal.valueOf(numberOfNights));
        }

        return totalprice;
    }

    public boolean validateValues(BigDecimal priceClass, long numberOfGuests, LocalDate startDate, LocalDate endDate) {
        if (priceClass == null || priceClass.signum() != 1)
            return false;
        else if (numberOfGuests < 1) {
            return false;
        } else if (startDate.isAfter(endDate) || startDate.isBefore(LocalDate.now())) {
            return false;
        } else if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            return false;
        } else return true;
    }
}
