package d3.soar.MainLists;

import java.util.List;

import d3.soar.FlightData.Plane;

/* @brief Represents a list of planes in the system, containing methods to add, remove, and read planes from a file*/
public class Planes {
    public List<Plane> planes;

    /* @brief Constructs a new planes object with the specified list of planes*/
    public Planes() {
    }

    /* @brief Constructs a new planes object with the specified list of planes*/
    public Planes(List<Plane> planes) {
        this.planes = planes;
    }

    /* @brief Adds a plane to the planes list*/
    public void addPlane(Plane plane) {
        this.planes.add(plane);
    }

    /* @brief Removes a plane from the planes list*/
    public void removePlane(Plane plane) {
        this.planes.remove(plane);
    }

    /* @brief Reads planes from a file and populates the planes list*/
    public void readPlanesFromFile(String filePath) {
        // Implement file reading logic to populate the planes list
    }

    /* @brief Gets the list of planes for the planes object*/
    public void setPlanes(Planes planes) {
        this.planes = planes.planes;
    }
}
