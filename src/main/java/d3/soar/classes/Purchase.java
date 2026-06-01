package d3.soar;
import java.util.Date;

public class Purchase {
    private Date purchaseDate;
    private String receipt;
    private Ticket ticket;
    private Passenger passenger;

    // Default constructor
    public Purchase() {
    }
    // Constructor with all fields
    public Purchase(Date purchaseDate, String receipt, Ticket ticket, Passenger passenger) {    
        this.purchaseDate = purchaseDate;
        this.receipt = receipt;
        this.ticket = ticket;
        this.passenger = passenger;
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

    // Method to file a receipt for the purchase
    public void fileReceipt() {
        String receiptContent = "Purchase Date: " + purchaseDate.toString() + "\n" +
                                "Ticket Details: " + ticket.toString() + "\n" +
                                "Passenger Details: " + passenger.toString() + "\n";
        this.receipt = receiptContent;     
    }

    // Getter for ticket
    public Ticket getTicket() {
        return ticket;
    }

    // Setter for ticket
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    // Getter for passenger
    public Passenger getPassenger() {
        return passenger;
    }

    // Setter for passenger
    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
    
    // Logic to confirm the purchase, e.g., process payment, update ticket status, etc.
    public void confirmPurchase() {
        // This is a placeholder for the actual implementation.
        System.out.println("Purchase confirmed for passenger: " + passenger.getName() + " " + passenger.getSurname());
    }    

}
