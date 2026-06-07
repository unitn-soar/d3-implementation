package d3.soar.MainLists;

import java.util.List;

import d3.soar.FlightData.Plane;

public class Planes {
    public List<Plane> planes;

    // Default constructor
    public Planes() {
    }

    // Constructor with all fields
    public Planes(List<Plane> planes) {
        this.planes = planes;
    }

    public void addPlane(Plane plane) {
        this.planes.add(plane);
    }

    public void removePlane(Plane plane) {
        this.planes.remove(plane);
    }

    public void readPlanesFromFile(String filePath) {
        // Implement file reading logic to populate the planes list
    }
}
