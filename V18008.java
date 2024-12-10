import java.util.Scanner;

class GymProduct {
    private String name;
    private String category;
    private double price;

    public GymProduct(String name, String category, double price) {
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

public class V18008 {
    private static GymProduct[] supplements = new GymProduct[5];
    private static GymProduct[] equipment = new GymProduct[5];
    private static GymProduct[] purchasedProducts = new GymProduct[100];
    private static int supplementCount = 5;
    private static int equipmentCount = 5;
    private static int purchasedCount = 0;
    private static double totalAmount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Predefined products
        supplements[0] = new GymProduct("Whey Protein", "Supplement", 2500);
        supplements[1] = new GymProduct("Multivitamins", "Supplement", 500);
        supplements[2] = new GymProduct("BCAA", "Supplement", 1500);
        supplements[3] = new GymProduct("Creatine", "Supplement", 800);
        supplements[4] = new GymProduct("Fish Oil", "Supplement", 1200);

        equipment[0] = new GymProduct("Dumbbell", "Equipment", 2000);
        equipment[1] = new GymProduct("Treadmill", "Equipment", 50000);
        equipment[2] = new GymProduct("Bench Press", "Equipment", 10000);
        equipment[3] = new GymProduct("Exercise Bike", "Equipment", 15000);
        equipment[4] = new GymProduct("Kettlebell", "Equipment", 3000);

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
                    selectProducts(supplements, supplementCount, scanner);
                    break;
                case 2:
                    selectProducts(equipment, equipmentCount, scanner);
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

    private static void selectProducts(GymProduct[] products, int productCount, Scanner scanner) {
        boolean selecting = true;
        while (selecting) {
            System.out.println("\nAvailable Products:");
            for (int i = 0; i < productCount; i++) {
                if (products[i] != null) {
                    System.out.println((i + 1) + ". " + products[i]);
                }
            }
            System.out.println((productCount + 1) + ". Return to main menu");
            System.out.print("Choose a product to purchase: ");
            int choice = getInputAsInt(scanner);

            if (choice > 0 && choice <= productCount && products[choice - 1] != null) {
                GymProduct selectedProduct = products[choice - 1];
                purchasedProducts[purchasedCount++] = selectedProduct;
                totalAmount += selectedProduct.getPrice();
                System.out.println("Purchased: " + selectedProduct);
            } else if (choice == productCount + 1) {
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

            GymProduct newProduct = new GymProduct(name, category, price);
            if (category.equalsIgnoreCase("Supplement")) {
                if (supplementCount >= supplements.length) {
                    GymProduct[] newArray = new GymProduct[supplements.length + 5];
                    System.arraycopy(supplements, 0, newArray, 0, supplements.length);
                    supplements = newArray;
                }
                supplements[supplementCount++] = newProduct;
            } else if (category.equalsIgnoreCase("Equipment")) {
                if (equipmentCount >= equipment.length) {
                    GymProduct[] newArray = new GymProduct[equipment.length + 5];
                    System.arraycopy(equipment, 0, newArray, 0, equipment.length);
                    equipment = newArray;
                }
                equipment[equipmentCount++] = newProduct;
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
        for (int i = 0; i < purchasedCount; i++) {
            System.out.println((i + 1) + ". " + purchasedProducts[i]);
        }
        System.out.print("Choose a product to cancel: ");
        int choice = getInputAsInt(scanner);

        if (choice > 0 && choice <= purchasedCount) {
            GymProduct cancelledProduct = purchasedProducts[choice - 1];
            totalAmount -= cancelledProduct.getPrice();
            System.out.println("Cancelled: " + cancelledProduct);

            // Shift the remaining products to fill the gap
            for (int i = choice - 1; i < purchasedCount - 1; i++) {
                purchasedProducts[i] = purchasedProducts[i + 1];
            }
            purchasedCount--;
        } else {
            System.out.println("Invalid choice! Please try again.");
        }
    }

    private static void displayPurchasedProducts() {
        System.out.println("\n=== Final Bill ===");
        for (int i = 0; i < purchasedCount; i++) {
            System.out.println(purchasedProducts[i]);
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
