package d3.soar;

import java.util.List;

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
