package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

public class GuestRepository {
    private EntityManagerFactory emf;

    public GuestRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Find guest in Guest table based on email
    public Guest get(String email) {
        try {
            return emf.callInTransaction(em ->
                em.createQuery("select g from Guest g where g.email = :email", Guest.class)
                    .setParameter("email", email)
                    .getSingleResult()
            );
        } catch (NoResultException e) {
            return null;
        }
    }


    // Create a new Guest in Guest table
    public void create(String firstName, String lastName, String email) {
        if (guestExist(email)) {
            return;
        }

        emf.runInTransaction(em -> {
            Guest guest = new Guest();
            guest.setFirstName(firstName);
            guest.setLastName(lastName);
            guest.setEmail(email);
            em.persist(guest);
        });
    }

    // Returns true if guest exists in database
    public boolean guestExist(String email) {
        Guest existingGuest = get(email);
        return existingGuest != null;
    }
}
