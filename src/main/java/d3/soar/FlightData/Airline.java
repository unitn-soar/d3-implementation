package d3.soar.FlightData;

/// @brief The Airline class represents an airline with its name and provides methods to access and modify the airline's information.
public class Airline {

    private String airlineName;

    /// @brief Constructs a new airline object with default values
    public Airline() {
    }

    ///@brief Constructs a new airline object with the specified airline name
    ///@param airlineName The name of the Airline
    public Airline(String airlineName) {
        this.airlineName = airlineName;
    }

    /// @brief Gets the airline name
    public String getAirlineName() {
        return airlineName;
    }

    /// @brief Sets the airline name
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    /// Returns a string representation of the airline object
    /// @return A string representation of the airline object
    @Override
    public String toString() {
        return airlineName;
    }
}