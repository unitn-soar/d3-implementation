package d3.soar.PurchaseData;
import java.util.Date;

/* @brief Represents a purchase in the system, containing the purchase date, receipt, ticket, and passenger*/
public class Purchase {
    private Date purchaseDate;
    private String receipt;
    private Ticket ticket;
    private Passenger passenger;

    /* @brief Constructs a new purchase with the specified fields*/
    public Purchase() {
    }
    /* @brief Constructs a new purchase with the specified fields*/
    public Purchase(Date purchaseDate, String receipt, Ticket ticket, Passenger passenger) {    
        this.purchaseDate = purchaseDate;
        this.receipt = receipt;
        this.ticket = ticket;
        this.passenger = passenger;
    }

    /* @brief Gets the purchase date for the purchase*/
    public Date getPurchaseDate() {
        return purchaseDate;
    }

    /* @brief Sets the purchase date for the purchase*/
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /* @brief Gets the receipt for the purchase*/
    public String getReceipt() {
        return receipt;
    }

    /* @brief Files a receipt for the purchase*/
    public void fileReceipt() {
        String receiptContent = "Purchase Date: " + purchaseDate.toString() + "\n" +
                                "Ticket Details: " + ticket.toString() + "\n" +
                                "Passenger Details: " + passenger.toString() + "\n";
        this.receipt = receiptContent;     
    }

    /* @brief Gets the ticket for the purchase*/
    public Ticket getTicket() {
        return ticket;
    }

    /* @brief Sets the ticket for the purchase*/
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    /* @brief Gets the passenger for the purchase*/
    public Passenger getPassenger() {
        return passenger;
    }

    /* @brief Sets the passenger for the purchase*/
    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
    
/* @brief Confirms the purchase*/
    public void confirmPurchase() {
        // This is a placeholder for the actual implementation.
        System.out.println("Purchase confirmed for passenger: " + passenger.getName() + " " + passenger.getSurname());
    }    

    @Override
    /* @brief Returns a string representation of the purchase object*/
    public String toString() {
        return "Purchase{" +
                "\npurchaseDate=" + purchaseDate +
                ",\nreceipt='" + receipt + '\'' +
                ",\nticket=" + ticket +
                ",\npassenger=" + passenger +
                '}';
    }
}
