package d3.soar.FlightData;

/* @brief Represents an airline in the system, containing the airline name*/
public class Airline {

    private String airlineName;

    /* @brief Constructs a new airline object with the specified airline name*/
    public Airline() {
    }

    /* @brief Constructs a new airline object with the specified airline name*/
    public Airline(String airlineName) {
        this.airlineName = airlineName;
    }

    /* @brief Gets the airline name*/
    public String getAirlineName() {
        return airlineName;
    }

    /* @brief Sets the airline name*/
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    @Override
    /* @brief Returns a string representation of the airline object*/
    public String toString() {
        return "Airline{" +
                "airlineName='" + airlineName + '\'' +
                '}';
    }
}