package d3.soar.MainLists;

import java.util.List;

import d3.soar.FlightData.Airline;

/* @brief Represents a list of airlines in the system, containing methods to add, remove, and read airlines from a file*/
public class Airlines {
    public List<Airline> airlines;

    /* @brief Constructs a new airlines object with the specified list of airlines*/
    public Airlines() {
    }

    /* @brief Constructs a new airlines object with the specified list of airlines*/
    public Airlines(List<Airline> airlines) {
        this.airlines = airlines;
    }

    /* @brief Adds an airline to the airlines list*/
    public void addAirline(Airline airline) {
        this.airlines.add(airline);
    }

    /* @brief Removes an airline from the airlines list*/
    public void removeAirline(Airline airline) {
        this.airlines.remove(airline);
    }

    /* @brief Reads airlines from a file and populates the airlines list*/
    public void readAirlinesFromFile(String filePath) {
        // Implement file reading logic to populate the airlines list
    }

    /* @brief Gets the list of airlines for the airlines object*/
    public void setAirlines(Airlines airlines) {
        this.airlines = airlines.airlines;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Airline airline : airlines) {
            sb.append(airline.toString()).append("\n");
        }
        return sb.toString();
    }
}
