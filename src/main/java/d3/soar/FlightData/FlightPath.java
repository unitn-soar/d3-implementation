package d3.soar.FlightData;

/// @brief Represents a flight path in the system, containing the duration, distance, departure airport, and destination airport
/// @see Airport
public class FlightPath {

    private int duration;
    private float distance;
    private final Airport departure;
    private final Airport destination;

    /// @brief Constructs a new flight path object with the specified parameters
    /// @param departure The departure airport for the flight path
    /// @param destination The destination airport for the flight path
    /// @param duration The duration for the flight path
    /// @param distance The distance for the flight path
    /// @see Airport
    public FlightPath(Airport departure, Airport destination, int duration, float distance) {
        this.duration = duration;
        this.distance = distance;
        this.departure = departure;
        this.destination = destination;
    }

    /// @brief Gets the duration for the flight path in minutes
    public int getDuration() {
        return duration;
    }

    /// @brief Sets the duration for the flight path
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /// @brief Gets the distance for the flight path
    public float getDistance() {
        return distance;
    }

    /// @brief Sets the distance for the flight path
    public void setDistance(float distance) {
        this.distance = distance;
    }

    /// @brief Gets the departure airport for the flight path
    public Airport getDeparture() {
        return departure;
    }

    /// @brief Gets the destination airport for the flight path
    public Airport getDestination() {
        return destination;
    }

    /// @brief Returns a string representation of the flight path object
    /// @return A string representation of the flight path object
    @Override
    public String toString() {
        return "FlightPath{" +
                "duration=" + duration +
                ", distance=" + distance +
                ", departure=" + departure +
                ", destination=" + destination +
                '}';
    }
}
