package d3.soar.PurchaseData;
import java.util.List;

public class TicketPackage {
    private List<Ticket> tickets;

    // Default constructor
    public TicketPackage() {
    }

    // Constructor with all fields
    public TicketPackage(List<Ticket> tickets) {
        this.tickets = tickets;
    }   

    // Getter for tickets
    public List<Ticket> getTickets() {
        return tickets; 
    }   

    // Setter for tickets
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }   

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    public void removeTicket(Ticket ticket) {
        this.tickets.remove(ticket);
    }
}
