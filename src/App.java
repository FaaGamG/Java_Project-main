import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.*;

public class App {
    private JFrame fr;
    private JButton btn_admin;

    public void header() {
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

    public void footer() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        footerPanel.setBackground(new Color(52, 73, 94));

        JButton admin = new JButton("Admin");
        JButton user = new JButton("User");
        footerPanel.add(admin);
        // footerPanel.add(user);
        fr.add(footerPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        App app = new App();
        app.fr = new JFrame();
        app.header();
        app.footer();
    }
}