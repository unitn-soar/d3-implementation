package d3.soar.MainLists;

import java.util.List;

import d3.soar.PurchaseData.Route;

/* @brief Represents a list of routes in the system, containing methods to add, remove, and read routes from a file*/
public class Routes {
    public List<Route> routes;

    /* @brief Constructs a new routes object with the specified list of routes*/
    public Routes() {
    }

    /* @brief Constructs a new routes object with the specified list of routes*/
    public Routes(List<Route> routes) {
        this.routes = routes;
    }

    /* @brief Adds a route to the routes list*/
    public void addRoute(Route route) {
        this.routes.add(route);
    }

    /* @brief Removes a route from the routes list*/
    public void removeRoute(Route route) {
        this.routes.remove(route);
    }

    /* @brief Reads routes from a file and populates the routes list*/
    public void readRoutesFromFile(String filePath) {
        // Implement file reading logic to populate the routes list
    }

    /* @brief Gets the list of routes for the routes object*/
    public void setRoutes(Routes routes) {
        this.routes = routes.routes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Route route : routes) {
            sb.append(route.toString()).append("\n");
        }
        return sb.toString();
    }
}

