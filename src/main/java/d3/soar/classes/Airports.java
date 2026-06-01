package d3.soar.classes;

import java.util.List;

public class Airports {
    public List<Airport> airports;

    // Default constructor
    public Airports() {
    }

    // Constructor with all fields
    public Airports(List<Airport> airports) {
        this.airports = airports;
    }

    public void addAirport(Airport airport) {
        this.airports.add(airport);
    }

    public void removeAirport(Airport airport) {
        this.airports.remove(airport);
    }

    public void readAirportsFromFile(String filePath) {
        // Implement file reading logic to populate the airports list
    }
}

