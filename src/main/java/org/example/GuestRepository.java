package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class GuestRepository {
    private EntityManagerFactory emf;

    public GuestRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Find guest in Guest table based on email
    public Guest get(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Guest> query = em.createQuery(
                "select g from Guest g where g.email = :email",
                Guest.class
            );
           query.setParameter("email", email);
           return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
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
