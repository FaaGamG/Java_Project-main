package page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

import javax.swing.*;

public class app {
    private JFrame fr;
    private JButton btn_admin;
    private boolean isAdmin = false;
    // Product and cart models
    private java.util.List<Product> products = new ArrayList<>();
    private java.util.List<CartItem> cartItems = new ArrayList<>();
    private JPanel productGrid; // will be created in content()
    private DefaultTableModel cartModel;
    private JLabel totalLabel;
    private JButton addProductButton;
    // Category
    private String currentCategory = "All";
    private java.util.List<String> categories = new ArrayList<>();

    public void header() {
        JPanel header = new JPanel();
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        header.setBackground(new Color(112, 128, 144));
        JButton admin = new JButton("Admin");
        JButton user = new JButton("Customer");
        addProductButton = new JButton("Add Product");
        addProductButton.setVisible(false);

        header.add(admin);
        header.add(user);
        header.add(addProductButton);

        fr = new JFrame("ระบบร้านค้า อุปกร์ไฟฟ้า");
        fr.add(header, BorderLayout.NORTH);
        fr.setSize(1000, 1000);
        fr.setLocationRelativeTo(null);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setVisible(true);

        // button actions
        admin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isAdmin = true;
                addProductButton.setVisible(true);
                refreshProductGrid();
            }
        });
        user.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isAdmin = false;
                addProductButton.setVisible(false);
                refreshProductGrid();
            }
        });
        addProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddProductDialog();
            }
        });
    }

    public void footer() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(112, 128, 144));
        JLabel footerLabel = new JLabel("New Technic Shop");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("TimesRoman", Font.BOLD, 32));
        footerPanel.add(footerLabel);
        fr.add(footerPanel, BorderLayout.SOUTH);
    }

    public void content() {
        // 1. Main Panel แบ่งหน้าจอซ้าย-ขวาด้วย GridLayout(1, 2)
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(245, 245, 245));

        // --- [ฝั่งซ้าย: รายการที่ซื้อ] ---
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setOpaque(false);

        JLabel cartTitle = new JLabel("รายการที่ซื้อ", JLabel.LEFT);
        cartTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        leftPanel.add(cartTitle, BorderLayout.NORTH);

        String[] columns = { "Product Name", "Unit price", "Quantity", "Total" };
        cartModel = new DefaultTableModel(null, columns);
        JTable cartTable = new JTable(cartModel);
        cartTable.setRowHeight(30);
        JScrollPane spCart = new JScrollPane(cartTable);
        leftPanel.add(spCart, BorderLayout.CENTER);

        // total area with checkout button
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setOpaque(false);
        JPanel totalLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabelPanel.setOpaque(false);
        totalLabel = new JLabel("Total: 0฿");
        totalLabelPanel.add(totalLabel);
        totalPanel.add(totalLabelPanel, BorderLayout.CENTER);

        JButton checkoutBtn = new JButton("📄 Check Bill");
        checkoutBtn.setFont(checkoutBtn.getFont().deriveFont(Font.BOLD, 18f));
        checkoutBtn.setBackground(new Color(40, 167, 69));
        checkoutBtn.setForeground(Color.WHITE);

        checkoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCheckoutSummary();
            }
        });

        JPanel checkoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        checkoutPanel.setOpaque(false);
        checkoutPanel.add(checkoutBtn);
        JButton cancelBtn = new JButton("🚫 Clear Order");
        cancelBtn.setFont(checkoutBtn.getFont().deriveFont(Font.BOLD, 18f));
        cancelBtn.setBackground(new Color(255, 0, 0));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cartItems.clear();
                updateCartTableAndTotal();
            }
        });
        checkoutPanel.add(cancelBtn);
        totalPanel.add(checkoutPanel, BorderLayout.SOUTH);

        leftPanel.add(totalPanel, BorderLayout.SOUTH);

        // --- [ฝั่งขวา: รายการสินค้าแบบ Card พร้อมหมวดหมู่] ---
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setOpaque(false);

        // Category buttons
        JLabel categoryTitle = new JLabel("Categories:", JLabel.LEFT);
        categoryTitle.setFont(new Font("Tahoma", Font.BOLD, 14));

        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        categoryPanel.setOpaque(false);
        categoryPanel.add(categoryTitle);

        // initialize products and categories
        initProducts();

        for (String cat : categories) {
            JButton catBtn = new JButton(cat);
            catBtn.setFocusPainted(false);
            catBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    currentCategory = cat;
                    refreshProductGrid();
                }
            });
            categoryPanel.add(catBtn);
        }
        rightPanel.add(categoryPanel, BorderLayout.NORTH);

        // Product grid
        productGrid = new JPanel(new GridLayout(0, 2, 15, 15));
        productGrid.setOpaque(false);

        refreshProductGrid();

        JScrollPane spProduct = new JScrollPane(productGrid);
        spProduct.setBorder(null);
        spProduct.setOpaque(false);
        spProduct.getViewport().setOpaque(false);

        rightPanel.add(spProduct, BorderLayout.CENTER);

        // นำใส่ Main Panel
        contentPanel.add(leftPanel); // ซ้าย
        contentPanel.add(rightPanel); // ขวา

        fr.add(contentPanel, BorderLayout.CENTER);
    }

    // Product card builder using Product model
    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // image
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(p.imgPath));
            Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img), JLabel.CENTER);
            card.add(imgLabel, BorderLayout.NORTH);
        } catch (Exception e) {
            card.add(new JLabel("No Image", JLabel.CENTER), BorderLayout.NORTH);
        }

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);
        JLabel lblName = new JLabel(p.name, JLabel.CENTER);
        lblName.setFont(new Font("Tahoma", Font.BOLD, 18));
        JLabel lblPrice = new JLabel(String.format("%.0f฿", p.price), JLabel.CENTER);
        lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblPrice.setForeground(Color.GRAY);
        infoPanel.add(lblName);
        infoPanel.add(lblPrice);
        card.add(infoPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);

        // Only show Add-to-cart when not in admin mode
        if (!isAdmin) {
            JButton btnAdd = new JButton("🛒 Add to cart");
            btnAdd.setBackground(new Color(13, 110, 253));
            btnAdd.setForeground(Color.WHITE);
            btnAdd.setFocusPainted(false);
            btnAdd.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            btnAdd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addToCart(p);
                }
            });
            bottom.add(btnAdd);
        }

        // Admin can remove products and Edit Product
        if (isAdmin) {
            JButton btnRemove = new JButton("🗑️ Remove");
            btnRemove.setFont(btnRemove.getFont().deriveFont(Font.BOLD, 18f));
            btnRemove.setBackground(new Color(220, 20, 60));
            btnRemove.setForeground(Color.WHITE);
            JButton btnEdit = new JButton("🖋️ Edit");
            btnEdit.setBackground(new Color(13, 110, 253));
            btnEdit.setForeground(Color.WHITE);
            btnEdit.setFont(btnEdit.getFont().deriveFont(Font.BOLD, 18f));
            btnRemove.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    products.remove(p);
                    refreshProductGrid();
                }
            });
            btnEdit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JTextField nameF = new JTextField(p.name);
                    JTextField priceF = new JTextField(String.format("%.0f", p.price));
                    JTextField imgF = new JTextField(p.imgPath);
                    JComboBox<String> catCombo = new JComboBox<>(
                            categories.stream().filter(c -> !c.equals("All")).toArray(String[]::new));
                    catCombo.setSelectedItem(p.category);
                    Object[] fields = { "Name:", nameF, "Price:", priceF, "Image path (resource):", imgF, "Category:",
                            catCombo };
                    int option = JOptionPane.showConfirmDialog(fr, fields, "Edit Product",
                            JOptionPane.OK_CANCEL_OPTION);
                    if (option == JOptionPane.OK_OPTION) {
                        try {
                            String n = nameF.getText().trim();
                            double pr = Double.parseDouble(priceF.getText().trim());
                            String img = imgF.getText().trim();
                            if (!img.startsWith("/"))
                                img = "/" + img;
                            String cat = (String) catCombo.getSelectedItem();
                            p.name = n;
                            p.price = pr;
                            p.imgPath = img;
                            p.category = cat;
                            refreshProductGrid();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(fr, "Invalid input: " + ex.getMessage());
                        }
                    }
                }
            });
            bottom.add(btnRemove);
            bottom.add(btnEdit);
        }

        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }

    // Simple product model
    private static class Product {
        String name;
        double price;
        String imgPath;
        String category;

        Product(String n, double p, String i, String c) {
            name = n;
            price = p;
            imgPath = i;
            category = c;
        }
    }

    // Cart item
    private static class CartItem {
        Product product;
        int qty;

        CartItem(Product product, int qty) {
            this.product = product;
            this.qty = qty;
        }
    }

    private void initProducts() {
        products.clear();
        categories.clear();
        categories.add("All");
        categories.add("Microcontroller");
        categories.add("Electrical");
        categories.add("Connectors");
        categories.add("Tools");

        // Microcontroller
        products.add(new Product("NodeMCU", 900, "/Image/NODE.jpg", "Microcontroller"));
        products.add(new Product("Arduino", 800, "/Image/UNO.jpg", "Microcontroller"));
        products.add(new Product("STM32", 1200, "/Image/STM32.jpg", "Microcontroller"));
        products.add(new Product("Relay", 500, "/Image/relay.jpg", "Microcontroller"));
        products.add(new Product("LCD", 1000, "/Image/LCD.jpg", "Microcontroller"));
        products.add(new Product("Servo", 1500, "/Image/Servo.jpg", "Microcontroller"));
        products.add(new Product("Ultrasonic", 1200, "/Image/Ultrasonic.jpg", "Microcontroller"));

        // Electrical
        products.add(new Product("Power Supply", 4000, "/Image/PowerSupply.jpg", "Electrical"));
        products.add(new Product("Resistor 1K", 10, "/Image/resistor.jpg", "Electrical"));
        products.add(new Product("Capacitor", 15, "/Image/capasistor.jpg", "Electrical"));

        // Connectors
        products.add(new Product("Jumper Male", 100, "/Image/JUB_P.jpg", "Connectors"));
        products.add(new Product("Jumper Female", 100, "/Image/JUM_M.jpg", "Connectors"));
        products.add(new Product("USB Cable", 500, "/Image/USB.jpg", "Connectors"));

        // Tools
        products.add(new Product("Soldering Iron", 250, "/Image/BUG.jpg", "Tools"));
        products.add(new Product("Multimeter", 3000, "/Image/Multimeter.jpg", "Tools"));
        products.add(new Product("Pliers", 300, "/Image/Pliers.jpg", "Tools"));

    }

    private void refreshProductGrid() {
        productGrid.removeAll();
        for (Product p : products) {
            if (currentCategory.equals("All") || p.category.equals(currentCategory)) {
                productGrid.add(createProductCard(p));
            }
        }
        productGrid.revalidate();
        productGrid.repaint();
    }

    private void addToCart(Product p) {
        // find existing
        for (CartItem ci : cartItems) {
            if (ci.product == p) {
                ci.qty += 1;
                updateCartTableAndTotal();
                return;
            }
        }
        cartItems.add(new CartItem(p, 1));
        updateCartTableAndTotal();
    }

    private void updateCartTableAndTotal() {
        // clear model and repopulate
        cartModel.setRowCount(0);
        double total = 0;
        for (CartItem ci : cartItems) {
            double line = ci.product.price * ci.qty;
            cartModel.addRow(new Object[] { ci.product.name, String.format("%.0f฿", ci.product.price), ci.qty,
                    String.format("%.0f฿", line) });
            total += line;
        }
        totalLabel.setText(String.format("Tatal: %.0f฿", total));
    }

    private void showAddProductDialog() {
        JTextField nameF = new JTextField();
        JTextField priceF = new JTextField();
        JTextField imgF = new JTextField();
        JComboBox<String> catCombo = new JComboBox<>(
                categories.stream().filter(c -> !c.equals("All")).toArray(String[]::new));
        Object[] fields = { "Name:", nameF, "Price:", priceF, "Image path (resource):", imgF, "Category:", catCombo };
        int option = JOptionPane.showConfirmDialog(fr, fields, "Add Product", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String n = nameF.getText().trim();
                double p = Double.parseDouble(priceF.getText().trim());
                String img = imgF.getText().trim();
                if (!img.startsWith("/"))
                    img = "/" + img;
                String cat = (String) catCombo.getSelectedItem();
                products.add(new Product(n, p, img, cat));
                refreshProductGrid();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(fr, "Invalid input: " + ex.getMessage());
            }
        }
    }

    private void showCheckoutSummary() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(fr, "Your cart is empty!");
            return;
        }

        JDialog summaryDialog = new JDialog(fr, "Order Summary", true);
        summaryDialog.setLayout(new BorderLayout(10, 10));
        summaryDialog.setSize(600, 500);
        summaryDialog.setLocationRelativeTo(fr);

        // Title
        JLabel titleLabel = new JLabel(" Order Summary", JLabel.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        summaryDialog.add(titleLabel, BorderLayout.NORTH);

        // Summary table
        String[] summaryColumns = { "Product Name", "Unit Price", "Quantity", "Subtotal" };
        DefaultTableModel summaryModel = new DefaultTableModel(null, summaryColumns) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        double grandTotal = 0;
        for (CartItem ci : cartItems) {
            double lineTotal = ci.product.price * ci.qty;
            summaryModel.addRow(new Object[] {
                    ci.product.name,
                    String.format("%.0f฿", ci.product.price),
                    ci.qty,
                    String.format("%.0f฿", lineTotal)
            });
            grandTotal += lineTotal;
        }

        JTable summaryTable = new JTable(summaryModel);
        summaryTable.setRowHeight(30);
        summaryTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        JScrollPane summaryScroll = new JScrollPane(summaryTable);

        // Customer info panel (ชื่อ, ที่อยู่, เบอร์โทร)

        final JTextField nameField = new JTextField();
        final JTextField lastNameField = new JTextField();
        final JTextField addrField = new JTextField();
        final JTextField phoneField = new JTextField();
        Font font = new Font("Tahoma", Font.PLAIN, 16);
        nameField.setFont(font);
        lastNameField.setFont(font);
        addrField.setFont(font);
        phoneField.setFont(font);
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        infoPanel.setBorder(BorderFactory.createTitledBorder("Customer Info"));
        infoPanel.add(new JLabel("First Name : "));
        infoPanel.add(nameField);
        infoPanel.add(new JLabel("Last Name : "));
        infoPanel.add(lastNameField);
        infoPanel.add(new JLabel("Address : "));
        infoPanel.add(addrField);
        infoPanel.add(new JLabel("Tele Phone Number : "));
        infoPanel.add(phoneField);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(summaryScroll, BorderLayout.CENTER);
        centerPanel.add(infoPanel, BorderLayout.SOUTH);
        summaryDialog.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel with total and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel totalAreaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalLabel2 = new JLabel(String.format("TOTAL: %.0f฿", grandTotal));
        totalLabel2.setFont(new Font("Tahoma", Font.BOLD, 16));
        totalLabel2.setForeground(new Color(13, 110, 253));
        totalAreaPanel.add(totalLabel2);
        bottomPanel.add(totalAreaPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton confirmBtn = new JButton("✓ Confirm Order");

        // confirmBtn.getFont(): ไปดึงค่าฟอนต์มาตรฐานที่ปุ่มนั้นมีอยู่เดิมมาก่อน
        // .deriveFont(...): เป็นคำสั่ง "สร้างฟอนต์ใหม่โดยดัดแปลงจากฟอนต์เดิม"

        confirmBtn.setFont(confirmBtn.getFont().deriveFont(Font.BOLD, 18f));
        confirmBtn.setBackground(new Color(40, 167, 69));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFocusPainted(false);
        final double finalGrandTotal = grandTotal;
        confirmBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String addr = addrField.getText().trim();
                String phone = phoneField.getText().trim();

                if (name.isEmpty() || addr.isEmpty() || phone.isEmpty() || lastName.isEmpty()) {
                    JOptionPane.showMessageDialog(summaryDialog, "Please fill in all customer information.",
                            "ข้อมูลไม่ครบ", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(summaryDialog,
                        String.format(
                                "Order confirmed!\nName: %s\nLast Name: %s\nAddress: %s\nPhone: %s\nTotal: %.0f฿\nThank you!",
                                name, lastName, addr, phone, finalGrandTotal),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                cartItems.clear();
                updateCartTableAndTotal();
                summaryDialog.dispose();
            }
        });
        buttonPanel.add(confirmBtn);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        summaryDialog.add(bottomPanel, BorderLayout.SOUTH);
        summaryDialog.setVisible(true);
    }

    public static void main(String[] args) {
        app app = new app();
        app.header();
        app.footer();
        app.content();
    }
}
