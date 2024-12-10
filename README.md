# java-versions 1 array
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



# java version 2 vector
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


#java version 3 JDBC

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



# java version 4 GUI


    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.sql.*;
    import java.util.Vector;

    class DatabaseConnectionNew {
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

    class ProductGUI {
    private String name;
    private String category;
    private double price;

    public ProductGUI(String name, String category, double price) {
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

    public class V418008 {
    private static Vector<ProductGUI> supplements = new Vector<>();
    private static Vector<ProductGUI> equipment = new Vector<>();
    private static Vector<ProductGUI> purchasedProducts = new Vector<>();
    private static double totalAmount = 0;

    private JFrame frame;
    private JTextArea displayArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(V418008::new);
    }

    public V418008() {
        createGUI();
        loadProductsFromDatabase();
    }

    private void createGUI() {
        frame = new JFrame("Gym Product Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setFont(new Font("Arial", Font.PLAIN, 16));
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 3, 10, 10));

        JButton supplementsButton = new JButton("Select Supplements");
        JButton equipmentButton = new JButton("Select Equipment");
        JButton addButton = new JButton("Add New Product");
        JButton discountButton = new JButton("Apply Discount");
        JButton cancelButton = new JButton("Cancel a Product");
        JButton exitButton = new JButton("Exit");

        Dimension buttonSize = new Dimension(200, 60);
        supplementsButton.setPreferredSize(buttonSize);
        equipmentButton.setPreferredSize(buttonSize);
        addButton.setPreferredSize(buttonSize);
        discountButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        supplementsButton.addActionListener(this::selectSupplements);
        equipmentButton.addActionListener(this::selectEquipment);
        addButton.addActionListener(this::addNewProduct);
        discountButton.addActionListener(this::applyDiscount);
        cancelButton.addActionListener(this::cancelProduct);
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(supplementsButton);
        buttonPanel.add(equipmentButton);
        buttonPanel.add(addButton);
        buttonPanel.add(discountButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(exitButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void loadProductsFromDatabase() {
        try (Connection conn = DatabaseConnectionNew.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            while (rs.next()) {
                ProductGUI product = new ProductGUI(rs.getString("name"),
                                                    rs.getString("category"),
                                                    rs.getDouble("price"));
                if (product.getCategory().equalsIgnoreCase("Supplement")) {
                    supplements.add(product);
                } else if (product.getCategory().equalsIgnoreCase("Equipment")) {
                    equipment.add(product);
                }
            }
            displayArea.append("Connected to the database successfully!\n");
        } catch (SQLException e) {
            displayArea.append("Failed to connect to the database.\n");
            e.printStackTrace();
        }
    }

    private void selectSupplements(ActionEvent e) {
        selectProducts(supplements);
    }

    private void selectEquipment(ActionEvent e) {
        selectProducts(equipment);
    }

    private void selectProducts(Vector<ProductGUI> products) {
        displayArea.append("\nAvailable Products:\n");
        for (int i = 0; i < products.size(); i++) {
            displayArea.append((i + 1) + ". " + products.get(i) + "\n");
        }
        String input = JOptionPane.showInputDialog(frame, "Choose product numbers to purchase (comma separated):");
        if (input != null && !input.trim().isEmpty()) {
            String[] choices = input.split(",");
            for (String choice : choices) {
                try {
                    int index = Integer.parseInt(choice.trim()) - 1;
                    if (index >= 0 && index < products.size()) {
                        ProductGUI selectedProduct = products.get(index);
                        purchasedProducts.add(selectedProduct);
                        totalAmount += selectedProduct.getPrice();
                        displayArea.append("Purchased: " + selectedProduct + "\n");
                    } else {
                        displayArea.append("Invalid choice: " + choice + "\n");
                    }
                } catch (NumberFormatException ex) {
                    displayArea.append("Invalid input: " + choice + "\n");
                }
            }
        }
    }

    private void addNewProduct(ActionEvent e) {
        String[] options = {"Supplement", "Equipment"};
        int categoryOption = JOptionPane.showOptionDialog(frame, "Select category to add new product:",
                "Category", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (categoryOption >= 0) {
            String category = options[categoryOption];
            String name = JOptionPane.showInputDialog(frame, "Enter product name:");
            String priceInput = JOptionPane.showInputDialog(frame, "Enter product price:");
            try {
                double price = Double.parseDouble(priceInput);
                ProductGUI newProduct = new ProductGUI(name, category, price);
                try (Connection conn = DatabaseConnectionNew.connect();
                     PreparedStatement pstmt = conn.prepareStatement("INSERT INTO products (name, category, price) VALUES (?, ?, ?)")) {

                    pstmt.setString(1, newProduct.getName());
                    pstmt.setString(2, newProduct.getCategory());
                    pstmt.setDouble(3, newProduct.getPrice());
                    pstmt.executeUpdate();
                    displayArea.append("Product added successfully.\n");
                } catch (SQLException ex) {
                    displayArea.append("Failed to add product to the database.\n");
                    ex.printStackTrace();
                }

                if (category.equalsIgnoreCase("Supplement")) {
                    supplements.add(newProduct);
                } else if (category.equalsIgnoreCase("Equipment")) {
                    equipment.add(newProduct);
                }
            } catch (NumberFormatException ex) {
                displayArea.append("Invalid price.\n");
            }
        }
    }

    private void applyDiscount(ActionEvent e) {
        String input = JOptionPane.showInputDialog(frame, "Enter discount percentage:");
        try {
            double discount = Double.parseDouble(input);
            totalAmount -= totalAmount * (discount / 100);
            displayArea.append("Discount applied successfully. Total Amount: ₹" + totalAmount + "\n");
        } catch (NumberFormatException ex) {
            displayArea.append("Invalid discount percentage.\n");
        }
    }

    private void cancelProduct(ActionEvent e) {
        displayArea.append("\nPurchased Products:\n");
        for (int i = 0; i < purchasedProducts.size(); i++) {
            displayArea.append((i + 1) + ". " + purchasedProducts.get(i) + "\n");
        }
        String input = JOptionPane.showInputDialog(frame, "Choose a product number to cancel:");
        try {
            int choice = Integer.parseInt(input) - 1;
            if (choice >= 0 && choice < purchasedProducts.size()) {
                ProductGUI cancelledProduct = purchasedProducts.remove(choice);
                totalAmount -= cancelledProduct.getPrice();
                displayArea.append("Cancelled: " + cancelledProduct + "\n");
            } else {
                displayArea.append("Invalid choice.\n");
            }
        } catch (NumberFormatException ex) {
            displayArea.append("Invalid input.\n");
        }
    }
    }
