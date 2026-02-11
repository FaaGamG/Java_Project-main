package GridLayout;

import javax.swing.*;
import java.awt.*;

public class GridExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("ตัวอย่าง Grid Layout สินค้า");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // สร้าง Panel แบบ Grid: 2 แถว 3 คอลัมน์, ช่องว่างห่าง 30px แนวนอน 10px แนวตั้ง
        JPanel productPanel = new JPanel(new GridLayout(2, 3, 30, 10));
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // เพิ่มปุ่มสินค้าจำลอง
        String[] items = {"กาแฟ", "ชาเย็น", "นมสด", "ขนมปัง", "เค้ก", "คุ้กกี้"};
        for (String item : items) {
            JButton btn = new JButton(item);
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
            productPanel.add(btn);
        }

        frame.add(productPanel);
        frame.setVisible(true);
    }
}
