import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdvancedTests {

    @Test
    void negativeStockMustBeRejected() {
        Product p = new Product(1, "Mouse", 20, 5);

        assertThrows(IllegalArgumentException.class, () -> p.setQuantity(-1));
    }

    @Test
    void duplicateProductMustBeRejected() {
        Inventory inv = new Inventory();

        inv.addProduct(new Product(1, "Mouse", 20, 5));

        assertThrows(
                IllegalArgumentException.class,
                () -> inv.addProduct(new Product(1, "Keyboard", 50, 10)));
    }

    @Test
    void removingProductReturnsTrueOnlyWhenFound() {
        Inventory inv = new Inventory();

        assertFalse(inv.removeProduct(999));

        inv.addProduct(new Product(1, "Mouse", 20, 5));

        assertTrue(inv.removeProduct(1));
    }

    @Test
    void lowStockIncludesBoundaryValue() {
        Inventory inv = new Inventory();

        inv.addProduct(new Product(1, "Mouse", 20, 5));

        assertEquals(1, inv.getLowStockProducts(5).size());
    }

    @Test
    void transactionUsesQuantityTimesPrice() {
        Transaction t = new Transaction(1, 1, 4, 12.50);

        assertEquals(50.0, t.getTotal(), 0.001);
    }

    @Test
    void negativeSaleMustBeRejected() {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Mouse", 20, 10));

        TransactionManager manager = new TransactionManager(inv);

        assertFalse(manager.processSale(1, 1, -2));
    }

    @Test
    void failedSaleMustNotChangeInventory() {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Mouse", 20, 5));

        TransactionManager manager = new TransactionManager(inv);

        assertFalse(manager.processSale(1, 1, 10));
        assertEquals(5, inv.findProduct(1).getQuantity());
    }

    @Test
    void successfulSaleMustReduceStockExactly() {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Mouse", 20, 10));

        TransactionManager manager = new TransactionManager(inv);

        assertTrue(manager.processSale(1, 1, 4));
        assertEquals(6, inv.findProduct(1).getQuantity());
    }

    @Test
    void multipleCustomerTransactionsMustAccumulate() {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Mouse", 10, 100));

        TransactionManager manager = new TransactionManager(inv);

        manager.processSale(1, 1, 2);
        manager.processSale(1, 1, 3);

        assertEquals(50, manager.getCustomerTotal(1), 0.001);
    }

    @Test
    void transactionListMustBeProtected() {
        Inventory inv = new Inventory();
        inv.addProduct(new Product(1, "Mouse", 10, 20));

        TransactionManager manager = new TransactionManager(inv);
        manager.processSale(1, 1, 2);

        List<Transaction> returned = manager.getTransactions();
        returned.clear();

        assertEquals(1, manager.getTransactions().size());
    }

    @Test
    void loaderMustHandleWhitespace(@TempDir Path dir) throws Exception {

        Path file = dir.resolve("inventory.txt");

        Files.writeString(file, "1, Mouse , 20.0 , 5\n");

        Inventory inv = InventoryFile.loadInventory(file.toString());

        assertEquals("Mouse", inv.findProduct(1).getName());
        assertEquals(5, inv.findProduct(1).getQuantity());
    }

    @Test
    void loaderMustSkipMalformedRecords(@TempDir Path dir) throws Exception {

        Path file = dir.resolve("inventory.txt");

        Files.writeString(file,
                "1,Mouse,20.0,5\n" +
                        "INVALID RECORD\n" +
                        "2,Keyboard,50.0,10\n"
        );

        Inventory inv = InventoryFile.loadInventory(file.toString());

        assertEquals(2, inv.getProductCount());
        assertNotNull(inv.findProduct(1));
        assertNotNull(inv.findProduct(2));
    }
}