package com.mycompany.prog6112assignment1;

import org.junit.Test; // JUnit test imports that were automatically created
import static org.junit.Assert.*;

public class MainLogicTest {
    
    public MainLogicTest() { // constructor 
    }

    @Test // all tests are being filled with appropriate test data
    public void testGetFlights() {
        MainLogic logic = new MainLogic(); // creating a new object everytime so we can access the methods we are testing
        assertNotNull(logic.getFlights());
        assertTrue(logic.getFlights().isEmpty());
    }

    @Test
    public void testAddFlight() throws Exception { // to accomodate for the exception handling in the methods
        MainLogic logic = new MainLogic();
        Flight flight = new Flight("FL101", "Airline A", "JNB", 100, 50, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        logic.addFlight(flight);
        assertEquals(1, logic.getFlights().size());
        assertEquals("FL101", logic.getFlights().get(0).getFlightId());
    }

    @Test
    public void testSearchFlight() throws Exception {
        MainLogic logic = new MainLogic();
        Flight flight = new Flight("FL101", "Airline A", "JNB", 100, 50, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        logic.addFlight(flight);
        
        Flight found = logic.searchFlight("fl101"); // testing case-insensitive search
        assertNotNull(found);
        assertEquals("FL101", found.getFlightId());
    }

    @Test
    public void testUpdateFlightDetails() throws Exception {
        MainLogic logic = new MainLogic();
        Flight flight = new Flight("FL101", "Airline A", "JNB", 100, 50, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        logic.addFlight(flight);
        
        logic.updateFlightDetails("FL101", "Airline B", "CPT", 200, 100);
        Flight updated = logic.searchFlight("FL101");
        
        assertEquals("Airline B", updated.getAirlineName());
        assertEquals("CPT", updated.getFlightDestination());
        assertEquals(200, updated.getPassengerCapacity());
        assertEquals(100, updated.getNumOfBookedPassangers());
    }

    @Test
    public void testUpdateFlightStatus() throws Exception {
        MainLogic logic = new MainLogic();
        Flight flight = new Flight("FL101", "Airline A", "JNB", 100, 50, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        logic.addFlight(flight);
        
        logic.updateFlightStatus("FL101", FlightStatus.DELAYED);
        assertEquals(FlightStatus.DELAYED, logic.searchFlight("FL101").getStatus());
    }

    @Test
    public void testRemoveFlight() throws Exception {
        MainLogic logic = new MainLogic();
        Flight flight = new Flight("FL101", "Airline A", "JNB", 100, 50, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        logic.addFlight(flight);
        
        boolean removed = logic.removeFlight("FL101");
        assertTrue(removed);
        assertTrue(logic.getFlights().isEmpty());
    }

    @Test
    public void testDisplayAllFlights() {
        MainLogic logic = new MainLogic();
        logic.displayAllFlights();
    }

    @Test
    public void testAssignFlightToGate() throws Exception {
        MainLogic logic = new MainLogic();
        Flight flight = new Flight("FL101", "Airline A", "JNB", 100, 50, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        logic.addFlight(flight);
        
        logic.assignFlightToGate("FL101", 0, 0);
        assertTrue(true);
    }

    @Test
    public void testReleaseGateByFlightId() {
        MainLogic logic = new MainLogic();
        Flight flight = new Flight("FL101", "Airline A", "JNB", 100, 50, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        try {
            logic.addFlight(flight);
            logic.assignFlightToGate("FL101", 0, 0);
            logic.releaseGateByFlightId("FL101");
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReleaseGateByLocation() throws Exception {
        MainLogic logic = new MainLogic();
        Flight flight = new Flight("FL101", "Airline A", "JNB", 100, 50, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        logic.addFlight(flight);
        logic.assignFlightToGate("FL101", 0, 0);
        
        logic.releaseGateByLocation(0, 0);
        assertTrue(true);
    }

    @Test
    public void testDisplayGateScheduleGrid() {
        MainLogic logic = new MainLogic();
        logic.displayGateScheduleGrid();
    }

    @Test
    public void testDisplayAvailableSlots() {
        MainLogic logic = new MainLogic();
        logic.displayAvailableSlots();
    }

    @Test
    public void testDisplayOccupiedSlots() {
        MainLogic logic = new MainLogic();
        logic.displayOccupiedSlots();
    }

    @Test
    public void testGenerateFullOperationalReport() {
        MainLogic logic = new MainLogic();
        logic.generateFullOperationalReport();
    }

    @Test
    public void testDisplayHighestPassengerLoadFlight() {
        MainLogic logic = new MainLogic();
        logic.displayHighestPassengerLoadFlight();
    }

    @Test
    public void testSortFlightsById() throws Exception {
        MainLogic logic = new MainLogic();
        logic.addFlight(new Flight("FL102", "Airline B", "CPT", 100, 50, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED));
        logic.addFlight(new Flight("FL101", "Airline A", "DUR", 100, 50, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED));
        
        logic.sortFlightsById();
        
        assertEquals("FL101", logic.getFlights().get(0).getFlightId());
        assertEquals("FL102", logic.getFlights().get(1).getFlightId());
    }

    @Test
    public void testSortFlightsByDepartureTime() throws Exception {
        MainLogic logic = new MainLogic();
        Flight f1 = new Flight("FL101", "Airline A", "JNB", 100, 50, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        Flight f2 = new Flight("FL102", "Airline B", "CPT", 100, 50, FlightCategory.DOMESTIC, FlightStatus.SCHEDULED);
        
        logic.addFlight(f1);
        logic.addFlight(f2);
        
        logic.assignFlightToGate("FL102", 0, 1);
        logic.assignFlightToGate("FL101", 0, 0);
        
        logic.sortFlightsByDepartureTime();
        
        assertEquals("FL101", logic.getFlights().get(0).getFlightId());
    }
}