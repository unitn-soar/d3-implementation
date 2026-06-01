package d3.soar;

import java.util.List;

public class PurchaseLogs {
    public List<PurchaseLog> purchaseLogs;

    // Default constructor
    public PurchaseLogs() {
    }

    // Constructor with all fields
    public PurchaseLogs(List<PurchaseLog> purchaseLogs) {
        this.purchaseLogs = purchaseLogs;
    }

    public void addPurchaseLog(PurchaseLog purchaseLog) {
        this.purchaseLogs.add(purchaseLog);
    }

    public void removePurchaseLog(PurchaseLog purchaseLog) {
        this.purchaseLogs.remove(purchaseLog);
    }

    public void readPurchaseLogsFromFile(String filePath) {
        // Implement file reading logic to populate the purchaseLogs list
    }
}

