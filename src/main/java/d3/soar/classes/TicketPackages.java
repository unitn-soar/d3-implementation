package d3.soar;

import java.util.List;

public class TicketPackages {

    public List<TicketPackage> ticketpackages;

    // Default constructor
    public TicketPackages() {
    }
    // Constructor with all fields
    public TicketPackages(List<TicketPackage> ticketpackages) {
        this.ticketpackages = ticketpackages;
    }

    public void addpackage(TicketPackage ticketPackage) {
        this.ticketpackages.add(ticketPackage);
    }

    public void removeTicketPackage(TicketPackage ticketPackage) {
        this.ticketpackages.remove(ticketPackage);
    }

    public void readTicketPackagesFromFile(String filePath) {
        // Implement file reading logic to populate the packages list
    }
}

