package org.example;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Hotel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    //private String address;

    //private String city;

    //private String rating;

    @OneToMany (mappedBy = "hotel", cascade = CascadeType.PERSIST)
    private List<Room> rooms = new ArrayList<>();

    //todo: implement method to add rooms to hotel
    public void addRoom(){}

    //todo: implement method to remove rooms form hotel
    public void removeRoom(){}

    public List<Room> getRooms() {
        return rooms;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
