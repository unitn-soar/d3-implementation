package d3.soar.FlightData;

import java.util.Date;

/// @brief Represents a flight in the system, containing the departure time, plane, and flight path
/// @see FlightPaths
/// @see Plane
public class Flight {

    private Date departureTime;
    private Plane plane;
    private FlightPath flightPath;

    /// @brief Constructs a new flight object with default values
    public Flight() {
    }

    /// @brief Constructs a new flight object with the specified departure time, plane, and flight path
    /// @param departureTime The departure time for the flight
    /// @param plane The plane for the flight
    /// @param flightPath The flight path for the flight
    public Flight(Date departureTime, Plane plane, FlightPath flightPath) {
        this.departureTime = departureTime;
        this.plane = plane;
        this.flightPath = flightPath;
    }

    /// @brief Gets the departure time for the flight
    public Date getDepartureTime() {
        return departureTime;
    }

    /// @brief Sets the departure time for the flight
    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    /// @brief Delays the flight by a certain number of minutes
    /// @param minutes The number of minutes to delay the flight
    public void delay(int minutes) {
        if (departureTime != null) {
            departureTime.setTime(departureTime.getTime() + minutes * 60 * 1000);
        }
    }

    /// @brief Gets the plane for the flight
    public Plane getPlane() {
        return plane;
    }

    /// @brief Sets the plane for the flight
    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    /// @brief Gets the flight path for the flight
    public FlightPath getFlightPath() {
        return flightPath;
    }

    /// @brief Sets the flight path for the flight
    public void setFlightPath(FlightPath flightPath) {
        this.flightPath = flightPath;
    }

    public Airport getDestination(){
        return this.flightPath.getDestination();
    }

    public Airport getDeparture(){
        return this.flightPath.getDeparture();
    }


    /// @brief Returns a string representation of the flight object
    /// @return A string representation of the flight object
    @Override
    public String toString() {
        return "Flight{" +
                "\ndepartureTime=" + departureTime +
                "\nplane=" + plane +
                "\nflightPath=" + flightPath +
                '}';
    }
}
