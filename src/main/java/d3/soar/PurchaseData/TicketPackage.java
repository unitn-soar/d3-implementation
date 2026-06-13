package d3.soar.PurchaseData;
import java.util.List;

/* @brief Represents a ticket package in the system, containing a list of tickets*/
public class TicketPackage {
    private List<Ticket> tickets;

    /* @brief Constructs a new ticket package*/
    public TicketPackage() {
    }

    /* @brief Constructs a new ticket package with the specified list of tickets*/
    public TicketPackage(List<Ticket> tickets) {
        this.tickets = tickets;
    }   

    /* @brief Gets the list of tickets for the ticket package*/
    public List<Ticket> getTickets() {
        return tickets; 
    }   

    /* @brief Sets the list of tickets for the ticket package*/
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }   

    /* @brief Adds a ticket to the ticket package*/
    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    /* @brief Removes a ticket from the ticket package*/
    public void removeTicket(Ticket ticket) {
        this.tickets.remove(ticket);
    }

    @Override
    /* @brief Returns a string representation of the ticket package object*/
    public String toString() {
        return "TicketPackage{" +
                "tickets=" + tickets +
                '}';
    }
}
