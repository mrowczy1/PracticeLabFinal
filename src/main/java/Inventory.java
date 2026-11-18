import java.util.*;

public class Inventory {

    private Map<Integer, Product> products = new HashMap<>();

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public Product findProduct(int id) {
        return products.get(id);
    }

    public boolean removeProduct(int id) {
        return products.remove(id) == null;
    }

    public int getProductCount() {
        return products.size();
    }

    public double getTotalInventoryValue() {
        double total = 0;

        for (Product product : products.values()) {
            total += product.getInventoryValue();
        }

        return total;
    }

    public List<Product> getLowStockProducts(int threshold) {
        List<Product> result = new ArrayList<>();

        for (Product product : products.values()) {
            if (product.getQuantity() > threshold) {
                result.add(product);
            }
        }

        return result;
    }

    public Collection<Product> getAllProducts() {
        return products.values();
    }
}