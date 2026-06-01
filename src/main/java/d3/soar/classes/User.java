package d3.soar.classes;
public class User {
    private String email;
    private String password;
    private PurchaseLog purchaseLog;

    // Default constructor
    public User() {
    }

    // Constructor with parameters
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.purchaseLog = new PurchaseLog();
    }

    // Getter for email
    public String getEmail() {
        return email;
    }

    // Setter for email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for purchaseLog
    public PurchaseLog getPurchaseLog() {
        return purchaseLog;
    }
    
    // Setter for purchaseLog
    public void setPurchaseLog(PurchaseLog purchaseLog) {
        this.purchaseLog = purchaseLog;
    }

    public void addPurchase(Purchase purchase) {
        this.purchaseLog.addPurchase(purchase);
    }

    public void removePurchase(Purchase purchase) {
        this.purchaseLog.removePurchase(purchase);
    }

    public void addBulkPurchase(BulkPurchase bulkPurchase) {
        this.purchaseLog.addBulkPurchase(bulkPurchase);
    }

    public void removeBulkPurchase(BulkPurchase bulkPurchase) {
        this.purchaseLog.removeBulkPurchase(bulkPurchase);
    }
}