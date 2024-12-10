import java.util.Scanner;
import java.util.Vector;

class ProductVec {
    private String name;
    private String category;
    private double price;

    public ProductVec(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public String toString() {
        return name + " - ₹" + price;
    }
}

public class V218008 {
    private static Vector<ProductVec> supplements = new Vector<>();
    private static Vector<ProductVec> equipment = new Vector<>();
    private static Vector<ProductVec> purchasedProducts = new Vector<>();
    private static double totalAmount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Predefined products
        supplements.add(new ProductVec("Whey Protein", "Supplement", 2500));
        supplements.add(new ProductVec("Multivitamins", "Supplement", 500));
        supplements.add(new ProductVec("BCAA", "Supplement", 1500));
        supplements.add(new ProductVec("Creatine", "Supplement", 800));
        supplements.add(new ProductVec("Fish Oil", "Supplement", 1200));

        equipment.add(new ProductVec("Dumbbell", "Equipment", 2000));
        equipment.add(new ProductVec("Treadmill", "Equipment", 50000));
        equipment.add(new ProductVec("Bench Press", "Equipment", 10000));
        equipment.add(new ProductVec("Exercise Bike", "Equipment", 15000));
        equipment.add(new ProductVec("Kettlebell", "Equipment", 3000));

        boolean running = true;
        while (running) {
            System.out.println("\n=== Gym Product Management System ===");
            System.out.println("1. Select Supplements");
            System.out.println("2. Select Equipment");
            System.out.println("3. Add New Product or Supplement");
            System.out.println("4. Apply Discount");
            System.out.println("5. Cancel a Product");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int option = getInputAsInt(scanner);

            switch (option) {
                case 1:
                    selectProducts(supplements, scanner);
                    break;
                case 2:
                    selectProducts(equipment, scanner);
                    break;
                case 3:
                    addNewProduct(scanner);
                    break;
                case 4:
                    applyDiscount(scanner);
                    break;
                case 5:
                    cancelProduct(scanner);
                    break;
                case 6:
                    displayPurchasedProducts();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
        scanner.close();
    }

    private static void selectProducts(Vector<ProductVec> products, Scanner scanner) {
        boolean selecting = true;
        while (selecting) {
            System.out.println("\nAvailable Products:");
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i) != null) {
                    System.out.println((i + 1) + ". " + products.get(i));
                }
            }
            System.out.println((products.size() + 1) + ". Return to main menu");
            System.out.print("Choose a product to purchase: ");
            int choice = getInputAsInt(scanner);

            if (choice > 0 && choice <= products.size() && products.get(choice - 1) != null) {
                ProductVec selectedProduct = products.get(choice - 1);
                purchasedProducts.add(selectedProduct);
                totalAmount += selectedProduct.getPrice();
                System.out.println("Purchased: " + selectedProduct);
            } else if (choice == products.size() + 1) {
                selecting = false;
            } else {
                System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void addNewProduct(Scanner scanner) {
        System.out.println("Select category to add new product:");
        System.out.println("1. Supplement");
        System.out.println("2. Equipment");
        System.out.print("Choose an option: ");
        int categoryOption = getInputAsInt(scanner);
        String category = categoryOption == 1 ? "Supplement" : categoryOption == 2 ? "Equipment" : null;

        if (category != null) {
            System.out.print("Enter product name: ");
            String name = scanner.next();
            System.out.print("Enter product price: ");
            double price = getInputAsDouble(scanner);

            ProductVec newProduct = new ProductVec(name, category, price);
            if (category.equalsIgnoreCase("Supplement")) {
                supplements.add(newProduct);
            } else if (category.equalsIgnoreCase("Equipment")) {
                equipment.add(newProduct);
            }
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Invalid category! Product not added.");
        }
    }

    private static void applyDiscount(Scanner scanner) {
        System.out.print("Enter discount percentage: ");
        double discount = getInputAsDouble(scanner);
        totalAmount -= totalAmount * (discount / 100);
        System.out.println("Discount applied successfully. Total Amount: ₹" + totalAmount);
    }

    private static void cancelProduct(Scanner scanner) {
        System.out.println("\n=== Cancel a Product ===");
        for (int i = 0; i < purchasedProducts.size(); i++) {
            System.out.println((i + 1) + ". " + purchasedProducts.get(i));
        }
        System.out.print("Choose a product to cancel: ");
        int choice = getInputAsInt(scanner);

        if (choice > 0 && choice <= purchasedProducts.size()) {
            ProductVec cancelledProduct = purchasedProducts.get(choice - 1);
            totalAmount -= cancelledProduct.getPrice();
            System.out.println("Cancelled: " + cancelledProduct);
            purchasedProducts.remove(choice - 1);
        } else {
            System.out.println("Invalid choice! Please try again.");
        }
    }

    private static void displayPurchasedProducts() {
        System.out.println("\n=== Final Bill ===");
        for (ProductVec product : purchasedProducts) {
            System.out.println(product);
        }
        System.out.println("Total Amount: ₹" + totalAmount);
    }

    private static int getInputAsInt(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }

    private static double getInputAsDouble(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.next());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }
    }
}
