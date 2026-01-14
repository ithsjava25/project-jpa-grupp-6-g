package org.example;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import java.math.BigDecimal;
import java.time.LocalDate;


class BookingServiceTest {
    @Test
    void calculateTotalPriceShouldReturnRightPrice() {
        var priceClass = BigDecimal.valueOf(400);
        var startDate = LocalDate.now();
        var endDate = LocalDate.now().plusDays(3);
        var guests = 3;
        var bookingService = new BookingService(priceClass);

        var totalPrice = bookingService.calculateTotalPrice(guests, startDate, endDate);

        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(3600));
    }
    @Test
    void calculateTotalPriceShouldThrowExceptionWhenCalculationIsNotPositive() {
        var priceClass = BigDecimal.ZERO;
        var startDate = LocalDate.now();
        var endDate = LocalDate.now().plusDays(3);
        var guests = 3;
        var bookingService = new BookingService(priceClass);

        assertThatThrownBy( () -> bookingService.calculateTotalPrice(guests, startDate, endDate)).isInstanceOf(Exception.class);
    }
    @Test
    void calculateTotalPriceShouldReturnNegativeValueIfGuestsAreLessThanOne() {
        var guests = 0;
        var start = LocalDate.now();
        var end = LocalDate.now().plusDays(1);
        var bookingService = new BookingService(BigDecimal.ONE);

        var result = bookingService.calculateTotalPrice(guests, start, end);

        assertThat(result).isEqualTo(BigDecimal.valueOf(-1));
    }

    @Test
    void calculateTotalPriceShouldReturnNegativeValueIfStartDateIsBeforeEndDate(){
        var guests = 1;
        var start = LocalDate.now().plusDays(1);
        var end = LocalDate.now();
        var bookingService = new BookingService(BigDecimal.ONE);

        var result = bookingService.calculateTotalPrice(guests, start, end);

        assertThat(result).isEqualTo(BigDecimal.valueOf(-1));
    }

    @Test
    void calculateTotalPriceShouldReturnNegativeValueIfStartDateIsBeforeCurrentDate(){
        var guests = 1;
        var start = LocalDate.now().minusDays(1);
        var end = LocalDate.now().plusDays(1);
        var bookingService = new BookingService(BigDecimal.ONE);

        var result = bookingService.calculateTotalPrice(guests, start, end);

        assertThat(result).isEqualTo(BigDecimal.valueOf(-1));
    }

    @Test
    void calculateTotalPriceShouldReturnNegativeValueIfStartDateIsEqualToEndDate(){
        var guests = 1;
        var start = LocalDate.now();
        var end = LocalDate.now();
        var bookingService = new BookingService(BigDecimal.ONE);

        var result = bookingService.calculateTotalPrice(guests, start, end);

        assertThat(result).isEqualTo(BigDecimal.valueOf(-1));
    }
}
