package d3.soar.classes;

import java.util.List;

public class Airlines {
    public List<Airline> airlines;

    // Default constructor
    public Airlines() {
    }

    // Constructor with all fields
    public Airlines(List<Airline> airlines) {
        this.airlines = airlines;
    }

    public void addAirline(Airline airline) {
        this.airlines.add(airline);
    }

    public void removeAirline(Airline airline) {
        this.airlines.remove(airline);
    }

    public void readAirlinesFromFile(String filePath) {
        // Implement file reading logic to populate the airlines list
    }
}
