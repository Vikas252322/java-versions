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
