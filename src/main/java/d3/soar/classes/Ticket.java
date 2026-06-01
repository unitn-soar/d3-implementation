package d3.soar.classes;

public class Ticket {

    private int seatClass;
    private float price;
    private int seat;
    private boolean active = false;
    private Flight flight;

    // Default constructor
    public Ticket() {
    }

    // Constructor with all fields
    public Ticket(int seatClass, float price, int seat, Flight flight) {
        this.seatClass = seatClass;
        this.price = price;
        this.seat = seat;
        this.flight = flight;
    }

    // Getter for seatClass
    public int getSeatClass() {
        return seatClass;
    }

    // Setter for seatClass
    public void setSeatClass(int seatClass) {
        this.seatClass = seatClass;
    }

    // Getter for price
    public float getPrice() {
        return price;
    }

    // Setter for price
    public void setPrice(float price) {
        this.price = price;
    }

    // Getter for seat
    public int getSeat() {
        return seat;
    }

    // Setter for seat
    public void setSeat(int seat) {
        this.seat = seat;
    }

    // Getter for active
    public boolean isActive() {
        return active;
    }

    // Setter for active
    public void setActive(boolean active) {
        this.active = active;
    }

    public void CheckIn() {
        this.active = true;
    }

    // Getter for flight    
    public Flight getFlight() {
        return this.flight;
    }

    // Setter for flight
    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public FlightPath getFlightPath() {
        return this.flight.getFlightPath();
    }
}

