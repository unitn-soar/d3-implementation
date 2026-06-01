package d3.soar;
import java.util.List;

public class PurchaseLog {
    private List<Purchase> purchases;
    private List<BulkPurchase> bulkPurchases;

    // Default constructor
    public PurchaseLog() {
    }

    // Constructor with all fields
    public PurchaseLog(List<Purchase> purchases, List<BulkPurchase> bulkPurchases) {
        this.purchases = purchases;
        this.bulkPurchases = bulkPurchases;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public List<BulkPurchase> getBulkPurchases() {
        return bulkPurchases;
    }

    public void setBulkPurchases(List<BulkPurchase> bulkPurchases) {
        this.bulkPurchases = bulkPurchases;
    }

    // Methods to add and remove purchases from the purchase log
    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
    }

    public void removePurchase(Purchase purchase) {
        this.purchases.remove(purchase);
    }
    
    // Methods to add and remove bulk purchases from the purchase log
    public void addBulkPurchase(BulkPurchase bulkPurchase) {
        this.bulkPurchases.add(bulkPurchase);
    }

    public void removeBulkPurchase(BulkPurchase bulkPurchase) {
        this.bulkPurchases.remove(bulkPurchase);
    }
    
    public void clearPurchases() {
        this.purchases.clear();
    }

    public void clearBulkPurchases() {
        this.bulkPurchases.clear();
    }
    
}
