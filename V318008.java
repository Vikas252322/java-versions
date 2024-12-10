import java.sql.*;
import java.util.Scanner;
import java.util.Vector;

// Database Connection Class
class DBConnectionNew {
    private static final String URL = "jdbc:mysql://localhost:3306/gym_management";
    private static final String USER = "vikas";  // Your MySQL username
    private static final String PASSWORD = "vikas252322";  // Your MySQL password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load MySQL driver.");
            e.printStackTrace();
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// Product Class
class GymProductNew {
    private String name;
    private String category;
    private double price;

    public GymProductNew(String name, String category, double price) {
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

// Service Class
class ProductService {
    private Vector<GymProductNew> supplements = new Vector<>();
    private Vector<GymProductNew> equipment = new Vector<>();

    public Vector<GymProductNew> getSupplements() {
        return supplements;
    }

    public Vector<GymProductNew> getEquipment() {
        return equipment;
    }

    public void loadProductsFromDatabase() throws SQLException {
        try (Connection conn = DBConnectionNew.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            while (rs.next()) {
                GymProductNew product = new GymProductNew(rs.getString("name"),
                                                          rs.getString("category"),
                                                          rs.getDouble("price"));
                if (product.getCategory().equalsIgnoreCase("Supplement")) {
                    supplements.add(product);
                } else if (product.getCategory().equalsIgnoreCase("Equipment")) {
                    equipment.add(product);
                }
            }
        }
    }

    public void addNewProduct(GymProductNew newProduct) throws SQLException {
        try (Connection conn = DBConnectionNew.connect();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO products (name, category, price) VALUES (?, ?, ?)")) {

            pstmt.setString(1, newProduct.getName());
            pstmt.setString(2, newProduct.getCategory());
            pstmt.setDouble(3, newProduct.getPrice());
            pstmt.executeUpdate();
            if (newProduct.getCategory().equalsIgnoreCase("Supplement")) {
                supplements.add(newProduct);
            } else if (newProduct.getCategory().equalsIgnoreCase("Equipment")) {
                equipment.add(newProduct);
            }
        }
    }
}

// Main Class
public class V318008 {
    private static ProductService productService = new ProductService();
    private static Vector<GymProductNew> purchasedProducts = new Vector<>();
    private static double totalAmount = 0;

    public static void main(String[] args) {
        try {
            productService.loadProductsFromDatabase();
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(System.in);
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
                    selectProducts(productService.getSupplements(), scanner);
                    break;
                case 2:
                    selectProducts(productService.getEquipment(), scanner);
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

    private static void selectProducts(Vector<GymProductNew> products, Scanner scanner) {
        boolean selecting = true;
        while (selecting) {
            System.out.println("\nAvailable Products:");
            for (int i = 0; i < products.size(); i++) {
                System.out.println((i + 1) + ". " + products.get(i));
            }
            System.out.println((products.size() + 1) + ". Return to main menu");
            System.out.print("Choose a product to purchase: ");
            int choice = getInputAsInt(scanner);

            if (choice > 0 && choice <= products.size()) {
                GymProductNew selectedProduct = products.get(choice - 1);
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

            GymProductNew newProduct = new GymProductNew(name, category, price);
            try {
                productService.addNewProduct(newProduct);
                System.out.println("Product added successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
            GymProductNew cancelledProduct = purchasedProducts.remove(choice - 1);
            totalAmount -= cancelledProduct.getPrice();
            System.out.println("Cancelled: " + cancelledProduct);
        } else {
            System.out.println("Invalid choice! Please try again.");
        }
    }

    private static void displayPurchasedProducts() {
        System.out.println("\n=== Final Bill ===");
        for (GymProductNew product : purchasedProducts) {
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