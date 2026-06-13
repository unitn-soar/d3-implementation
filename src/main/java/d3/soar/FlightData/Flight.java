package d3.soar.FlightData;

/* @brief Represents a flight in the system, containing the departure time, plane, and flight path*/
import java.util.Date;

public class Flight {

    private Date departureTime;
    private Plane plane;
    private FlightPath flightPath;

    /* @brief Constructs a new flight object with the specified departure time, plane, and flight path*/
    public Flight() {
    }

    /* @brief Constructs a new flight object with the specified departure time, plane, and flight path*/
    public Flight(Date departureTime, Plane plane, FlightPath flightPath) {
        this.departureTime = departureTime;
        this.plane = plane;
        this.flightPath = flightPath;
    }

    /* @brief Gets the departure time for the flight*/
    public Date getDepartureTime() {
        return departureTime;
    }

    /* @brief Delays the flight by a certain number of minutes*/
    public void delay(Date departureTime) {
        this.departureTime = departureTime;
    }

    /* @brief Delays the flight by a certain number of minutes*/
    public void delay(int minutes) {
        if (departureTime != null) {
            departureTime.setTime(departureTime.getTime() + minutes * 60 * 1000);
        }
    }

    /* @brief Gets the plane for the flight*/
    public Plane getPlane() {
        return plane;
    }

    /* @brief Sets the plane for the flight*/
    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    /* @brief Gets the flight path for the flight*/
    public FlightPath getFlightPath() {
        return flightPath;
    }

    /* @brief Sets the flight path for the flight*/
    public void setFlightPath(FlightPath flightPath) {
        this.flightPath = flightPath;
    }

    @Override
    /* @brief Returns a string representation of the flight object*/
    public String toString() {
        return "Flight{" +
                "departureTime=" + departureTime +
                ", plane=" + plane +
                ", flightPath=" + flightPath +
                '}';
    }
}
