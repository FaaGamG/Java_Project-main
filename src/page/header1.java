package page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.*;

public class header1 {
    private JFrame fr;

    public void header( JFrame fr) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        headerPanel.setBackground(new Color(52, 73, 94));

        JButton admin = new JButton("Admin");
        JButton user = new JButton("User");
        headerPanel.add(admin);
        headerPanel.add(user);
        fr.add(headerPanel, BorderLayout.NORTH);
        fr.setSize(400, 200);
        fr.setLocationRelativeTo(null);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setVisible(true);
    }
}
