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
        bookingRepo.getBookings().forEach(b ->
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

        // todo: Validate if guest exists

        bookingRepo.getBookingInfoByGuest(email)
            .forEach(b ->
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

        bookingRepo.getGuestInfoByBooking(bookingId)
            .forEach(g ->
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

            // todo: Validate that end date is after start date

            System.out.print("Number of guests: ");
            long guests = Long.parseLong(scanner.nextLine());

            System.out.println("Guest emails (separate with comma): ");
            List<String> emails = Arrays.stream(scanner.nextLine().split(","))
                .map(String::trim)
                .toList();

            // todo: validate and loop through list and create guests if they dont exists

            var rooms = bookingRepo.getEmptyRooms(start, end, guests);

            // todo: validate if no empty rooms exists

            Room room = rooms.get(0);
            BigDecimal nights = BigDecimal.valueOf(ChronoUnit.DAYS.between(start, end));;
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

