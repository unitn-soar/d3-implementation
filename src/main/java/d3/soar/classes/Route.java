package d3.soar;

import java.util.List;

public class Route {
    private List<Ticket> routeTickets;
    // Default constructor
    public Route() {
    }

    // Constructor with all fields
    public Route(List<Ticket> routeTickets) {
        this.routeTickets = routeTickets;
    }

    // Getter for routeTickets
    public List<Ticket> getRouteTickets() {
        return routeTickets;
    }

    // Setter for routeTickets
    public void setRouteTickets(List<Ticket> routeTickets) {
        this.routeTickets = routeTickets;
    }

    // Method to add a ticket to the route
    public void addTicket(Ticket ticket) {
        if(ticket.getFlightPath() == this.routeTickets.get(this.routeTickets.size() - 1).getFlightPath()) {
            this.routeTickets.add(ticket);
        }
        else{
            System.out.println("The flight path of the ticket does not match the route's last ticket.");
        }
        if(this.routeTickets.isEmpty()) {
            this.routeTickets.add(ticket);
        }
    }
}
