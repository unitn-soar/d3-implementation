package d3.soar.MainLists;

import java.util.List;

import d3.soar.PurchaseData.PurchaseLog;

    /// @brief Represents the list of all the Purchase Logs
    /// @see PurchaseLog
public class PurchaseLogs {
    public List<PurchaseLog> purchaseLogs;

    ///@brief Constructs a new purchaselogs object with default values
    public PurchaseLogs() {
    }

    ///@brief Constructs a new purchase logs object with the specified list of purchase logs
    ///@param purchaseLogs A list of PurchaseLogs
    public PurchaseLogs(List<PurchaseLog> purchaseLogs) {
        this.purchaseLogs = purchaseLogs;
    }

    ///@brief Adds a purchase log to the purchase logs list
    public void addPurchaseLog(PurchaseLog purchaseLog) {
        this.purchaseLogs.add(purchaseLog);
    }

    ///@brief Removes a purchase log from the purchase logs list
    public void removePurchaseLog(PurchaseLog purchaseLog) {
        this.purchaseLogs.remove(purchaseLog);
    }

    ///@brief Reads purchase logs from a file and populates the purchase logs list (not implemented)
    public void readPurchaseLogsFromFile(String filePath) {
    }

    ///@brief Gets the list of purchase logs for the purchase logs object
    public void setPurchaseLogs(PurchaseLogs purchaseLogs) {
        this.purchaseLogs = purchaseLogs.purchaseLogs;
    }

    /// @brief Returns a string representation of the list of Purchase Logs
    /// @return A string representation of the list of Purchase Logs
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (PurchaseLog purchaseLog : purchaseLogs) {
            sb.append(purchaseLog.toString()).append("\n");
        }
        return sb.toString();
    }
}
