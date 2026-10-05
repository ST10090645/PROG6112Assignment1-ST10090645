package com.mycompany.prog6112assignment1;

import java.util.ArrayList; // imports so we can use ArrayList
import java.util.List;

public class MainLogic {
    private List<Flight> flights; // using the ArrayList class to hold all registered flights
    private Flight[][] gateGrid; // 2D Array where the colums represent the timeslots and the rows represent the gates

    public static final String[] TIME_SLOTS = {
        "06:00", "08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00"
    };

    public MainLogic() {
        flights = new ArrayList<>();
        gateGrid = new Flight[5][8];
    }

    public List<Flight> getFlights() {
        return flights;
    }
    
    // Methods for option 1 (adding, removing, searching and updating all flights)

    public void addFlight(Flight flight) throws DuplicateFlightIdException {
        if (flight == null) {
            throw new IllegalArgumentException("Flight cannot be null.");
        }
        for (Flight f : flights) {
            if (f.getFlightId().equalsIgnoreCase(flight.getFlightId())) {
                throw new DuplicateFlightIdException("Flight ID '" + flight.getFlightId() + "' already exists.");
            }
        }
        flights.add(flight);
    }

    public Flight searchFlight(String flightId) throws MissingFlightException {
        for (Flight f : flights) {
            if (f.getFlightId().equalsIgnoreCase(flightId)) {
                return f;
            }
        }
        throw new MissingFlightException("Flight ID '" + flightId + "' not found.");
    }

    public void updateFlightDetails(String flightId, String newAirline, String newDestination, 
                                    int newCapacity, int newBooked) throws MissingFlightException {
        Flight f = searchFlight(flightId);
        f.setAirlineName(newAirline);
        f.setFlightDestination(newDestination);
        f.setPassengerCapacity(newCapacity);
        f.setNumOfBookedPassangers(newBooked);
    }

    public void updateFlightStatus(String flightId, FlightStatus newStatus) throws MissingFlightException {
        Flight f = searchFlight(flightId);
        f.setStatus(newStatus);
        
        // automatically release gate when flight status is updated to cancelled
        if (newStatus == FlightStatus.CANCELLED || newStatus == FlightStatus.DEPARTED) {
            releaseGateByFlightId(flightId);
        }
    }

    public boolean removeFlight(String flightId) throws MissingFlightException {
        Flight f = searchFlight(flightId);
        releaseGateByFlightId(flightId);
        return flights.remove(f);
    }

    public void displayAllFlights() {
        if (flights.isEmpty()) {
            System.out.println("No flights registered.");
            return;
        }
        for (Flight f : flights) {
            f.displayDetails();
            System.out.println("-----------------------------------");
        }
    }
    
    // methods for gate management (allocating/releasing gates as well as displaying the entire array or occupied/available slots)

    public void assignFlightToGate(String flightId, int gateIndex, int slotIndex) throws MainException {
        Flight f = searchFlight(flightId);

        if (gateIndex < 0 || gateIndex >= 5 || slotIndex < 0 || slotIndex >= 8) {
            throw new GateSchedulingException("Invalid gate or time slot index.");
        }

        if (gateGrid[gateIndex][slotIndex] != null) {
            throw new GateSchedulingException("Gate " + (gateIndex + 1) + " at " + 
                                            TIME_SLOTS[slotIndex] + " is already occupied.");
        }

        releaseGateByFlightId(flightId);

        gateGrid[gateIndex][slotIndex] = f;
        f.setDepartureTime(TIME_SLOTS[slotIndex]);
    }

    public void releaseGateByFlightId(String flightId) {
        for (int g = 0; g < 5; g++) {
            for (int s = 0; s < 8; s++) {
                if (gateGrid[g][s] != null && gateGrid[g][s].getFlightId().equalsIgnoreCase(flightId)) {
                    gateGrid[g][s].setDepartureTime("Unscheduled");
                    gateGrid[g][s] = null;
                    return;
                }
            }
        }
    }

    public void releaseGateByLocation(int gateIndex, int slotIndex) throws GateSchedulingException {
        if (gateIndex < 0 || gateIndex >= 5 || slotIndex < 0 || slotIndex >= 8) {
            throw new GateSchedulingException("Invalid gate or slot index.");
        }
        if (gateGrid[gateIndex][slotIndex] != null) {
            gateGrid[gateIndex][slotIndex].setDepartureTime("Unscheduled");
            gateGrid[gateIndex][slotIndex] = null;
        }
    }

    public void displayGateScheduleGrid() {
        System.out.printf("%-10s", "Gate/Time");
        for (String slot : TIME_SLOTS) {
            System.out.printf("%-12s", slot);
        }
        System.out.println("\n--------------------------------------------------------------------------------------------");

        for (int g = 0; g < 5; g++) {
            System.out.printf("%-10s", "Gate " + (g + 1));
            for (int s = 0; s < 8; s++) {
                String val = (gateGrid[g][s] == null) ? "[Empty]" : gateGrid[g][s].getFlightId();
                System.out.printf("%-12s", val);
            }
            System.out.println();
        }
    }

