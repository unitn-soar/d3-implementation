package d3.soar;

public class Plane {

    private final int id;
    private final int seats;
    private Airline airline;

    // Constructor with all fields
    public Plane(int id, int seats, Airline airline) {
        this.id = id;
        this.seats = seats;
        this.airline = airline;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Getter for seats
    public int getSeats() {
        return seats;
    }

    // Getter for airline
    public Airline getAirline() {
        return airline;
    }

    // Setter for airline
    public void setAirline(Airline airline) {
        this.airline = airline;
    }
}