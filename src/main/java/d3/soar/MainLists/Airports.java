package d3.soar.MainLists;

import java.util.List;

import d3.soar.FlightData.Airport;

    /// @brief Represents the list of all the Airports
    /// @see Airport
public class Airports {
    public List<Airport> airports;

    ///@brief Constructs a new airports object with default values
    public Airports() {
    }

    ///@brief Constructs a new airports object with the specified list of airports
    ///@param airports A list of Airports
    public Airports(List<Airport> airports) {
        this.airports = airports;
    }

///@brief Adds an airport to the airports list
    public void addAirport(Airport airport) {
        this.airports.add(airport);
    }

    ///@brief Removes an airport from the airports list
    public void removeAirport(Airport airport) {
        this.airports.remove(airport);
    }

    ///@brief Reads airports from a file and populates the airports list (not implemented)
    public void readAirportsFromFile(String filePath) {
        // Implement file reading logic to populate the airports list
    }

    ///@brief Gets the list of airports for the airports object
    public void setAirports(Airports airports) {
        this.airports = airports.airports;
    }

    /// @brief Returns a string representation of the list of Airports
    /// @return A string representation of the list of Airports
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Airport airport : airports) {
            sb.append(airport.toString()).append("\n");
        }
        return sb.toString();
    }
}

