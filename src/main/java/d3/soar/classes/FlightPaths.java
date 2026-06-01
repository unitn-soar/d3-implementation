package d3.soar.classes;

import java.util.List;

public class FlightPaths {
    public List<FlightPath> flightPaths;

    // Default constructor
    public FlightPaths() {
    }

    // Constructor with all fields
    public FlightPaths(List<FlightPath> flightPaths) {
        this.flightPaths = flightPaths;
    }

    public void addFlightPath(FlightPath flightPath) {
        this.flightPaths.add(flightPath);
    }

    public void removeFlightPath(FlightPath flightPath) {
        this.flightPaths.remove(flightPath);
    }

    public void readFlightPathsFromFile(String filePath) {
        // Implement file reading logic to populate the flightPaths list
    }
}

