package org.example;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //todo: what room you booked in
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room bookedRoom;

    //todo: not null, needs to be in future
    private Date startDate;

    //todo: not null, needs to be start date +1 or more
    private Date endDate;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name="guestBooking",
        joinColumns = @JoinColumn(name="booking_id"),
        inverseJoinColumns = @JoinColumn(name = "guest_id")
    )
    Set<Guest> guestBookings;

    //todo: calculate total price by base price * capacity * nightsSpent (weekday/end) (Adult/Child)
    //todo: get base price and room capacity from room table.
    public void calculateTotalPrice(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
