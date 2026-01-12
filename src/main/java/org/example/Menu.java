package org.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);

    private final GuestRepository guestRepo;
    private final BookingRepository bookingRepo;

    public Menu(GuestRepository guestRepo, BookingRepository bookingRepo) {
        this.guestRepo = guestRepo;
        this.bookingRepo = bookingRepo;
    }

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


    public void printBookings() {

        var bookings = bookingRepo.getBookings();

        // Validate that bookings is not empty
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
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

    public void printBookingsByGuest(){
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
        }

        var bookings = bookingRepo.getBookingInfoByGuest(email);

        // Validate that bookings is not empty
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
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

    public void printGuestsByBooking(){
        System.out.print("Booking ID: ");
        String bookingId = scanner.nextLine();

        // Validate booking id is not empty
        if (bookingId.isEmpty()) {
            System.out.println("Booking ID cannot be empty.");
            return;
        }

        // Check that booking id is a number
        try {
            Long.parseLong(bookingId);
        } catch (NumberFormatException e) {
            System.out.println("Booking ID must be a number.");
            return;
        }

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

    public void createBooking(){
        try {
            System.out.print("Start date (YYYY-MM-DD): ");
            LocalDate start = LocalDate.parse(scanner.nextLine());

            System.out.print("End date (YYYY-MM-DD): ");
            LocalDate end = LocalDate.parse(scanner.nextLine());

            // Validate that end date is after start date
            if (!end.isAfter(start)) {
                System.out.println("End date must be after start date.");
                return;
            }

            System.out.print("Number of guests: ");
            long guests = Long.parseLong(scanner.nextLine());

            System.out.println("Guest emails (separate with comma): ");
            List<String> emails = Arrays.stream(scanner.nextLine().split(","))
                .map(String::trim)
                .toList();

            // Loop through list and create guests if they don't exist
            for (String email : emails) {
                if (!guestRepo.guestExist(email)) {
                    guestRepo.create("Unknown", "Guest", email);
                }
            }

            var rooms = bookingRepo.getEmptyRooms(start, end, guests);

            // Validate if no empty rooms exists
            if (rooms.isEmpty()) {
                System.out.println("No available rooms.");
                return;
            }

            Room room = rooms.get(0);
            BigDecimal nights = BigDecimal.valueOf(ChronoUnit.DAYS.between(start, end));
            BigDecimal totalPrice = BigDecimal.valueOf(100); // todo: calculate total price

            bookingRepo.create(emails, start, end, guests, totalPrice);
            System.out.println("Booking created.");


        } catch(Exception e) {
            System.out.println("Invalid input.");
        }
    }

    public void removeBooking(){
        System.out.print("Booking ID to cancel: ");
        String bookingId = scanner.nextLine();

        bookingRepo.remove(bookingId);
        System.out.println("Booking removed.");
    }
}

