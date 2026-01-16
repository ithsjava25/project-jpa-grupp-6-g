package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Command-line interface for the booking system.
 * Provides a menu-driven interface for users to:
 *   Create new bookings
 *   View existing bookings
 *   Search bookings by guest
 *   View guests associated with a booking
 *   Cancel bookings
 */
public class Menu {

    private final Scanner scanner = new Scanner(System.in);

    private final GuestRepository guestRepo;
    private final BookingRepository bookingRepo;
    private final BookingService bookingService;
    private static final int INVALID = -1;

    public Menu(GuestRepository guestRepo, BookingRepository bookingRepo, BookingService bookingService) {
        this.guestRepo = guestRepo;
        this.bookingRepo = bookingRepo;
        this.bookingService = bookingService;
    }

    /**
     * Starts the main menu loop.
     * Displays the menu and processes user input until the user chooses to exit.
     */
    public void start() {
        while (true) {
            try {
                System.out.println("\n=== Booking System ===");
                System.out.println("1) Create booking");
                System.out.println("2) View all bookings");
                System.out.println("3) View all bookings by guest");
                System.out.println("4) View all guests by booking");
                System.out.println("5) Cancel booking");
                System.out.println("0) Exit");
                System.out.print("Choose: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> createBooking();
                    case "2" -> printBookings();
                    case "3" -> printBookingsByGuest();
                    case "4" -> printGuestsByBooking();
                    case "5" -> removeBooking();
                    case "0" -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                System.err.println("Error reading input: " + e.getMessage());
            }

        }
    }


    /**
     * Displays all the bookings in the system.
     * If no bookings exist, displays an appropriate message.
     */
    public void printBookings() {

        var bookings = bookingRepo.getBookings();

        // Validate that bookings is not empty
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        bookings.forEach(b ->
            System.out.println(
                "Booking #" + b.id() +
                    " | Room: " + b.roomNumber() +
                    " | " + b.startDate() + " → " + b.endDate() +
                    " | Price: " + b.totalPrice()
            )
        );
    }

    /**
     * Displays all bookings associated with a specific guest.
     * Prompts for the guest's email address and validates that the guest exists
     * before displaying their bookings.
     */
    public void printBookingsByGuest() {
        System.out.print("Guest email: ");
        String email = scanner.nextLine();

        // Validate if email is not empty
        if (email.isEmpty()) {
            System.out.println("Email cannot be empty");
            return;
        }

        // Validate if guest exists
        if (!guestRepo.guestExist(email)) {
            System.out.println("No guest found with email: " + email);
            return;
        }

        var bookings = bookingRepo.getBookingInfoByGuest(email);

        // Validate that bookings is not empty
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        bookings.forEach(b ->
            System.out.println(
                "Booking #" + b.id() +
                    " | Room: " + b.roomNumber() +
                    " | " + b.startDate() + " → " + b.endDate() +
                    " | Price: " + b.totalPrice()
            )
        );
    }

    /**
     * Displays all guests associated with a specific booking.
     * Prompts for the booking ID and validates it before displaying the guest list.
     */
    public void printGuestsByBooking() {
        System.out.print("Booking ID: ");
        String bookingId = scanner.nextLine();

        // Validate booking id
        if (isInvalidBookingId(bookingId)) return;

        var guests = bookingRepo.getGuestInfoByBooking(bookingId);

        // Check if bookings exists with booking id
        if (guests.isEmpty()) {
            System.out.println("No guests found for this booking or booking does not exist.");
            return;
        }

        guests.forEach(g ->
            System.out.println(
                g.firstName() + " " + g.lastName() +
                    " (" + g.email() + ")"
            )
        );
    }

    /**
     * Creates a new booking through an interactive process.
     * - Collecting booking dates and guest count.
     * - Validating input and checking room availability.
     * - Calculating and displaying the total price.
     * - Collecting guest information (creating new guests if needed).
     * - Saving the booking to the database.
     */
    public void createBooking() {
        LocalDate start = null;
        LocalDate end = null;
        BigDecimal totalPrice = BigDecimal.valueOf(-1);
        int guests = INVALID;
        do {
            try {
                System.out.print("Start date (YYYY-MM-DD): ");
                start = LocalDate.parse(scanner.nextLine());

                System.out.print("End date (YYYY-MM-DD): ");
                end = LocalDate.parse(scanner.nextLine());

                System.out.print("Number of guests: ");
                guests = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException | DateTimeParseException e) {
                System.out.println("Invalid Date format/Number of guests needs to be a whole number.");
                continue;
            }

            if (bookingRepo.getEmptyRooms(start, end, guests).isEmpty()) {
                System.out.println("No rooms available for the selected dates and guest count.");
                return;
            }

            totalPrice = bookingService.calculateTotalPrice(guests, start, end);
        } while (totalPrice.signum() != 1);

        System.out.println("The total price of the booking is: " + totalPrice.setScale(2, RoundingMode.HALF_UP));
        System.out.println("Do you want to continue? (Y/N)");
        String input = scanner.nextLine().toLowerCase();
        if (!input.equals("y")) {
            System.out.println("Booking cancelled.");
            return;
        }

        List<String> emails = new ArrayList<>();
        String email = "";

        if (!bookingRepo.getEmptyRooms(start, end, guests).isEmpty()) {
            for (int i = 0; i < guests; i++) {
                boolean guestExists = false;
                while (!guestExists) {
                    // verify not empty
                    System.out.println("Enter details for guest #" + (i + 1));
                    System.out.print("Email: ");
                    email = scanner.nextLine().trim().toLowerCase();
                    if (!email.isEmpty()) {
                        if (!guestRepo.guestExist(email)) {
                            System.out.print("First name: ");
                            String firstName = scanner.nextLine().trim().toLowerCase();
                            System.out.print("Last name: ");
                            String lastName = scanner.nextLine().trim().toLowerCase();

                            if (isValidGuestNames(firstName, lastName)) {
                                guestRepo.create(firstName, lastName, email);
                                guestExists = true;
                            }

                        } else{
                            guestExists = true;
                        }
                    }
                }
                emails.add(email);
            }

            bookingRepo.create(emails, start, end, guests, totalPrice);
            System.out.println("Booking created.");

        }
    }


    /**
     * Cancels an existing booking.
     * Prompts for the booking ID, validates it, and deletes the booking
     * along with all associated guest relationships.
     */
    public void removeBooking(){
        System.out.print("Booking ID to cancel: ");
        String bookingId = scanner.nextLine();

        // Validate booking id
        if (isInvalidBookingId(bookingId)) return;

        try {
            bookingRepo.remove(bookingId);
            System.out.println("Booking cancelled.");
        } catch (Exception e) {
            System.out.println("Error cancelling booking: " + e.getMessage());
        }
    }

    /**
     * Validates a booking ID string.
     * Checks that the ID is not empty and can be parsed as a long integer.
     * @param bookingId the booking ID to validate.
     * @return true if the booking ID is invalid, false if valid.
     */
    private static boolean isInvalidBookingId(String bookingId) {
        // Validate booking id is not empty
        if (bookingId.isEmpty()) {
            System.out.println("Booking ID cannot be empty.");
            return true;
        }

        // Check that booking id is a number
        try {
            Long.parseLong(bookingId);
        } catch (NumberFormatException e) {
            System.out.println("Booking ID must be a number.");
            return true;
        }
        return false;
    }

    /**
     * Validates that the first name and last name are not empty.
     * @param firstName A string with the first name input.
     * @param lastName A string with the last name input.
     * @return Returns false if either parameter is empty, otherwise true.
     */
    private boolean isValidGuestNames(String firstName, String lastName){
        if (firstName.isEmpty() || lastName.isEmpty()) {
            System.out.println("First and last name can't be blank.");
            return false;
        }
        return true;
    }
}
