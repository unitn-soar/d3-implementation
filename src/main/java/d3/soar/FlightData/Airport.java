package d3.soar.FlightData;

public class Airport {

    private final String name;
    private final String nation;

    // Constructor with all fields
    public Airport(String name, String nation) {
        this.name = name;
        this.nation = nation;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for nation
    public String getNation() {
        return nation;
    }
}