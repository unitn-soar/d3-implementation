package d3.soar.PurchaseData;

import java.util.Date;
import java.util.List;

import d3.soar.UserData.TravelAgency;

/// @brief Represents a bulk purchase in the system, containing a ticket package, list of passengers, discount, purchase date, receipt, and travel agency
public class BulkPurchase {
    private TicketPackage ticketPackage;
    private List<Passenger> passengersList;
    private float discount;
    private Date purchaseDate;
    private String receipt;
    private TravelAgency travelAgency;

///@brief Constructs a new bulk purchase with default values
    public BulkPurchase() {
    }

///@brief Constructs a new bulk purchase with the specified fields
/// @param ticketPackage An existing Ticket Package
/// @param passengersList A list of Passenger
/// @param discount Discount percentage on the purchase
/// @param travelAgency The TravelAgency making the BulkPurchase 
    public BulkPurchase(TicketPackage ticketPackage, List<Passenger> passengersList, float discount, Date purchaseDate, TravelAgency travelAgency) {
        this.ticketPackage = ticketPackage;
        this.passengersList = passengersList;
        this.discount = discount;
        this.purchaseDate = purchaseDate;
        this.travelAgency = travelAgency;
    }
    
    ///@brief Gets the ticket package for the bulk purchase
    public TicketPackage getTicketPackage() {
        return ticketPackage;
    }

    ///@brief Sets the ticket package for the bulk purchase
    public void setTicketPackage(TicketPackage ticketPackage) {
        this.ticketPackage = ticketPackage;
    }

    ///@brief Gets the list of passengers for the bulk purchase
    public List<Passenger> getPassengersList() {        
        return passengersList;
    }

    ///@brief Sets the list of passengers for the bulk purchase
    public void setPassengersList(List<Passenger> passengersList) {
        this.passengersList = passengersList;
    }   

    ///@brief Adds a passenger to the bulk purchase
    public void addPassenger(Passenger passenger) {
        this.passengersList.add(passenger);
    }

    ///@brief Adds a passenger to the bulk purchase with the specified details
    public void addPassenger(String name, String surname, String email) {
        Passenger passenger = new Passenger(name, surname, email);
        this.passengersList.add(passenger);
    }

    ///@brief Removes a passenger from the bulk purchase
    public void removePassenger(Passenger passenger) {
        this.passengersList.remove(passenger);
    }

///@brief Gets the discount for the bulk purchase
    public float getDiscount() {
        return discount;
    }       

///@brief Sets the discount for the bulk purchase
    public void setDiscount(float discount) {       
        this.discount = discount;
    }   

    ///@brief Gets the purchase date for the bulk purchase
    public Date getPurchaseDate() {        
        return purchaseDate;
    }   

    ///@brief Sets the purchase date for the bulk purchase
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    ///@brief Gets the receipt for the bulk purchase
    public String getReceipt() {
        return receipt;
    }

    /// @brief Returns a string representation of the bulk purchase object
    /// @return A string representation of the bulk purchase object
    public void fileReceipt() {      
        StringBuilder sb = new StringBuilder();
        sb.append("Bulk Purchase Receipt:\n");
        sb.append("Travel Agency: ").append(travelAgency.getCompanyName()).append("\n");
        sb.append("Purchase Date: ").append(purchaseDate.toString()).append("\n");
        sb.append("Discount: ").append(discount).append("%\n");
        sb.append("Passengers:\n");
        for (Passenger passenger : passengersList) {
            sb.append("- ").append(passenger.getName()).append(" ").append(passenger.getSurname()).append("\n");
        }
        sb.append("Tickets:\n");
        for (Ticket ticket : ticketPackage.getTickets()) {
            sb.append("- Class: ").append(ticket.getSeatClass()).append(", Price: $").append(ticket.getPrice()).append(", Seat: ").append(ticket.getSeat()).append("\n");
        }
        sb.append("Total Price: $").append(calculateTotalPrice()).append("\n");
        this.receipt = sb.toString();
    }

    ///@brief Gets the travel agency for the bulk purchase
    public TravelAgency getTravelAgency() {
        return travelAgency;
    }

///@brief Sets the travel agency for the bulk purchase
    public void setTravelAgency(TravelAgency travelAgency) {
        this.travelAgency = travelAgency;
    }

    ///@brief Calculates the total price of the bulk purchase after applying the discount
    ///@return The total price of the purchase after applying the discount
    private float calculateTotalPrice() {
        float totalPrice = 0;
        for (Ticket ticket : ticketPackage.getTickets()) {
            totalPrice += ticket.getPrice();
        }
        totalPrice = totalPrice * (1 - (discount / 100.0f));
        return totalPrice;
    }

    ///@brief Confirms the bulk purchase, such as processing payment and updating ticket availability (not implemented)
    public void confirmPurchase() {
        // This is a placeholder for the actual implementation
        System.out.println("Bulk purchase confirmed.");
    }

    ///@brief Distributes tickets to the passengers (not implemented)
    public void distributeTickets() {
        // This is a placeholder for the actual implementation
        System.out.println("Tickets distributed to passengers.");
    }

    /// @brief Returns a string representation of the Bulk Purchase object
    /// @return A string representation of the Bulk Purchase object
    @Override
    public String toString() {
        return "BulkPurchase{" +
                "ticketPackage=" + ticketPackage +
                ", passengersList=" + passengersList +
                ", discount=" + discount +
                ", purchaseDate=" + purchaseDate +
                ", receipt='" + receipt + '\'' +
                ", travelAgency=" + travelAgency +
                '}';
    }
}
