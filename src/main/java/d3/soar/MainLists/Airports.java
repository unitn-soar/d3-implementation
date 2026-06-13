package d3.soar.MainLists;

import java.util.List;

import d3.soar.FlightData.Airport;

/* @brief Represents a list of airports in the system, containing methods to add, remove, and read airports from a file*/
public class Airports {
    public List<Airport> airports;

    /* @brief Constructs a new airports object with the specified list of airports*/
    public Airports() {
    }

    /* @brief Constructs a new airports object with the specified list of airports*/
    public Airports(List<Airport> airports) {
        this.airports = airports;
    }

/* @brief Adds an airport to the airports list*/
    public void addAirport(Airport airport) {
        this.airports.add(airport);
    }

    /* @brief Removes an airport from the airports list*/
    public void removeAirport(Airport airport) {
        this.airports.remove(airport);
    }

    /* @brief Reads airports from a file and populates the airports list*/
    public void readAirportsFromFile(String filePath) {
        // Implement file reading logic to populate the airports list
    }

    /* @brief Gets the list of airports for the airports object*/
    public void setAirports(Airports airports) {
        this.airports = airports.airports;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Airport airport : airports) {
            sb.append(airport.toString()).append("\n");
        }
        return sb.toString();
    }
}

