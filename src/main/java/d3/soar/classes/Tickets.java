package d3.soar;

import java.util.List;

public class Tickets {
    public List<Ticket> tickets;

    // Default constructor
    public Tickets() {
    }

    // Constructor with all fields
    public Tickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    public void removeTicket(Ticket ticket) {
        this.tickets.remove(ticket);
    }

    public void readTicketsFromFile(String filePath) {
        // Implement file reading logic to populate the tickets list
    }
}
