package d3.soar;

public class Airline {

    private String airlineName;

    // Default constructor
    public Airline() {
    }

    // Constructor with airlineName
    public Airline(String airlineName) {
        this.airlineName = airlineName;
    }

    // Getter for airlineName
    public String getAirlineName() {
        return airlineName;
    }

    // Setter for airlineName
    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }
}