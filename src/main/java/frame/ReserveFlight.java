package frame;

import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import flight.DateTime;
import flight.DbSelect;
import flight.Flight;

public class ReserveFlight {
	private JFrame frame;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ReserveFlight window = new ReserveFlight();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ReserveFlight() {
		initialize();
	}

	private void initialize() {
		Flight f = new DbSelect().FlightSelect(Login.FlightId);
		frame = new JFrame();
		frame.setBounds(100, 100, 738, 548);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel label = new JLabel("起飞城市");
		label.setBounds(43, 75, 108, 29);
		frame.getContentPane().add(label);

		JLabel label2 = new JLabel("降落城市");
		label2.setBounds(43, 133, 108, 29);
		frame.getContentPane().add(label2);

		JLabel label_2 = new JLabel("起飞时间");
		label_2.setBounds(43, 212, 62, 29);
		frame.getContentPane().add(label_2);

		JLabel lblNewLabel = new JLabel("价格");
		lblNewLabel.setBounds(43, 306, 44, 29);
		frame.getContentPane().add(lblNewLabel);

		JLabel flightNamelabel = new JLabel("航班号：");
		flightNamelabel.setBounds(43, 36, 62, 29);
		frame.getContentPane().add(flightNamelabel);

		//跳转支付界面
		JButton Create = new JButton("点击支付");
		Create.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				Pay windowPay = new Pay();
				windowPay.getFrame().setVisible(true);
			}
		});
		Create.setBounds(92, 386, 153, 37);
		frame.getContentPane().add(Create);

		JLabel label_1 = new JLabel("剩余票数");
		label_1.setBounds(439, 36, 55, 29);
		frame.getContentPane().add(label_1);

		JLabel label_3 = new JLabel("降落时间");
		label_3.setBounds(43, 264, 54, 15);
		frame.getContentPane().add(label_3);

		//返回航班界面
		JButton button = new JButton("取消预定");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				Research window = new Research();
				window.getFrame().setVisible(true);
			}
		});
		button.setBounds(413, 386, 153, 37);
		frame.getContentPane().add(button);

		JLabel FlightName = new JLabel("New label");
		FlightName.setBounds(121, 36, 74, 29);
		frame.getContentPane().add(FlightName);

		JLabel StartCity = new JLabel("New label");
		StartCity.setBounds(121, 75, 74, 29);
		frame.getContentPane().add(StartCity);

		JLabel ArrCity = new JLabel("New label");
		ArrCity.setBounds(121, 133, 92, 29);
		frame.getContentPane().add(ArrCity);

		JLabel StartTime = new JLabel("New label");
		StartTime.setBounds(121, 219, 145, 15);
		frame.getContentPane().add(StartTime);

		JLabel ArrTime = new JLabel("New label");
		ArrTime.setBounds(121, 264, 145, 15);
		frame.getContentPane().add(ArrTime);

		JLabel Price = new JLabel("New label");
		Price.setBounds(121, 313, 92, 15);
		frame.getContentPane().add(Price);

		JLabel Seat = new JLabel("New label");
		Seat.setBounds(558, 43, 123, 15);
		frame.getContentPane().add(Seat);


		FlightName.setText(f.getFlightName());
		StartCity.setText(f.getStartCity());
		ArrCity.setText(f.getArrivalCity());
		StartTime.setText(DateTime.GetDateTimeStr(f.getStartTime()));
		ArrTime.setText(DateTime.GetDateTimeStr(f.getArrivalTime()));
		Price.setText(Float.toString(f.getPrice()) + "￥");
		Seat.setText(Integer.toString(f.getSeatCapacity()));

	}

	public Window getFrame() {
		return frame;
	}
}
