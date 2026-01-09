package org.example;

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
        System.out.println("Guest email: ");
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
        //todo: call BookingRepository to get all guests connected to booking and print to console
    }

    public void createBooking(){
        //todo: enter guest and booking information
        //todo: check for available rooms and choose room(s)
        //todo: create booking through BookingRepository
    }

    public void removeBooking(){
        //todo: call BookingRepository to remove a booking
    }
}

