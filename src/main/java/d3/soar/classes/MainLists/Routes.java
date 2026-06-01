package d3.soar.classes.MainLists;

import java.util.List;

import d3.soar.classes.PurchaseData.Route;

public class Routes {
    public List<Route> routes;

    // Default constructor
    public Routes() {
    }

    // Constructor with all fields
    public Routes(List<Route> routes) {
        this.routes = routes;
    }

    public void addRoute(Route route) {
        this.routes.add(route);
    }

    public void removeRoute(Route route) {
        this.routes.remove(route);
    }

    public void readRoutesFromFile(String filePath) {
        // Implement file reading logic to populate the routes list
    }
}

