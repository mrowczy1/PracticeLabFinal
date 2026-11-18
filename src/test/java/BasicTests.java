import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BasicTests {

    @Test
    void testProductInventoryValue() {
        Product p = new Product(1, "Laptop", 1000, 5);
        assertEquals(5000.0, p.getInventoryValue(), 0.001);
    }

    @Test
    void testReduceStock() {
        Product p = new Product(1, "Mouse", 20, 10);
        p.reduceStock(3);
        assertEquals(7, p.getQuantity());
    }

    @Test
    void testAddProduct() {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Laptop", 1000, 5));
        assertEquals(1, inv.getProductCount());
    }

    @Test
    void testFindProduct() {
        Inventory inv = new Inventory();
        Product p = new Product(1, "Laptop", 1000, 5);
        inv.addProduct(p);
        assertSame(p, inv.findProduct(1));
    }

    @Test
    void testRemoveProduct() {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Laptop", 1000, 5));

        assertTrue(inv.removeProduct(1));
        assertNull(inv.findProduct(1));
    }

    @Test
    void testRemoveMissingProduct() {
        Inventory inv = new Inventory();
        assertFalse(inv.removeProduct(999));
    }

    @Test
    void testInventoryTotal() {
        Inventory inv = new Inventory();

        inv.addProduct(new Product(1, "Laptop", 1000, 2));
        inv.addProduct(new Product(2, "Mouse", 20, 5));

        assertEquals(2100.0, inv.getTotalInventoryValue(), 0.001);
    }

    @Test
    void testLowStock() {
        Inventory inv = new Inventory();

        inv.addProduct(new Product(1, "Mouse", 20, 2));
        inv.addProduct(new Product(2, "Keyboard", 50, 20));

        List<Product> low = inv.getLowStockProducts(5);

        assertEquals(1, low.size());
        assertEquals(1, low.get(0).getId());
    }

    @Test
    void testTransactionTotal() {
        Transaction t = new Transaction(1, 101, 3, 10.0);
        assertEquals(30.0, t.getTotal(), 0.001);
    }

    @Test
    void testSuccessfulSale() {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Mouse", 20, 10));

        TransactionManager manager = new TransactionManager(inv);

        assertTrue(manager.processSale(1, 1, 3));
    }

    @Test
    void testSaleUpdatesInventory() {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Mouse", 20, 10));

        TransactionManager manager = new TransactionManager(inv);
        manager.processSale(1, 1, 3);

        assertEquals(7, inv.findProduct(1).getQuantity());
    }

    @Test
    void testRejectExcessiveSale() {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Mouse", 20, 5));

        TransactionManager manager = new TransactionManager(inv);

        assertFalse(manager.processSale(1, 1, 10));
    }

    @Test
    void testRejectZeroQuantity() {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Mouse", 20, 5));

        TransactionManager manager = new TransactionManager(inv);

        assertFalse(manager.processSale(1, 1, 0));
    }

    @Test
    void testCustomerTotal() {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Mouse", 10, 20));

        TransactionManager manager = new TransactionManager(inv);

        manager.processSale(1, 1, 2);
        manager.processSale(1, 1, 3);

        assertEquals(50.0, manager.getCustomerTotal(1), 0.001);
    }

    @Test
    void testSaveAndLoadInventory(@TempDir Path tempDir) throws Exception {

        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Mouse", 20, 10));

        Path file = tempDir.resolve("inventory.txt");

        InventoryFile.saveInventory(inv, file.toString());

        Inventory loaded = InventoryFile.loadInventory(file.toString());

        assertEquals(1, loaded.getProductCount());
        assertEquals(10, loaded.findProduct(1).getQuantity());
    }

    @Test
    void testReportContainsProductName() {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Gaming Mouse", 50, 5));

        String report = ReportGenerator.generateSummary(inv);

        assertTrue(report.contains("Gaming Mouse"));
    }
}