    public void displayAvailableSlots() {
        System.out.println("Available Gate Slots:");
        for (int g = 0; g < 5; g++) {
            for (int s = 0; s < 8; s++) {
                if (gateGrid[g][s] == null) {
                    System.out.println(" - Gate " + (g + 1) + " at " + TIME_SLOTS[s]);
                }
            }
        }
    }

    public void displayOccupiedSlots() {
        System.out.println("Occupied Gate Slots:");
        for (int g = 0; g < 5; g++) {
            for (int s = 0; s < 8; s++) {
                if (gateGrid[g][s] != null) {
                    System.out.println(" - Gate " + (g + 1) + " at " + TIME_SLOTS[s] + 
                                       " -> Flight " + gateGrid[g][s].getFlightId());
                }
            }
        }
    }

    public void displayScheduledFlights() {
        System.out.println("--- Scheduled Flights ---");
        boolean found = false;
        for (Flight f : flights) {
            if (f.getStatus() == FlightStatus.SCHEDULED) {
                f.displayDetails();
                System.out.println("-----------------------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No scheduled flights found.");
        }
    }

    public void displayDelayedFlights() {
        System.out.println("--- Delayed Flights ---");
        boolean found = false;
        for (Flight f : flights) {
            if (f.getStatus() == FlightStatus.DELAYED) {
                f.displayDetails();
                System.out.println("-----------------------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("No delayed flights found.");
        }
    }

    public void displayBusiestGate() {
        int maxCount = 0;
        int busiestGateIndex = -1;

        for (int g = 0; g < 5; g++) {
            int count = 0;
            for (int s = 0; s < 8; s++) {
                if (gateGrid[g][s] != null) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                busiestGateIndex = g;
            }
        }

        if (busiestGateIndex == -1 || maxCount == 0) {
            System.out.println("Busiest Gate: None (no gates assigned yet)");
        } else {
            System.out.println("Busiest Gate: Gate " + (busiestGateIndex + 1) + " (" + maxCount + " assigned flights)");
        }
    }

    public void displayPeakTimeSlot() {
        int maxCount = 0;
        int peakSlotIndex = -1;

        for (int s = 0; s < 8; s++) {
            int count = 0;
            for (int g = 0; g < 5; g++) {
                if (gateGrid[g][s] != null) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                peakSlotIndex = s;
            }
        }

        if (peakSlotIndex == -1 || maxCount == 0) {
            System.out.println("Peak Time Slot: None (no departures scheduled yet)");
        } else {
            System.out.println("Peak Time Slot: " + TIME_SLOTS[peakSlotIndex] + " (" + maxCount + " departures)");
        }
    }

    public void displayHighestPassengerLoadFlight() {
        if (flights.isEmpty()) {
            System.out.println("Highest Passenger Load Flight: None (no flights registered)");
            return;
        }

        Flight highest = flights.get(0);
        for (Flight f : flights) {
            if (f.calculatePassengerLoadPercentage() > highest.calculatePassengerLoadPercentage()) {
                highest = f;
            }
        }

        System.out.println("Highest Passenger Load Flight: " + highest.getFlightId() + 
                           " (" + String.format("%.2f", highest.calculatePassengerLoadPercentage()) + "%)");
    }

    public double calculateAveragePassengerLoadPercentage() {
        if (flights.isEmpty()) {
            return 0.0;
        }
        double totalPercentage = 0.0;
        for (Flight f : flights) {
            totalPercentage += f.calculatePassengerLoadPercentage();
        }
        return totalPercentage / flights.size();
    }
    
    // generating the full operational report and allowing a sorted list to be displayed using bubble sort

    public void generateFullOperationalReport() {
        System.out.println("--------------------------------------------------");
        System.out.println("            Operational Report              ");
        System.out.println("--------------------------------------------------");
        
        displayScheduledFlights();
        displayDelayedFlights();

        System.out.println("\n--- Stats ---");
        displayBusiestGate();
        displayPeakTimeSlot();
        displayHighestPassengerLoadFlight();
        System.out.printf("Average Passenger Load Percentage: %.2f%%\n", calculateAveragePassengerLoadPercentage());
        System.out.println("--------------------------------------------------");
    }

    public void sortFlightsById() {
        int n = flights.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (flights.get(j).getFlightId().compareToIgnoreCase(flights.get(j + 1).getFlightId()) > 0) {
                    Flight temp = flights.get(j);
                    flights.set(j, flights.get(j + 1));
                    flights.set(j + 1, temp);
                }
            }
        }
    }

    public void sortFlightsByDepartureTime() {
        int n = flights.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (flights.get(j).getDepartureTime().compareToIgnoreCase(flights.get(j + 1).getDepartureTime()) > 0) {
                    Flight temp = flights.get(j);
                    flights.set(j, flights.get(j + 1));
                    flights.set(j + 1, temp);
                }
            }
        }
    }
}