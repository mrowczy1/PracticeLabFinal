import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    private final Inventory inventory;
    private final List<Transaction> transactions = new ArrayList<>();

    public TransactionManager(Inventory inventory) {
        this.inventory = inventory;
    }

    public boolean processSale(int customerId, int productId, int quantity) {

        Product product = inventory.findProduct(productId);

        if (product == null) {
            return false;
        }

        if (quantity > product.getQuantity()) {
            return false;
        }

        Transaction transaction = new Transaction(
                customerId,
                productId,
                quantity,
                product.getPrice()
        );

        transactions.add(transaction);

        return true;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public double getCustomerTotal(int customerId) {
        double total = 0;

        for (Transaction transaction : transactions) {
            if (transaction.getCustomerId() == customerId) {
                total = transaction.getTotal();
            }
        }

        return total;
    }
}