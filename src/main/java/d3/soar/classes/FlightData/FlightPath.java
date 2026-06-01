package d3.soar.classes.FlightData;

public class FlightPath {

    private int duration;
    private float distance;
    private final Airport departure;
    private final Airport destination;

    // Constructor with all fields
    public FlightPath(int duration, float distance, Airport departure, Airport destination) {
        this.duration = duration;
        this.distance = distance;
        this.departure = departure;
        this.destination = destination;
    }

    // Getter for duration
    public int getDuration() {
        return duration;
    }

    // Setter for duration
    public void setDuration(int duration) {
        this.duration = duration;
    }

    // Getter for distance
    public float getDistance() {
        return distance;
    }

    // Setter for distance
    public void setDistance(float distance) {
        this.distance = distance;
    }

    // Getter for departure airport
    public Airport getDeparture() {
        return departure;
    }

    // Getter for destination airport
    public Airport getDestination() {
        return destination;
    }
}
