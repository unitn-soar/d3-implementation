package d3.soar.PurchaseData;
import java.util.List;

/* @brief Represents a purchase log in the system, containing lists of purchases and bulk purchases*/
public class PurchaseLog {
    private List<Purchase> purchases;
    private List<BulkPurchase> bulkPurchases;

/* @brief Constructs a new purchase log*/
    public PurchaseLog() {
    }

/* @brief Constructs a new purchase log with the specified lists of purchases and bulk purchases*/
    public PurchaseLog(List<Purchase> purchases, List<BulkPurchase> bulkPurchases) {
        this.purchases = purchases;
        this.bulkPurchases = bulkPurchases;
    }

    /* @brief Gets the list of purchases for the purchase log*/
    public List<Purchase> getPurchases() {
        return purchases;
    }

    /* @brief Sets the list of purchases for the purchase log*/
    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    /* @brief Gets the list of bulk purchases for the purchase log*/
    public List<BulkPurchase> getBulkPurchases() {
        return bulkPurchases;
    }

    /* @brief Sets the list of bulk purchases for the purchase log*/
    public void setBulkPurchases(List<BulkPurchase> bulkPurchases) {
        this.bulkPurchases = bulkPurchases;
    }

    /* @brief Adds a purchase to the purchase log*/
    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
    }

    /* @brief Removes a purchase from the purchase log*/
    public void removePurchase(Purchase purchase) {
        this.purchases.remove(purchase);
    }
    
    /* @brief Adds a bulk purchase to the purchase log*/
    public void addBulkPurchase(BulkPurchase bulkPurchase) {
        this.bulkPurchases.add(bulkPurchase);
    }

    /* @brief Removes a bulk purchase from the purchase log*/
    public void removeBulkPurchase(BulkPurchase bulkPurchase) {
        this.bulkPurchases.remove(bulkPurchase);
    }
    
    /* @brief Clears all purchases from the purchase log*/
    public void clearPurchases() {
        this.purchases.clear();
    }

    /* @brief Clears all bulk purchases from the purchase log*/
    public void clearBulkPurchases() {
        this.bulkPurchases.clear();
    }
    
    @Override
    /* @brief Returns a string representation of the purchase log object*/
    public String toString() {
        return "PurchaseLog{" +
                "purchases=" + purchases +
                ",\nbulkPurchases=" + bulkPurchases +
                '}';
    }
}
