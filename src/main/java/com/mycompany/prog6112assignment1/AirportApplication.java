package com.mycompany.prog6112assignment1;

import java.util.Scanner;

public class AirportApplication {
    private static final MainLogic system = new MainLogic();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            displayMainMenu();
            int choice = getIntInput("Select an option (1-4): ");

            switch (choice) {
                case 1 -> flightManagementMenu();
                case 2 -> gateSchedulingMenu();
                case 3 -> operationalReportsMenu();
                case 4 -> {
                    System.out.println("\nGoodbye! Logging you out...");
                    running = false;
                }
                default -> System.out.println("Invalid selection. Please choose an option between 1 and 4.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("      Main Menu      ");
        System.out.println("--------------------------------------------------");
        System.out.println("1. Flight Management");
        System.out.println("2. Gate Scheduling Management");
        System.out.println("3. Operational Reports");
        System.out.println("4. Exit System");
        System.out.println("--------------------------------------------------");
    }

    private static void flightManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Flight Management System ---");
            System.out.println("1. Register New Flight");
            System.out.println("2. Search Flight Details");
            System.out.println("3. Update Flight Details");
            System.out.println("4. Update Flight Status");
            System.out.println("5. Remove Flight");
            System.out.println("6. Display All Flights");
            System.out.println("7. Back to Main Menu");

            int choice = getIntInput("Select an option (1-7): ");

            switch (choice) {
                case 1 -> registerFlight();
                case 2 -> searchFlight();
                case 3 -> updateFlight();
                case 4 -> updateStatus();
                case 5 -> removeFlight();
                case 6 -> system.displayAllFlights();
                case 7 -> back = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void registerFlight() {
        System.out.println("\n--- New Registration ---");
        System.out.println("1. Domestic Flight");
        System.out.println("2. International Flight");
        System.out.println("3. Cargo Flight");
        int type = getIntInput("Select Flight Type (1-3): ");

        String flightId = getStringInput("Enter Flight ID (e.g., SA123): ");
        String airline = getStringInput("Enter Airline Name: ");
        String destination = getStringInput("Enter Destination: ");

        try {
            Flight newFlight;

            if (type == 3) { //forgot to add cargo flights
                newFlight = new Flight(
                    flightId, airline, destination, 0, 0,
                    FlightCategory.CARGO, FlightStatus.SCHEDULED
                );
            } else if (type == 2) { // International Flight
                int capacity = getIntInput("Enter Passenger Capacity: ");
                int booked = getIntInput("Enter Number of Booked Passengers: ");
                String terminal = getStringInput("Enter Departure Terminal: ");
                String gate = getStringInput("Enter Boarding Gate: ");
                String customsInput = getStringInput("Is Customs Clearance Required? (yes/no): ");
                boolean customsClearance = customsInput.equalsIgnoreCase("yes") || customsInput.equalsIgnoreCase("y");

                newFlight = new InternationalFlight(
                    flightId, airline, destination, capacity, booked,
                    FlightStatus.SCHEDULED, terminal, gate, customsClearance
                );
            } else { // Domestic Flight
                int capacity = getIntInput("Enter Passenger Capacity: ");
                int booked = getIntInput("Enter Number of Booked Passengers: ");

                newFlight = new Flight(
                    flightId, airline, destination, capacity, booked,
                    FlightCategory.DOMESTIC, FlightStatus.SCHEDULED
                );
            }

            system.addFlight(newFlight);
            System.out.println("Flight " + flightId + " successfully registered!");
        } catch (DuplicateFlightIdException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void searchFlight() {
        String flightId = getStringInput("Enter Flight ID to search: ");
        try {
            Flight flight = system.searchFlight(flightId);
            flight.displayDetails();
        } catch (MissingFlightException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateFlight() {
        String flightId = getStringInput("Enter Flight ID to update: ");
        try {
            system.searchFlight(flightId); // Verify existence

            String airline = getStringInput("Enter New Airline Name: ");
            String destination = getStringInput("Enter New Destination: ");
            int capacity = getIntInput("Enter New Passenger Capacity: ");
            int booked = getIntInput("Enter New Booked Passengers: ");

            system.updateFlightDetails(flightId, airline, destination, capacity, booked);
            System.out.println("Flight details updated successfully.");
        } catch (MissingFlightException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateStatus() {
        String flightId = getStringInput("Enter Flight ID: ");
        System.out.println("Available Statuses: 1. SCHEDULED  2. BOARDING  3. DELAYED  4. DEPARTED  5. CANCELLED");
        int statusChoice = getIntInput("Select New Status (1-5): ");

        FlightStatus newStatus = switch (statusChoice) {
            case 1 -> FlightStatus.SCHEDULED;
            case 2 -> FlightStatus.BOARDING;
            case 3 -> FlightStatus.DELAYED;
            case 4 -> FlightStatus.DEPARTED;
            case 5 -> FlightStatus.CANCELLED;
            default -> null;
        };

        if (newStatus == null) {
            System.out.println("Invalid status selection.");
            return;
        }

        try {
            system.updateFlightStatus(flightId, newStatus);
            System.out.println("Status updated to " + newStatus + " for Flight " + flightId);
        } catch (MissingFlightException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void removeFlight() {
        String flightId = getStringInput("Enter Flight ID to remove: ");
        try {
            system.removeFlight(flightId);
            System.out.println("Flight " + flightId + " removed successfully.");
        } catch (MissingFlightException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void gateSchedulingMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Gate Scheduling ---");
            System.out.println("1. Assign Flight to Gate");
            System.out.println("2. Release Gate Assignment");
            System.out.println("3. Display Gate Schedule Grid");
            System.out.println("4. Display Available Slots");
            System.out.println("5. Display Occupied Slots");
            System.out.println("6. Back to Main Menu");

            int choice = getIntInput("Select an option (1-6): ");

            switch (choice) {
                case 1 -> assignGate();
                case 2 -> releaseGate();
                case 3 -> system.displayGateScheduleGrid();
                case 4 -> system.displayAvailableSlots();
                case 5 -> system.displayOccupiedSlots();
                case 6 -> back = true;
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void assignGate() {
        String flightId = getStringInput("Enter Flight ID to assign: ");

        System.out.println("Gate Rows: 1 to 5 (maps to Gate G01 - G05)");
        int gateNumber = getIntInput("Enter Gate Number (1-5): ");

        System.out.println("Time Slots:");
        for (int i = 0; i < MainLogic.TIME_SLOTS.length; i++) {
            System.out.println((i + 1) + ". " + MainLogic.TIME_SLOTS[i]);
        }
        int slotNumber = getIntInput("Select Time Slot Number (1-8): ");

        try {
            system.assignFlightToGate(flightId, gateNumber - 1, slotNumber - 1);
            System.out.println("Flight " + flightId + " assigned successfully to Gate " + gateNumber + " at Slot " + slotNumber + ".");
        } catch (MainException e) {
            System.out.println("Gate Scheduling Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static void releaseGate() {
        int gateNumber = getIntInput("Enter Gate Number (1-5): ");
        int slotNumber = getIntInput("Enter Time Slot Number (1-8): ");

        try {
            system.releaseGateByLocation(gateNumber - 1, slotNumber - 1);
            System.out.println("Gate slot cleared successfully.");
        } catch (GateSchedulingException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void operationalReportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Operational Report and Sorting ---");
            System.out.println("1. Generate Full Operational Report");
            System.out.println("2. Sort and Display Flights");
            System.out.println("3. Back to Main Menu");

            int choice = getIntInput("Select an option (1-3): ");

            switch (choice) {
                case 1 -> system.generateFullOperationalReport();
                case 2 -> displaySortedFlightsSubMenu();
                case 3 -> back = true;
                default -> System.out.println("Invalid choice. Please select an option between 1 and 3.");
            }
        }
    }

    private static void displaySortedFlightsSubMenu() {
        System.out.println("\n--- Sort and Display List ---");
        System.out.println("1. Sort by Flight ID (Alphabetical)");
        System.out.println("2. Sort by Departure Time (Chronological)");
        int choice = getIntInput("Select sorting option (1-2): ");

        if (choice == 1) {
            system.sortFlightsById();
            system.displayAllFlights();
        } else if (choice == 2) {
            system.sortFlightsByDepartureTime();
            system.displayAllFlights();
        } else {
            System.out.println("Invalid sorting selection.");
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer. Please enter a valid number.");
            }
        }
    }
}