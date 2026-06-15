package d3.soar.MainLists;

import java.util.List;

import d3.soar.FlightData.Plane;

    /// @brief Represents the list of all the Planes
    /// @see Plane
    public class Planes {
    public List<Plane> planes;

    ///@brief Constructs a new planes object with default values
    public Planes() {
    }

    ///@brief Constructs a new planes object with the specified list of planes
    /// @param planes A list of Planes
    public Planes(List<Plane> planes) {
        this.planes = planes;
    }

    ///@brief Adds a plane to the planes list
    public void addPlane(Plane plane) {
        this.planes.add(plane);
    }

    ///@brief Removes a plane from the planes list
    public void removePlane(Plane plane) {
        this.planes.remove(plane);
    }

    ///@brief Reads planes from a file and populates the planes list (not implemented)
    public void readPlanesFromFile(String filePath) {
    }

    ///@brief Gets the list of planes for the planes object
    public void setPlanes(Planes planes) {
        this.planes = planes.planes;
    }

    /// @brief Returns a string representation of the list of Planes
    /// @return A string representation of the list of Planes
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Plane plane : planes) {
            sb.append(plane.toString()).append("\n");
        }
        return sb.toString();
    }
}
