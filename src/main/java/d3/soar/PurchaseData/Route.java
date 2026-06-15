package d3.soar.PurchaseData;

import java.util.List;

///@brief Represents a route in the system, containing a list of tickets that make up the route
///@details Each ticket in the route must have a departure airport equal to the previous ticket's destination
/// @see Ticket
public class Route {
    private List<Ticket> routeTickets;

///@brief Constructs a new route with default values
    public Route() {
    }

///@brief Constructs a new route with the specified list of tickets
    public Route(List<Ticket> routeTickets) {
        this.routeTickets = routeTickets;
    }

    ///@brief Gets the list of tickets for the route
    public List<Ticket> getRouteTickets() {
        return routeTickets;
    }

    ///@brief Sets the list of tickets for the route
    public void setRouteTickets(List<Ticket> routeTickets) {
        this.routeTickets = routeTickets;
    }

    ///@brief Adds a ticket to the route
    ///@details The ticket is added only if the new departure matches the previous destination
    public void addTicket(Ticket ticket) {
        if(ticket.getDeparture() == this.routeTickets.get(this.routeTickets.size() - 1).getDestination()) {
            this.routeTickets.add(ticket);
        }
        else{
            System.out.println("The departure Airport of this ticket does not match the destination Airport of the last ticket in the route.");
        }
        if(this.routeTickets.isEmpty()) {
            this.routeTickets.add(ticket);
        }
    }

    /// @brief Returns a string representation of the Route object
    /// @return A string representation of the Route object
    @Override
    public String toString() {
        return "Route{" +
                "routeTickets=" + routeTickets +
                '}';
    }
}
