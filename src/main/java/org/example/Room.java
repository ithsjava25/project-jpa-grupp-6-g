package org.example;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;

@Entity
public class Room {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //todo make unique for each hotel
    @NaturalId
    private String roomNumber;

    private int roomCapacity;

    private BigDecimal priceClass;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public BigDecimal getPriceClass() {
        return priceClass;
    }

    public void setPriceClass(BigDecimal priceClass) {
        this.priceClass = priceClass;
    }
}

