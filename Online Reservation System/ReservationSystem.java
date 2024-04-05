import java.util.Scanner;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ReservationSystem {
    private static HashMap<String, String> users = new HashMap<>();
    private static final String USER_FILE = "users.txt";
    private static final String BOOKINGS_FILE = "bookings.txt";

    public static void main(String[] args) {
        loadUsers();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Reservation System!");

        boolean isLoggedIn = false;
        String currentUser = "";

        while (!isLoggedIn) {
            System.out.println("Are you an existing user? (yes/no)");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("yes")) {
                isLoggedIn = login(scanner);
                if (isLoggedIn) {
                    System.out.print("Enter username: ");
                    currentUser = scanner.nextLine().trim(); // Assign the username entered during login
                    System.out.println("Welcome, " + currentUser + "!");
                    accessMainSystem(scanner, currentUser); // Pass 'currentUser' to accessMainSystem
                } else {
                    System.out.println("Invalid username or password. Please try again.");
                }
            } else if (choice.equals("no")) {
                createAccount(scanner);
            } else {
                System.out.println("Invalid choice. Please enter 'yes' or 'no'.");
            }
        }
    }

    private static boolean login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        // Check if the username exists and the password matches
        return users.containsKey(username) && users.get(username).equals(password);
    }

    private static void accessMainSystem(Scanner scanner, String currentUser) {
        System.out.println("Welcome, " + currentUser + "!");
        boolean continueBooking = true;
        while (continueBooking) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Book a ticket");
            System.out.println("2. Check ticket details");
            System.out.println("3. Cancel a ticket");
            System.out.println("4. Logout");
    
            System.out.print("Enter your choice: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
    
                switch (choice) {
                    case 1:
                        bookTicket(scanner);
                        break;
                    case 2:
                        checkTicketDetails(scanner);
                        break;
                    case 3:
                        cancelTicket(scanner);
                        break;
                    case 4:
                        continueBooking = false;
                        System.out.println("Logged out successfully.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }    

    private static void bookTicket(Scanner scanner) {
        System.out.print("\nEnter the number of passengers: ");
        int numPassengers = Integer.parseInt(scanner.nextLine().trim());

        ArrayList<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < numPassengers; i++) {
            System.out.println("\nPassenger " + (i + 1) + ":");
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Gender (M/F): ");
            char gender = scanner.nextLine().trim().charAt(0);
            passengers.add(new Passenger(name, age, gender));
        }

        System.out.print("Enter the journey details (From): ");
        String from = scanner.nextLine().trim();
        System.out.print("Enter the journey details (To): ");
        String to = scanner.nextLine().trim();
        String classPreference;
        do {
            System.out.print("Enter the class preference (3A, 2A, 1A, SL, CC): ");
            classPreference = scanner.nextLine().trim().toUpperCase();
        } while (!classPreference.equals("3A") && !classPreference.equals("2A") && !classPreference.equals("1A")
                && !classPreference.equals("SL") && !classPreference.equals("CC"));
        System.out.print("Enter the journey date: ");
        String journeyDate = scanner.nextLine().trim();

        // Generate PNR
        String pnr = generatePNR();

        // Display the details for confirmation
        System.out.println("\nPlease confirm the details:");
        System.out.println("Passenger(s):");
        for (Passenger passenger : passengers) {
            System.out.println("- Name: " + passenger.getName() + ", Age: " + passenger.getAge() + ", Gender: "
                    + passenger.getGender());
        }
        System.out.println("Journey Details:");
        System.out.println("- From: " + from);
        System.out.println("- To: " + to);
        System.out.println("- Class Preference: " + classPreference);
        System.out.println("- Journey Date: " + journeyDate);
        System.out.println("- PNR: " + pnr);

        System.out.print("\nConfirm the details? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (confirm.equals("yes")) {
            // Save the booking details to file
            saveBooking(pnr, passengers, from, to, classPreference, journeyDate, "Booked");
            System.out.println("Booking confirmed!");
        } else {
            System.out.println("Booking canceled.");
        }
    }

    private static void checkTicketDetails(Scanner scanner) {
        System.out.print("\nEnter your PNR: ");
        String pnr = scanner.nextLine().trim();
    
        // Search for the booking details using the provided PNR
        boolean found = false;
        try (Scanner fileScanner = new Scanner(new File(BOOKINGS_FILE))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.startsWith("PNR: " + pnr)) {
                    found = true;
                    System.out.println("\nTicket Details for PNR: " + pnr);
                    System.out.println(line); // Print PNR
                    for (int i = 0; i < 6; i++) {
                        if (fileScanner.hasNextLine()) {
                            System.out.println(fileScanner.nextLine()); // Print next 6 lines of booking details
                        } else {
                            break; // Break if there are no more lines
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error searching for ticket details: " + e.getMessage());
        }
    
        if (!found) {
            System.out.println("Ticket details not found for PNR: " + pnr);
        }
    }
    

    private static void cancelTicket(Scanner scanner) {
        System.out.print("\nEnter the PNR of the ticket you want to cancel: ");
        String pnr = scanner.nextLine().trim();
    
        // Create a new ArrayList to store the modified booking data
        ArrayList<String> updatedBookings = new ArrayList<>();
    
        try (Scanner fileScanner = new Scanner(new File(BOOKINGS_FILE))) {
            boolean cancelled = false;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.startsWith("PNR: " + pnr)) {
                    // Modify the status to "Canceled"
                    updatedBookings.add("PNR: " + pnr);
                    updatedBookings.add("Status: Canceled");
                    // Skip the rest of the booking details
                    for (int i = 0; i < 6; i++) {
                        fileScanner.nextLine();
                    }
                    cancelled = true;
                    System.out.println("Ticket with PNR " + pnr + " cancelled successfully.");
                } else {
                    updatedBookings.add(line);
                }
            }
    
            // If the ticket was not found, notify the user
            if (!cancelled) {
                System.out.println("Ticket with PNR " + pnr + " not found.");
                return;
            }
    
            // Write the updated booking data back to the bookings file
            try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
                for (String booking : updatedBookings) {
                    writer.println(booking);
                }
            } catch (IOException e) {
                System.err.println("Error updating bookings file: " + e.getMessage());
            }
    
        } catch (IOException e) {
            System.err.println("Error cancelling ticket: " + e.getMessage());
        }
    }
    
    private static void createAccount(Scanner scanner) {
        System.out.print("Enter a new username: ");
        String newUsername = scanner.nextLine().trim();

        if (users.containsKey(newUsername)) {
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }

        System.out.print("Enter a new password: ");
        String newPassword = scanner.nextLine().trim();

        // Add the new user to the HashMap and save to file
        users.put(newUsername, newPassword);
        saveUsers();
        System.out.println("Account created successfully!");
    }

    private static void loadUsers() {
        try (Scanner fileScanner = new Scanner(new File(USER_FILE))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(":");
                users.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    private static void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            for (String username : users.keySet()) {
                writer.println(username + ":" + users.get(username));
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    private static void saveBooking(String pnr, ArrayList<Passenger> passengers, String from, String to, String classPreference, String journeyDate, String status) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(BOOKINGS_FILE, true))) {
            writer.println("PNR: " + pnr);
            writer.println("Status: " + status); // Include status
            writer.println("Passenger(s):");
            for (Passenger passenger : passengers) {
                writer.println("- Name: " + passenger.getName() + ", Age: " + passenger.getAge() + ", Gender: " + passenger.getGender());
            }
            writer.println("Journey Details:");
            writer.println("- From: " + from);
            writer.println("- To: " + to);
            writer.println("- Class Preference: " + classPreference);
            writer.println("- Journey Date: " + journeyDate);
            writer.println();
        } catch (IOException e) {
            System.err.println("Error saving booking: " + e.getMessage());
        }
    }    

    private static String generatePNR() {
        // Generate a random 9-digit number
        Random random = new Random();
        int pnr = random.nextInt(900000000) + 100000000;
        return String.valueOf(pnr);
    }

    static class Passenger {
        private String name;
        private int age;
        private char gender;

        public Passenger(String name, int age, char gender) {
            this.name = name;
            this.age = age;
            this.gender = gender;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public char getGender() {
            return gender;
        }
    }
}