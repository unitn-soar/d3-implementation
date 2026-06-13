package d3.soar.MainLists;

import java.util.List;

import d3.soar.PurchaseData.TicketPackage;

/* @brief The TicketPackages class represents a collection of ticket packages. It provides methods to add, remove, and read ticket packages from a file. */
public class TicketPackages {

    public List<TicketPackage> ticketpackages;

    /* @brief Constructs a new ticket packages object with the specified list of ticket packages*/
    public TicketPackages() {
    }

/* @brief Constructs a new ticket packages object with the specified list of ticket packages*/
    public TicketPackages(List<TicketPackage> ticketpackages) {
        this.ticketpackages = ticketpackages;
    }

    /* @brief Adds a ticket package to the ticket packages list*/
    public void addpackage(TicketPackage ticketPackage) {
        this.ticketpackages.add(ticketPackage);
    }

    /* @brief Removes a ticket package from the ticket packages list*/
    public void removeTicketPackage(TicketPackage ticketPackage) {
        this.ticketpackages.remove(ticketPackage);
    }

    /* @brief Reads ticket packages from a file and populates the ticket packages list*/
    public void readTicketPackagesFromFile(String filePath) {
        // Implement file reading logic to populate the packages list
    }

    /* @brief Gets the list of ticket packages for the ticket packages object*/
    public void setTicketPackages(TicketPackages ticketPackages) {
        this.ticketpackages = ticketPackages.ticketpackages;
    }
}

