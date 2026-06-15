package d3.soar.FlightData;

/// @brief Represents an airport in the system, containing the name, city, and nation
public class Airport {

    private final String name; 
    private final String city;
    private final String nation;

    /// @brief Constructs a new airport object with the specified name, city, and nation
    /// @param name The name of the airport
    /// @param city The city where the airport is located
    /// @param nation The nation where the airport is located
    public Airport(String name, String city, String nation) {
        this.name = name;
        this.city = city;
        this.nation = nation;
    }

    /// @brief Gets the name for the airport
    public String getName() {
        return name;
    }

    /// @brief Gets the city for the airport
    public String getCity() {
        return city;
    }

    /// @brief Gets the nation for the airport
    public String getNation() {
        return nation;
    }

    /// @brief Returns a string representation of the airport object
    /// @return A string representation of the airport object
    @Override
    public String toString() {
        return "Airport{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", nation='" + nation + '\'' +
                '}';
    }
}