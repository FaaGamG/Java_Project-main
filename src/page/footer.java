package page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.*;

public class footer extends JFrame {
    // public footer(JFrame fr) {
    //     this.fr = fr;
    // }

    public void footer(JFrame fr) {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        footerPanel.setBackground(new Color(52, 73, 94));

        JButton admin = new JButton("Admin");
        JButton user = new JButton("User");
        footerPanel.add(admin);
        // footerPanel.add(user);
        fr.add(footerPanel, BorderLayout.SOUTH);
    }
}
