package d3.soar.MainLists;

import java.util.List;

import d3.soar.PurchaseData.Route;

    /// @brief Represents the list of all the Routes
    /// @see Route
public class Routes {
    public List<Route> routes;

    ///@brief Constructs a new routes object with default values
    public Routes() {
    }

    ///@brief Constructs a new routes object with the specified list of routes
    /// @param routes A list of Routes
    public Routes(List<Route> routes) {
        this.routes = routes;
    }

    ///@brief Adds a route to the routes list
    public void addRoute(Route route) {
        this.routes.add(route);
    }

    ///@brief Removes a route from the routes list
    public void removeRoute(Route route) {
        this.routes.remove(route);
    }

    ///@brief Reads routes from a file and populates the routes list (not implemented)
    public void readRoutesFromFile(String filePath) {
    }

    ///@brief Gets the list of routes for the routes object
    public void setRoutes(Routes routes) {
        this.routes = routes.routes;
    }

    /// @brief Returns a string representation of the list of Routes
    /// @return A string representation of the list of Routes
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Route route : routes) {
            sb.append(route.toString()).append("\n");
        }
        return sb.toString();
    }
}

