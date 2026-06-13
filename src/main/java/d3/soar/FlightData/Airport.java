package d3.soar.FlightData;

/* @brief Represents an airport in the system, containing the name, city, and nation*/
public class Airport {

    private final String name;
    private final String city;
    private final String nation;

    /* @brief Constructs a new airport object with the specified name, city, and nation*/
    public Airport(String name, String city, String nation) {
        this.name = name;
        this.city = city;
        this.nation = nation;
    }

    /* @brief Gets the name for the airport*/
    public String getName() {
        return name;
    }

/* @brief Gets the city for the airport*/
    public String getCity() {
        return city;
    }
    
/* @brief Gets the nation for the airport*/
    public String getNation() {
        return nation;
    }

    @Override
    /* @brief Returns a string representation of the airport object*/
    public String toString() {
        return "Airport{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", nation='" + nation + '\'' +
                '}';
    }
}