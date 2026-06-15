package d3.soar.MainLists;

import java.util.List;

import d3.soar.FlightData.FlightPath;

    /// @brief Represents the list of all the Flight Paths
    /// @see FlightPath
    public class FlightPaths {
    public List<FlightPath> flightPaths;

    ///@brief Constructs a new flight paths object with default values
    public FlightPaths() {
    }

    ///@brief Constructs a new flight paths object with the specified list of flight paths
    /// @param flightPaths A list of FlightPath
    public FlightPaths(List<FlightPath> flightPaths) {
        this.flightPaths = flightPaths;
    }

    ///@brief Adds a flight path to the flight paths list
    public void addFlightPath(FlightPath flightPath) {
        this.flightPaths.add(flightPath);
    }

    ///@brief Removes a flight path from the flight paths list
    public void removeFlightPath(FlightPath flightPath) {
        this.flightPaths.remove(flightPath);
    }

    ///@brief Reads flight paths from a file and populates the flight paths list (not implemented)
    public void readFlightPathsFromFile(String filePath) {
        // Implement file reading logic to populate the flightPaths list
    }

    ///@brief Gets the list of flight paths for the flight paths object
    public void setFlightPaths(FlightPaths flightPaths) {
        this.flightPaths = flightPaths.flightPaths;
    }

    /// @brief Returns a string representation of the list of Flight Paths
    /// @return A string representation of the list of Flight Paths
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (FlightPath flightPath : flightPaths) {
            sb.append(flightPath.toString()).append("\n");
        }
        return sb.toString();
    }
}
