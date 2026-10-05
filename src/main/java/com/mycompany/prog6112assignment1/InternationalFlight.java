package com.mycompany.prog6112assignment1;

public class InternationalFlight extends Flight { // class is inheiriting from Flight class file
    private String departureTerminal;
    private String boardingGate;
    private boolean customsClearance;

    public InternationalFlight(String flightId, String airlineName, String flightDestination,
                               int passengerCapacity, int numOfBookedPassangers,
                               FlightStatus status, String departureTerminal,
                               String boardingGate, boolean customsClearanceRequired) { // using super() for constructor chaining
        super(flightId, airlineName, flightDestination, passengerCapacity,
              numOfBookedPassangers, FlightCategory.INTERNATIONAL, status);
        setDepartureTerminal(departureTerminal);
        setBoardingGate(boardingGate);
        setCustomsClearanceRequired(customsClearanceRequired);
    }

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public final void setDepartureTerminal(String departureTerminal) {
        if (departureTerminal == null || departureTerminal.trim().isEmpty()) {
            throw new IllegalArgumentException("Departure terminal cannot be empty.");
        }
        this.departureTerminal = departureTerminal.trim();
    }

    public String getBoardingGate() {
        return boardingGate;
    }

    public final void setBoardingGate(String boardingGate) {
        if (boardingGate == null || boardingGate.trim().isEmpty()) {
            throw new IllegalArgumentException("Boarding gate cannot be empty.");
        }
        this.boardingGate = boardingGate.trim();
    }

    public boolean isCustomsClearanceRequired() {
        return customsClearance;
    }

    public final void setCustomsClearanceRequired(boolean customsClearanceRequired) {
        this.customsClearance = customsClearanceRequired;
    }

    @Override // overrding the Display details method from the Flight Class
    public void displayDetails() {
        super.displayDetails();
        System.out.printf("   [International Details] Terminal: %-5s | Gate: %-5s | Customs Clearance: %s%n",
                departureTerminal,
                boardingGate,
                customsClearance ? "Yes" : "No"
        );
    }
}