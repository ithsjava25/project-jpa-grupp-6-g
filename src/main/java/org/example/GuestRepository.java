package org.example;

import jakarta.persistence.EntityManagerFactory;

/**
 * Repository class responsible for database operations related to {@link Guest}.
 * <p>
 * Provides methods for retrieving, creating and checking existence of guests
 * based on their email address.
 */
public class GuestRepository {
    private final EntityManagerFactory emf;

    public GuestRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Retrieves a guest from the database based on email address.
     *
     * @param email the email address used to identify the guest
     * @return the {@link Guest} if found, or {@code null} if no guest exists
     *         with the given email
     */
    public Guest get(String email) {
        return emf.callInTransaction(em ->
            em.createQuery("select g from Guest g where g.email = :email", Guest.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null)
                );
    }

    /**
     * Creates and persists a new guest in the database.
     * <p>
     * If a guest with the given email already exists, the method returns
     * without creating a new entry.
     *
     * @param firstName the guest's first name
     * @param lastName  the guest's last name
     * @param email     the guest's email address
     */
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

    /**
     * Checks whether a guest with the given email exists in the database.
     *
     * @param email the email address to check
     * @return {@code true} if a guest exists, {@code false} otherwise
     */
    public boolean guestExist(String email) {
        Guest existingGuest = get(email);
        return existingGuest != null;
    }
}
