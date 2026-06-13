package d3.soar.PurchaseData;

import java.util.List;

/* @brief Represents a route in the system, containing a list of tickets that make up the route*/
public class Route {
    private List<Ticket> routeTickets;
/* @brief Constructs a new route*/
    public Route() {
    }

/* @brief Constructs a new route with the specified list of tickets*/
    public Route(List<Ticket> routeTickets) {
        this.routeTickets = routeTickets;
    }

    /* @brief Gets the list of tickets for the route*/
    public List<Ticket> getRouteTickets() {
        return routeTickets;
    }

    /* @brief Sets the list of tickets for the route*/
    public void setRouteTickets(List<Ticket> routeTickets) {
        this.routeTickets = routeTickets;
    }

    /* @brief Adds a ticket to the route*/
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

    @Override
    /* @brief Returns a string representation of the route object*/
    public String toString() {
        return "Route{" +
                "routeTickets=" + routeTickets +
                '}';
    }
}
