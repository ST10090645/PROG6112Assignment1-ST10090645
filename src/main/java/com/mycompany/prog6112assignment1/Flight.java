package com.mycompany.prog6112assignment1;

public class Flight {
    private String flightId; // declaring variables
    private String airlineName;
    private String flightDestination;
    private int passengerCapacity;
    private int numOfBookedPassangers;
    private FlightCategory category;
    private FlightStatus status;
    private String departureTime = "Unscheduled"; 

    public Flight(String flightId, String airlineName, String flightDestination, // main constructor with variables passed through
                  int passengerCapacity, int numOfBookedPassangers, 
                  FlightCategory category, FlightStatus status) {
        setFlightId(flightId);
        setAirlineName(airlineName);
        setFlightDestination(flightDestination);
        this.category = category; // set category before capacity/passengers so validation knows if it's cargo
        this.status = status;
        setPassengerCapacity(passengerCapacity);
        setNumOfBookedPassangers(numOfBookedPassangers);
    }

    public String getFlightId() { // setters & getters + exception handeling 
        return flightId;
    }

    public void setFlightId(String flightId) {
        if (flightId == null || flightId.trim().isEmpty()) {
            throw new IllegalArgumentException("Flight ID cannot be empty.");
        }
        this.flightId = flightId;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        if (airlineName == null || airlineName.trim().isEmpty()) {
            throw new IllegalArgumentException("Airline name cannot be empty.");
        }
        this.airlineName = airlineName;
    }

    public String getFlightDestination() {
        return flightDestination;
    }

    public void setFlightDestination(String flightDestination) {
        if (flightDestination == null || flightDestination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be empty.");
        }
        this.flightDestination = flightDestination;
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        if (this.category != FlightCategory.CARGO && passengerCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        if (passengerCapacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative.");
        }
        this.passengerCapacity = passengerCapacity;
    }

    public int getNumOfBookedPassangers() {
        return numOfBookedPassangers;
    }

    public void setNumOfBookedPassangers(int numOfBookedPassangers) {
        if (numOfBookedPassangers < 0 || numOfBookedPassangers > this.passengerCapacity) {
            throw new IllegalArgumentException("Booked passengers cannot be negative or exceed capacity.");
        }
        this.numOfBookedPassangers = numOfBookedPassangers;
    }

    public FlightCategory getCategory() {
        return category;
    }

    public void setCategory(FlightCategory category) {
        this.category = category;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = (departureTime == null || departureTime.trim().isEmpty()) 
                             ? "Unscheduled" : departureTime;
    }

    public double calculatePassengerLoadPercentage() { // calculations of passanger load 
        if (passengerCapacity == 0) {
            return 0.0; // prevents division by zero (NaN) for cargo flights
        }
        return ((double) numOfBookedPassangers / passengerCapacity) * 100.0;
    }

    public void displayDetails() { // method to display flight details
        System.out.println("Flight ID: " + flightId);
        System.out.println("Airline: " + airlineName);
        System.out.println("Destination: " + flightDestination);
        System.out.println("Category: " + category);
        System.out.println("Status: " + status);
        System.out.println("Departure Time: " + departureTime);
        System.out.println("Capacity: " + numOfBookedPassangers + "/" + passengerCapacity + 
                           " (" + String.format("%.2f", calculatePassengerLoadPercentage()) + "%)");
    }
}