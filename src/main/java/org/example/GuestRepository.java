package org.example;

import jakarta.persistence.EntityManagerFactory;

public class GuestRepository {
    private EntityManagerFactory emf;

    public GuestRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void get(String email){
        //todo: find guest in Guest table based on email
    }

    public void create(String firstName, String lastName, String email) {
        if (validateGuest(email)){
            //todo: add new row to Guest table
        }
    }

    //todo: return true if guest doesn't exist in database
    public boolean validateGuest(String email){
        //todo: validate here if get returns no results

        return false;
    }
}
