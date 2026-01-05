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

    // Create a new Guest in Guest table
    public void create(String firstName, String lastName, String email) {
        if (!validateGuest(email)){
            System.out.println("Guest with email " + email + " already exists!");
            return;
        }

        Guest guest = new Guest();
        guest.setFirstName(firstName);
        guest.setLastName(lastName);
        guest.setEmail(email);

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(guest);
            em.getTransaction().commit();
            System.out.println("Guest created!");
            System.out.println(firstName + " " + lastName + ", " + email);
        } catch (Exception e ) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error creating guest: " + e.getMessage());

        } finally {
            em.close();
        }
    }

    // Return true if guest doesn't exist in database
    public boolean validateGuest(String email){
        Guest existingGuest = get(email);
        return existingGuest == null;
    }
}
