package frame;

import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import flight.DbSelect;
import flight.Flight;
import flight.PackageOrder;
import flight.Passenger;

import static java.awt.SystemColor.window;

public class ReserveFlight {
	private JFrame frame;
	private List<ReserveFlight> transitWindows = new ArrayList<>();//存储所有中转窗口的列表
	private Timer timer;//定时器


	public ReserveFlight(boolean isDmestic) {
		initialize(isDmestic);
	}

	private void initialize(boolean isDmestic) {
		Flight f = new DbSelect().FlightSelect(Login.FlightId, isDmestic);

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
		JButton Create = new JButton("点击预定");
		Create.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				System.out.println("ReserveFlight跳转支付界面");
				Pay windowPay = new Pay(isDmestic);
				System.out.println("支付界面跳转ReserveFlight");
				windowPay.getFrame().setVisible(true);
			}
		});
		Create.setBounds(92, 386, 153, 37);
		frame.getContentPane().add(Create);

		JLabel label_1 = new JLabel("剩余票数");
		label_1.setBounds(439, 36, 55, 29);
		frame.getContentPane().add(label_1);

		JLabel label_4 = new JLabel("当前套餐");
		label_4.setBounds(439, 65, 55, 29);
		frame.getContentPane().add(label_4);

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

		JLabel PackageStatus = new JLabel("New label");
		PackageStatus.setBounds(558, 75, 123, 15);
		frame.getContentPane().add(PackageStatus);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		FlightName.setText(f.getFlightName());
		StartCity.setText(f.getStartCity());
		ArrCity.setText(f.getArrivalCity());
		StartTime.setText(f.getStartTime().format(formatter));
		ArrTime.setText(f.getArrivalTime().format(formatter));
		Price.setText(PackageOrder.discountPrice(f.getPrice()) + "￥");
		Seat.setText(Integer.toString(f.getSeatCapacity()-f.getCurrentPassengers()));
		PackageStatus.setText(Login.packagestatus);


	}

	public Window getFrame() {
		return frame;
	}
}
