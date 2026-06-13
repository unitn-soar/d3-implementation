package d3.soar.MainLists;

import java.util.List;

import d3.soar.PurchaseData.PurchaseLog;

/* @brief Represents a list of purchase logs in the system, containing methods to add, remove, and read purchase logs from a file*/
public class PurchaseLogs {
    public List<PurchaseLog> purchaseLogs;
    // Default constructor
    public PurchaseLogs() {
    }

    /* @brief Constructs a new purchase logs object with the specified list of purchase logs*/
    public PurchaseLogs(List<PurchaseLog> purchaseLogs) {
        this.purchaseLogs = purchaseLogs;
    }

    /* @brief Adds a purchase log to the purchase logs list*/
    public void addPurchaseLog(PurchaseLog purchaseLog) {
        this.purchaseLogs.add(purchaseLog);
    }

    /* @brief Removes a purchase log from the purchase logs list*/
    public void removePurchaseLog(PurchaseLog purchaseLog) {
        this.purchaseLogs.remove(purchaseLog);
    }

    /* @brief Reads purchase logs from a file and populates the purchase logs list*/
    public void readPurchaseLogsFromFile(String filePath) {
        // Implement file reading logic to populate the purchaseLogs list
    }

    /* @brief Gets the list of purchase logs for the purchase logs object*/
    public void setPurchaseLogs(PurchaseLogs purchaseLogs) {
        this.purchaseLogs = purchaseLogs.purchaseLogs;
    }
}
