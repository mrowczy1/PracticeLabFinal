import java.io.*;

public class InventoryFile {

    public static void saveInventory(Inventory inventory, String filename) throws IOException {

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {

            for (Product product : inventory.getAllProducts()) {
                writer.println(product.getId() + ","
                        + product.getName() + ","
                        + product.getPrice() + ","
                        + product.getQuantity()
                );
            }
        }
    }

    public static Inventory loadInventory(String filename) throws IOException {

        Inventory inventory = new Inventory();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] fields = line.split(",");

                int id = Integer.parseInt(fields[0]);
                String name = fields[1];
                double price = Double.parseDouble(fields[2]);
                int quantity = Integer.parseInt(fields[3]);

                Product product = new Product(id, name, price, quantity);

                inventory.addProduct(product);
            }
        }

        return inventory;
    }
}