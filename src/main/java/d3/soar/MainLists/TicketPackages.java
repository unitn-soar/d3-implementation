package d3.soar.MainLists;

import java.util.List;

import d3.soar.PurchaseData.TicketPackage;

    /// @brief Represents the list of all the Ticket Packages
    /// @see TicketPackage
public class TicketPackages {

    public List<TicketPackage> ticketpackages;

    ///@brief Constructs a new ticket packages object with default values
    public TicketPackages() {
    }

///@brief Constructs a new ticket packages object with the specified list of ticket packages 
/// @param ticketpackages A list of TicketPackages
    public TicketPackages(List<TicketPackage> ticketpackages) {
        this.ticketpackages = ticketpackages;
    }

    ///@brief Adds a ticket package to the ticket packages list
    public void addpackage(TicketPackage ticketPackage) {
        this.ticketpackages.add(ticketPackage);
    }

    ///@brief Removes a ticket package from the ticket packages list
    public void removeTicketPackage(TicketPackage ticketPackage) {
        this.ticketpackages.remove(ticketPackage);
    }

    ///@brief Reads ticket packages from a file and populates the ticket packages list (not implemented)
    public void readTicketPackagesFromFile(String filePath) {
        // Implement file reading logic to populate the packages list
    }

    ///@brief Gets the list of ticket packages for the ticket packages object
    public void setTicketPackages(TicketPackages ticketPackages) {
        this.ticketpackages = ticketPackages.ticketpackages;
    }

    /// @brief Returns a string representation of the list of Ticket Packages
    /// @return A string representation of the list of Ticket Packages
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (TicketPackage ticketPackage : ticketpackages) {
            sb.append(ticketPackage.toString()).append("\n");
        }
        return sb.toString();
    }
}

