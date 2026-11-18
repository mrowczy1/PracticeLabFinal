public class ReportGenerator {

    public static String generateSummary(Inventory inventory) {

        String report = "INVENTORY REPORT\n";
        report += "----------------\n";

        for (Product product : inventory.getAllProducts()) {
            report += product.getName()
                    + " | Quantity: "
                    + product.getQuantity()
                    + " | Value: "
                    + product.getInventoryValue()
                    + "\n";
        }

        report += "Total Inventory Value: "
                + inventory.getTotalInventoryValue();

        return report;
    }
}