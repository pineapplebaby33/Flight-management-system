package frame;

import flight.DbInsert;

import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddAdmin {
    private JFrame frame;
    private JTextField adminName;
    private JPasswordField adminPass;

    /**
     * Launch the application.
     */

    /**
     * Create the application.
     */
    public AddAdmin() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 724, 531);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel label = new JLabel("管理员姓名");
        label.setBounds(113, 137, 136, 29);
        frame.getContentPane().add(label);

        JLabel label_1 = new JLabel("密码");
        label_1.setBounds(141, 214, 68, 29);
        frame.getContentPane().add(label_1);

        adminName = new JTextField();
        adminName.setBounds(356, 134, 126, 35);
        frame.getContentPane().add(adminName);
        adminName.setColumns(10);

        adminPass = new JPasswordField();
        adminPass.setBounds(356, 211, 126, 35);
        frame.getContentPane().add(adminPass);

        JButton Create = new JButton("确认创建");
        Create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String name = adminName.getText();
                char[] pass = adminPass.getPassword();
                String pas = "";
                for (int i = 0; i < pass.length; i++) {
                    pas += pass[i];

                }
                boolean x = new DbInsert().AdminInsert(name, pas);
                if (x) {
                    frame.setVisible(false);
                    AdminFunction window = new AdminFunction();
                    window.getAdminFrame().setVisible(true);
                    AllDialog.Dialog(window.getAdminFrame(), "操作成功");
                } else {
                    AllDialog.Dialog(frame, "操作失败，请重试");
                }
            }
        });
        Create.setBounds(275, 354, 153, 37);
        frame.getContentPane().add(Create);
    }

    public Window getFrame() {
        // TODO Auto-generated method stub
        return frame;
    }
}
