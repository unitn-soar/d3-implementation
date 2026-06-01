package d3.soar.classes.FlightData;

import java.util.Date;

public class Flight {

    private Date departureTime;
    private Plane plane;
    private FlightPath flightPath;

    // Default constructor
    public Flight() {
    }

    // Constructor with all fields
    public Flight(Date departureTime, Plane plane, FlightPath flightPath) {
        this.departureTime = departureTime;
        this.plane = plane;
        this.flightPath = flightPath;
    }

    // Getter for departureTime
    public Date getDepartureTime() {
        return departureTime;
    }

    // Setter for departureTime
    public void delay(Date departureTime) {
        this.departureTime = departureTime;
    }

    // Method to delay the flight by a certain number of minutes
    public void delay(int minutes) {
        if (departureTime != null) {
            departureTime.setTime(departureTime.getTime() + minutes * 60 * 1000);
        }
    }

    // Getter for plane
    public Plane getPlane() {
        return plane;
    }

    // Setter for plane
    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    // Getter for flightPath
    public FlightPath getFlightPath() {
        return flightPath;
    }

    // Setter for flightPath
    public void setFlightPath(FlightPath flightPath) {
        this.flightPath = flightPath;
    }
}
