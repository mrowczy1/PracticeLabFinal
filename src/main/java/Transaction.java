public class Transaction {

    private int customerId;
    private int productId;
    private int quantity;
    private double total;

    public Transaction(int customerId, int productId,
                       int quantity, double price) {

        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
        this.total = price + quantity;
    }

    public int getCustomerId() { return customerId; }
    public int getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public double getTotal() { return total; }
}