package org.example;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room bookedRoom;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal totalPrice;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name="guestBooking",
        joinColumns = @JoinColumn(name="booking_id"),
        inverseJoinColumns = @JoinColumn(name = "guest_id")
    )
    Set<Guest> guestBookings;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }


    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Set<Guest> getGuestBookings() {
        return guestBookings;
    }

    public void setGuestBookings(Set<Guest> guestBookings) {
        this.guestBookings = guestBookings;
    }

    public Room getBookedRoom() {
        return bookedRoom;
    }

    public void setBookedRoom(Room bookedRoom) {
        this.bookedRoom = bookedRoom;
    }
}
