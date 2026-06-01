package d3.soar.classes;

import java.util.List;

public class Flights {
    public List<Flight> flights;

    // Default constructor
    public Flights() {
    }

    // Constructor with all fields
    public Flights(List<Flight> flights) {
        this.flights = flights;
    }

    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }

    public void removeFlight(Flight flight) {
        this.flights.remove(flight);
    }

    public void readFlightsFromFile(String filePath) {
        // Implement file reading logic to populate the flights list
    }
}
