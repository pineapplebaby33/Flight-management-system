package frame;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import flight.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import javax.swing.JPasswordField;

public class Reschedule {
    private JFrame frame;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Reschedule window = new Reschedule();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Reschedule() {
        initialize();
    }

    private void initialize() {
            frame = new JFrame();
            frame.setTitle("改签订单");
            frame.setBounds(100, 100, 450, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setLayout(null);

            Order o = new DbSelect().OrderSelect(Login.OrderId,Research.isDomestic);
    }

    public JFrame getFrame() {
        return frame;
    }

}
