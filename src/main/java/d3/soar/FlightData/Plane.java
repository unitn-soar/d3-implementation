package d3.soar.FlightData;

/* @brief Represents a plane in the system, containing the plane ID, number of seats, and airline*/
public class Plane {

    private final int id;
    private final int seats;
    private Airline airline;

    /* @brief Constructs a new plane object with the specified ID, number of seats, and airline*/
    public Plane(int id, int seats, Airline airline) {
        this.id = id;
        this.seats = seats;
        this.airline = airline;
    }

    /* @brief Gets the ID for the plane*/
    public int getId() {
        return id;
    }

    /* @brief Gets the number of seats for the plane*/
    public int getSeats() {
        return seats;
    }

    /* @brief Gets the airline for the plane*/
    public Airline getAirline() {
        return airline;
    }

    /* @brief Sets the airline for the plane*/
    public void setAirline(Airline airline) {
        this.airline = airline;
    }

    @Override
    /* @brief Returns a string representation of the plane object*/
    public String toString() {
        return "Plane{" +
                "id=" + id +
                ", seats=" + seats +
                ", airline=" + airline +
                '}';
    }
}