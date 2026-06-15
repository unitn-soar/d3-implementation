package d3.soar.MainLists;

import java.util.List;

import d3.soar.PurchaseData.Ticket;

    /// @brief Represents the list of all the Tickets
    /// @see Ticket
public class Tickets {
    public List<Ticket> tickets;

    ///@brief Constructs a new tickets object with default values
    public Tickets() {
    }

    ///@brief Constructs a new tickets object with the specified list of tickets
    /// @param tickets A list of Tickets
    public Tickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    ///@brief Adds a ticket to the tickets list
    public void addTicket(Ticket ticket) {
        this.tickets.add(ticket);
    }

    ///@brief Removes a ticket from the tickets list
    public void removeTicket(Ticket ticket) {
        this.tickets.remove(ticket);
    }

    ///@brief Reads tickets from a file and populates the tickets list (not implemented)
    public void readTicketsFromFile(String filePath) {
        // Implement file reading logic to populate the tickets list
    }

    ///@brief Gets the list of tickets for the tickets object
    public void setTickets(Tickets tickets) {
        this.tickets = tickets.tickets;
    }

    /// @brief Returns a string representation of the list of Tickets
    /// @return A string representation of the list of Tickets
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Ticket ticket : tickets) {
            sb.append(ticket.toString()).append("\n");
        }
        return sb.toString();
    }

}
