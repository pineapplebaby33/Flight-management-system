package frame;

import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import flight.*;
import listeners.PaymentCompleteListener;

public class Pay {

	private JFrame frame;
	private JPasswordField passwordField;
	private JButton back;
	//private boolean isDmestic;
	private PaymentCompleteListener paymentListener; // 添加支付完成监听器

	public void setPaymentCompleteListener(PaymentCompleteListener listener) {
		this.paymentListener = listener;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					boolean isDmestic=true;
					Pay window = new Pay(isDmestic);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Pay(boolean isDmestic) {
		initialize(isDmestic);
	}

	private void initialize(boolean isDmestic) {
		frame = new JFrame();
		frame.setTitle("支付页面");
		frame.setBounds(550, 100, 350, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel label = new JLabel(
				"确认支付请输入密码");
		label.setBounds(10, 64, 129, 73);
		frame.getContentPane().add(label);

		//按钮_支付
		JButton sure = new JButton("确认支付");
		sure.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//获取密码
				char[] cpwd = passwordField.getPassword();
				String pwd = "";
				for (int i = 0; i < cpwd.length; i++) {
					pwd += cpwd[i];
				}
				//检查订单是否重复
				if (Order.IsHasOrder(Login.PassengerId, Login.FlightId,Research.isDomestic)) {
					int x1 = Passenger.ReserveFlight(Login.PassengerId, Login.FlightId, pwd,isDmestic);
					System.out.println("在PAY里Passenger.ReserveFlight");
					if (x1==1) {
						frame.setVisible(false);
						Research window = new Research();
						window.getFrame().setVisible(true);
						//正常购票
						boolean x2 =Flight.ReserveFlight(Login.PassengerId,Login.FlightId,isDmestic);
						System.out.println("正常购票成功");
						//存套餐订单
						if((Objects.equals(Login.packagestatus, "国内随心飞")&&isDmestic)||
								(Objects.equals(Login.packagestatus, "学生寒暑假")&&isDmestic)||
								(Objects.equals(Login.packagestatus, "国外随心飞")&&!isDmestic)){
							DbSelect select = new DbSelect();
							Order o = select.OrderSelect(Login.PassengerId,Login.FlightId,isDmestic);
							Flight f = select.FlightSelect(Login.FlightId,isDmestic);
							int pr =PackageOrder.ReservePackageOrder(Login.Pwd,Login.PassengerId,f.getPrice(),o.getId());
							System.out.println("订购套餐内的航班成功");
						}
						AllDialog.Dialog(window.getFrame(), "购票成功");

						// 调用支付完成监听器，通知支付成功
						if (paymentListener != null) {
							paymentListener.onPaymentComplete();
						}

					} else if(x1==2){
						AllDialog.Dialog(frame, "支付失败，请检查密码");
					}else if(x1==3) {
						AllDialog.Dialog(frame, "支付失败，当前航班已满");
					}
				} else {
					AllDialog.Dialog(frame, "请勿重复预订");
				}
			}
		});
		sure.setBounds(41, 157, 93, 38);
		frame.getContentPane().add(sure);

		passwordField = new JPasswordField();
		passwordField.setBounds(181, 90, 129, 21);
		frame.getContentPane().add(passwordField);

		back = new JButton("返回");
		back.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				ReserveFlight window = new ReserveFlight(isDmestic);
				window.getFrame().setVisible(true);
			}
		});
		back.setBounds(178, 157, 103, 38);
		frame.getContentPane().add(back);
	}

	public Window getFrame() {
		return frame;
	}
}
