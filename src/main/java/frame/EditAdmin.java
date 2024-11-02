package frame;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;

import flight.Admin;
import flight.DbSelect;
import flight.DbUpdate;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditAdmin {

	private JFrame frame;
	private JPasswordField oldpass;
	private JPasswordField newpass;
	private JTextField newuser;
	private JTextField olduser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EditAdmin window = new EditAdmin();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EditAdmin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 732, 566);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel label = new JLabel("新密码");
		label.setBounds(168, 287, 108, 29);
		frame.getContentPane().add(label);

		JLabel lblNewLabel = new JLabel("旧密码");
		lblNewLabel.setBounds(168, 237, 108, 29);
		frame.getContentPane().add(lblNewLabel);

		oldpass = new JPasswordField();
		oldpass.setBounds(324, 224, 193, 32);
		frame.getContentPane().add(oldpass);

		newpass = new JPasswordField();
		newpass.setBounds(324, 285, 193, 32);
		frame.getContentPane().add(newpass);

		JLabel label_1 = new JLabel("新用户名");
		label_1.setBounds(168, 129, 108, 29);
		frame.getContentPane().add(label_1);

		newuser = new JTextField();
		newuser.setBounds(324, 126, 126, 35);
		frame.getContentPane().add(newuser);
		newuser.setColumns(10);


		JLabel label_2 = new JLabel("旧用户名");
		label_2.setBounds(168, 65, 54, 15);
		frame.getContentPane().add(label_2);

		olduser = new JTextField();
		olduser.setBounds(324, 62, 126, 35);
		frame.getContentPane().add(olduser);
		olduser.setColumns(10);
		Admin nowadmin = new DbSelect().AdminSelect(Login.AdminId);
		olduser.setText(nowadmin.getUsername());

		JButton button = new JButton("确定");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String usernew = newuser.getText();
				String userold = olduser.getText();
				char[] p1 = oldpass.getPassword();
				char[] p2 = newpass.getPassword();
				String passold = "";
				String passnew = "";
				for (int i = 0; i < p1.length; i++) {
					passold += p1[i];
				}
				for (int i = 0; i < p2.length; i++) {
					passnew += p2[i];
				}
				if (Admin.CheckAdmin(userold, passold)) {
					boolean x = new DbUpdate().AdminUpdate(Login.AdminId, usernew, passnew);
					if (x) {
						frame.setVisible(false);
						AdminFunction window = new AdminFunction();
						window.getAdminFrame().setVisible(true);
					}
				} else {
					AllDialog.Dialog(frame, "���������");
				}

			}
		});
		button.setBounds(297, 412, 153, 37);
		frame.getContentPane().add(button);

		JButton button_2 = new JButton("返回");
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				AdminFunction n_adminFunction = new AdminFunction();
				n_adminFunction.getFrame().setVisible(true);
			}
		});
		//button_2.setFont(new Font("宋体", Font.PLAIN, 20));
		button_2.setBounds(297, 462, 153, 37);
		frame.getContentPane().add(button_2);

	}

	public Window getFrame() {
		// TODO Auto-generated method stub
		return frame;
	}
}
