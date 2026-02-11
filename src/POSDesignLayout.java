import javax.swing.*;
import java.awt.*;
public class POSDesignLayout extends JFrame {

    public POSDesignLayout() {
        setTitle("ระบบร้านค้า - ตัวอย่าง Layout");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. ใช้ BorderLayout เป็นโครงสร้างหลักของหน้าต่าง (JFrame)
        setLayout(new BorderLayout(10, 10));

        // --- ส่วนบน (Header) ใช้ FlowLayout ---
        // ปุ่มจะเรียงต่อกันจากซ้ายไปขวา
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        headerPanel.setBackground(new Color(52, 73, 94));
        
        JButton btnBack = new JButton("หน้าหลัก");
        JButton btnUser = new JButton("พนักงาน: สมชาย");
        // JButton btnReport = new JButton("รายงานการขาย");
        
        headerPanel.add(btnBack);
        headerPanel.add(btnUser);
        // headerPanel.add(btnReport);
        add(headerPanel, BorderLayout.NORTH);

        // --- ส่วนกลาง (Main Area) แบ่งเป็น ซ้าย-ขวา ---
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ฝั่งซ้าย: รายการสั่งซื้อ (สมมติใช้ BorderLayout ภายในอีกที)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("รายการในตะกร้า"));
        leftPanel.add(new JTextArea("1. กาแฟเย็น... 50.-\n2. เค้กส้ม... 65.-"), BorderLayout.CENTER);
        
        JButton btnPay = new JButton("ชำระเงิน");
        btnPay.setFont(new Font("Tahoma", Font.BOLD, 20));
        btnPay.setBackground(new Color(46, 204, 113));
        leftPanel.add(btnPay, BorderLayout.SOUTH);

        // ฝั่งขวา: รายการสินค้า ใช้ GridLayout
        // แบ่งเป็นช่องตาราง 3 คอลัมน์ (0 หมายถึงเพิ่มแถวได้ไม่จำกัด)
        JPanel productGrid = new JPanel(new GridLayout(0, 3, 10, 10));
        productGrid.setBorder(BorderFactory.createTitledBorder("เลือกสินค้า"));

        String[] menu = {"อเมริกาโน่", "ลาเต้", "ชาเขียว", "โกโก้", "น้ำเปล่า", "คุกกี้"};
        for (String item : menu) {
            JButton itemBtn = new JButton(item);
            itemBtn.setPreferredSize(new Dimension(80, 80));
            itemBtn.setBackground(Color.WHITE);
            productGrid.add(itemBtn);
        }

        // นำแผงสินค้าใส่ ScrollPane เผื่อกรณีสินค้าเยอะจนล้น
        JScrollPane scrollPane = new JScrollPane(productGrid);
        
        // ประกอบร่าง
        mainPanel.add(leftPanel);
        mainPanel.add(scrollPane);
        add(mainPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new POSDesignLayout().setVisible(true);
        });
    }
}