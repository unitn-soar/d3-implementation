package d3.soar.MainLists;

import java.util.List;

import d3.soar.FlightData.Flight;

    /// @brief Represents the list of all the Flights
    /// @see Flight
    public class Flights {
    public List<Flight> flights;

    ///@brief Constructs a new flights object with default values
    public Flights() {
    }

    ///@brief Constructs a new flights object with the specified list of flights
    /// @param flights A list of Flights
    public Flights(List<Flight> flights) {
        this.flights = flights;
    }

    ///@brief Adds a flight to the flights list
    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }

    ///@brief Removes a flight from the flights list
    public void removeFlight(Flight flight) {
        this.flights.remove(flight);
    }

    ///@brief Reads flights from a file and populates the flights list (not implemented)
    public void readFlightsFromFile(String filePath) {
    }

    ///@brief Gets the list of flights for the flights object
    public void setFlights(Flights flights) {
        this.flights = flights.flights;
    }

    /// @brief Returns a string representation of the list of Flights
    /// @return A string representation of the list of Flights
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Flight flight : flights) {
            sb.append(flight.toString()).append("\n");
        }
        return sb.toString();
    }
}
