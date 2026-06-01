package d3.soar.classes.PurchaseData;

import java.util.Date;
import java.util.List;

import d3.soar.classes.UserData.TravelAgency;

public class BulkPurchase {
    private TicketPackage ticketPackage;
    private List<Passenger> passengersList;
    private int discount;
    private Date purchaseDate;
    private String receipt;
    private TravelAgency travelAgency;

    // Default constructor
    public BulkPurchase() {
    }

    // Constructor with all fields
    public BulkPurchase(TicketPackage ticketPackage, List<Passenger> passengersList, int discount, Date purchaseDate, String receipt, TravelAgency travelAgency) {
        this.ticketPackage = ticketPackage;
        this.passengersList = passengersList;
        this.discount = discount;
        this.purchaseDate = purchaseDate;
        this.receipt = receipt;
        this.travelAgency = travelAgency;
    }
    
    // Getter for ticketPackage
    public TicketPackage getTicketPackage() {
        return ticketPackage;
    }

    // Setter for ticketPackage
    public void setTicketPackage(TicketPackage ticketPackage) {
        this.ticketPackage = ticketPackage;
    }

    // Getter for passengersList
    public List<Passenger> getPassengersList() {        
        return passengersList;
    }

    // Setter for passengersList
    public void setPassengersList(List<Passenger> passengersList) {
        this.passengersList = passengersList;
    }   

    // Methods to add and remove passengers from the bulk purchase
    public void addPassenger(Passenger passenger) {
        this.passengersList.add(passenger);
    }

    public void addPassenger(String name, String surname, String email) {
        Passenger passenger = new Passenger(name, surname, email);
        this.passengersList.add(passenger);
    }

    // Method to remove a passenger from the bulk purchase
    public void removePassenger(Passenger passenger) {
        this.passengersList.remove(passenger);
    }

    // Getter for discount
    public int getDiscount() {
        return discount;
    }       

    // Setter for discount
    public void setDiscount(int discount) {       
        this.discount = discount;
    }   

    // Getter for purchaseDate
    public Date getPurchaseDate() {        
        return purchaseDate;
    }   

    // Setter for purchaseDate
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    // Getter for receipt
    public String getReceipt() {
        return receipt;
    }

    // Method to file a receipt for the bulk purchase
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

    // Getter for travelAgency
    public TravelAgency getTravelAgency() {
        return travelAgency;
    }

    // Setter for travelAgency
    public void setTravelAgency(TravelAgency travelAgency) {
        this.travelAgency = travelAgency;
    }

    // Method to calculate the total price of the bulk purchase after applying the discount
    private float calculateTotalPrice() {
        float totalPrice = 0;
        for (Ticket ticket : ticketPackage.getTickets()) {
            totalPrice += ticket.getPrice();
        }
        totalPrice = totalPrice * (1 - (discount / 100.0f));
        return totalPrice;
    }

    // Logic to confirm the bulk purchase, such as processing payment and updating ticket availability
    public void confirmPurchase() {
        // This is a placeholder for the actual implementation
        System.out.println("Bulk purchase confirmed.");
    }

    public void distributeTickets() {
        // This is a placeholder for the actual implementation
        System.out.println("Tickets distributed to passengers.");
    }
}
