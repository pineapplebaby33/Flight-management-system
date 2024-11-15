package frame;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import javax.swing.JPasswordField;

import flight.DbInsert;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EnrollPassenger {

	private JFrame frame;
	private JTextField nameText;
	private JTextField IdentityText;
	private JPasswordField passwordField;

	public JFrame getFrame() {
		return this.frame;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EnrollPassenger window = new EnrollPassenger();
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
	public EnrollPassenger() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setFont(new Font("宋体", Font.PLAIN, 12));
		frame.setTitle("注册");
		frame.setBounds(100, 100, 634, 511);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		//标签_姓名
		JLabel realName = new JLabel("姓        名");
		realName.setFont(new Font("宋体", Font.PLAIN, 14));
		realName.setBounds(123, 73, 175, 38);
		frame.getContentPane().add(realName);

		//输入框_姓名
		nameText = new JTextField();
		nameText.setBounds(322, 77, 146, 38);
		frame.getContentPane().add(nameText);
		nameText.setColumns(10);


		//标签_身份证号
		JLabel identityID = new JLabel("身份证号");
		identityID.setFont(new Font("宋体", Font.PLAIN, 14));
		identityID.setBounds(123, 134, 175, 53);
		frame.getContentPane().add(identityID);

		//身份证号输入框
		IdentityText = new JTextField();
		IdentityText.setBounds(322, 145, 146, 38);
		frame.getContentPane().add(IdentityText);
		IdentityText.setColumns(10);

		//标签_密码
		JLabel password = new JLabel("密        码");
		password.setFont(new Font("宋体", Font.PLAIN, 14));
		password.setBounds(123, 210, 175, 45);
		frame.getContentPane().add(password);


		//密码输入框
		passwordField = new JPasswordField();
		passwordField.setBounds(322, 217, 146, 38);
		frame.getContentPane().add(passwordField);
		passwordField.setColumns(10);


		//取消按钮
		JButton btnNewButton = new JButton("取消");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				frame.setVisible(false);
				Login window = new Login();
				window.getFrame().setVisible(true);
			}
		});
		btnNewButton.setFont(new Font("宋体", Font.PLAIN, 14));
		btnNewButton.setBounds(136, 304, 146, 41);
		frame.getContentPane().add(btnNewButton);



		//注册按钮
		JButton button = new JButton("注册");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String realName = nameText.getText();
				String iden = IdentityText.getText();
				char[] pa = passwordField.getPassword();
				String pwd = "";
				for (int i = 0; i < pa.length; i++) {
					pwd += pa[i];
				}
				boolean ismath = ismath(iden);
				if (realName.equals("") || iden.equals("") || pwd.equals("")) {
					AllDialog.Dialog(frame, "请输入完整信息");
				}else if(!ismath){
					AllDialog.Dialog(frame, "身份证号格式不正确");
				}
				else if(iden.length()!=18){
					AllDialog.Dialog(frame, "身份证号位数不为18位");
				}
				else {
					boolean x = new DbInsert().PassengerInsert(realName, iden, pwd, "","");
					if (x) {
						frame.setVisible(false);
						Login window = new Login();
						window.getFrame().setVisible(true);
						AllDialog.Dialog(window.getFrame(), "注册成功");
					} else {
						AllDialog.Dialog(frame, "注册失败");
					}

				}
			}
		});
		button.setFont(new Font("宋体", Font.PLAIN, 14));
		button.setBounds(322, 304, 146, 41);
		frame.getContentPane().add(button);

	}

	private boolean ismath(String iden){
		for (char c : iden.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false; // 如果遇到非数字字符，返回false
			}
		}
		return true;
	}

}
