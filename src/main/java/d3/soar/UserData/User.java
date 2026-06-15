package d3.soar.UserData;

import d3.soar.PurchaseData.BulkPurchase;
import d3.soar.PurchaseData.Purchase;
import d3.soar.PurchaseData.PurchaseLog;

///@brief Represents a user in the system, containing their email, password, and purchase log
public class User {
    private String email;
    private String password;
    private PurchaseLog purchaseLog;

///@brief Constructs a new user
    public User() {
    }

///@brief Constructs a new user with the specified email and password
/// @param email An email
/// @param password A password
    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.purchaseLog = new PurchaseLog();
    }

///@brief Gets the email for the user
    public String getEmail() {
        return email;
    }

///@brief Sets the email for the user
    public void setEmail(String email) {
        this.email = email;
    }

///@brief Gets the password for the user
    public String getPassword() {
        return password;
    }

    ///@brief Sets the password for the user
  
    public void setPassword(String password) {
        this.password = password;
    }

///@brief Gets the purchase log for the user
    public PurchaseLog getPurchaseLog() {
        return purchaseLog;
    }
    
///@brief Sets the purchase log for the user
    public void setPurchaseLog(PurchaseLog purchaseLog) {
        this.purchaseLog = purchaseLog;
    }

    ///@brief Adds a purchase to the user's purchase log
    public void addPurchase(Purchase purchase) {
        this.purchaseLog.addPurchase(purchase);
    }

    ///@brief Removes a purchase from the user's purchase log
    public void removePurchase(Purchase purchase) {
        this.purchaseLog.removePurchase(purchase);
    }

    ///@brief Adds a bulk purchase to the user's purchase log
    public void addBulkPurchase(BulkPurchase bulkPurchase) {
        this.purchaseLog.addBulkPurchase(bulkPurchase);
    }

    ///@brief Removes a bulk purchase from the user's purchase log
    public void removeBulkPurchase(BulkPurchase bulkPurchase) {
        this.purchaseLog.removeBulkPurchase(bulkPurchase);
    }


    /// @brief Returns a string representation of the User object
    /// @return A string representation of the User object
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", purchaseLog=" + purchaseLog +
                '}';
    }
}