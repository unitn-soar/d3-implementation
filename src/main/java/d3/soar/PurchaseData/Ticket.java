package d3.soar.PurchaseData;

import d3.soar.FlightData.Airport;
import d3.soar.FlightData.Flight;

///@brief Represents a ticket in the system, containing the seat class, price, seat number, active status, and flight
/// @see Flight
public class Ticket {

    private String seatClass;
    private float price;
    private int seat;
    private boolean active = false;
    private Flight flight;

///@brief Constructs a new ticket with default values
    public Ticket() {
    }

///@brief Constructs a new ticket with the specified seat class, price, seat, and flight
///@param seatClass The class of the relative seat
/// @param price The price of the Ticket
/// @param seat The number of the relative seat
/// @param flight The flight relative to this ticket
/// @see Flight
    public Ticket(String seatClass, float price, int seat, Flight flight) {
        this.seatClass = seatClass;
        this.price = price;
        this.seat = seat;
        this.flight = flight;
    }

///@brief Gets the seat class for the ticket
    public String getSeatClass() {
        return seatClass;
    }

    ///@brief Sets the seat class for the ticket
    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    ///@brief Gets the price for the ticket
    public float getPrice() {
        return price;
    }

///@brief Sets the price for the ticket
    public void setPrice(float price) {
        this.price = price;
    }

    ///@brief Gets the seat for the ticket
    public int getSeat() {
        return seat;
    }

///@brief Sets the seat for the ticket
    public void setSeat(int seat) {
        this.seat = seat;
    }

///@brief Gets the active status for the ticket
    public boolean isActive() {
        return active;
    }

    ///@brief Sets the active status for the ticket
    public void setActive(boolean active) {
        this.active = active;
    }

    ///@brief Checks in the ticket
    public void CheckIn() {
        this.active = true;
    }

///@brief Gets the flight for the ticket
    public Flight getFlight() {
        return this.flight;
    }

///@brief Sets the flight for the ticket
    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    ///@brief Gets the destination Airport for the ticket
    public Airport getDestination() {
        return this.flight.getDestination();
    }

    ///@brief Gets the departure Airport for the ticket
    public Airport getDeparture() {
        return this.flight.getDeparture();
    }

    /// @brief Returns a string representation of the Ticket object
    /// @return A string representation of the Ticket object
    @Override
    public String toString() {
        return "Ticket{" +
                "\nseatClass='" + seatClass + '\'' +
                "\nprice=" + price +
                "\nseat=" + seat +
                "\nactive=" + active +
                "\nflight=" + flight +
                '}';
    }
}

