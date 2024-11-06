//航班查询界面，用户可以选择出发城市、到达城市、起飞时间、到达时间等条件来查询航班信息，
// 并显示在表格中。用户可以通过点击表格中的航班条目进行预定
package frame;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import flight.DbSelect;
import flight.Flight;
import flight.Order;
import flight.Passenger;

import java.awt.event.MouseEvent;
import java.io.Serial;
import java.time.format.DateTimeFormatter;

public class Research {

	private JFrame frame;
	private JTable Flight_Table;
	private JScrollPane scrollPane;

	public JFrame getFrame() {

		return this.frame;
	}


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Research window = new Research();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Research() {
		initialize();
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {
		//窗口信息
		frame = new JFrame();
		Passenger p = new DbSelect().PassengerSelect(Login.PassengerId);
		if (p != null) {
			String Frametext = "航班信息   欢迎," + p.getRealName();
			frame.setTitle(Frametext);
		} else {
			frame.setTitle("航班信息");
		}

		//页面布局
		frame.setBounds(250, 50, 950, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		setTable(frame);
		//跳转订单界面
		JButton btnNewButton = new JButton("我的订单");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				PassengerOrder window = new PassengerOrder();
				window.getFrame().setVisible(true);
			}
		});
		//btnNewButton.setFont(new Font("宋体", Font.PLAIN, 12));
		btnNewButton.setBounds(837, 7, 87, 42);
		frame.getContentPane().add(btnNewButton);

		//搜索按钮
		JButton button = new JButton("搜索");
		button.setFont(new Font("宋体", Font.PLAIN, 14));
		button.setBounds(1717, 83, 173, 41);
		frame.getContentPane().add(button);

		//标签价格
		JLabel lblNewLabel_8 = new JLabel("价格");
		lblNewLabel_8.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_8.setBounds(1423, 205, 92, 44);
		frame.getContentPane().add(lblNewLabel_8);

		//标签航班状态
		JLabel lblNewLabel_9 = new JLabel("航班状态");
		lblNewLabel_9.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_9.setBounds(1674, 205, 169, 44);
		frame.getContentPane().add(lblNewLabel_9);

		//标签_起飞时间
		JLabel StartTimeLabel = new JLabel("起飞时间");
		StartTimeLabel.setFont(new Font("宋体", Font.PLAIN, 14));
		StartTimeLabel.setBounds(391, 14, 92, 42);
		frame.getContentPane().add(StartTimeLabel);

		//日期选择器
		final DateChooser dateChooser = new DateChooser(frame.getContentPane(),
				100);
		dateChooser.setBounds(366, 66, 126, 42);
		frame.getContentPane().add(dateChooser);

		//标签_到达时间
		JLabel EndTimeLabel = new JLabel("到达时间");
		EndTimeLabel.setBounds(546, 28, 54, 15);
		frame.getContentPane().add(EndTimeLabel);

		//日期选择器
		final DateChooser dateChooser2 = new DateChooser(
				frame.getContentPane(), 100);
		dateChooser2.setBounds(521, 66, 126, 42);
		frame.getContentPane().add(dateChooser2);

		//返回登录界面
		JButton button_1 = new JButton("返回");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);

				Login window = new Login();
				window.getFrame().setVisible(true);
			}
		});

		button_1.setFont(new Font("宋体", Font.PLAIN, 14));
		button_1.setBounds(869, 1208, 173, 41);
		frame.getContentPane().add(button_1);

		//标签_起飞城市
		JLabel label = new JLabel("起飞城市");
		label.setBounds(79, 21, 60, 29);
		frame.getContentPane().add(label);

		//出发城市下拉框
		final JComboBox startCity = new JComboBox();
		startCity.setModel(new DefaultComboBoxModel(new String[] {
				"北京", "上海", "天津", "重庆",
				"哈尔滨", "长春", "沈阳",
				"呼和浩特", "石家庄",
				"乌鲁木齐", "兰州", "西宁",
				"西安 ", "银川", "郑州",
				"济南", "太原", "合肥", "长沙",
				"武汉", "南京", "成都", "贵阳",
				"昆明", "南宁", "拉萨", "杭州",
				"南昌", "广州", "福州", "台北",
				"海口", "香港", "澳门","深圳" }));
		startCity.setToolTipText("");
		startCity.setBounds(41, 62, 127, 37);
		frame.getContentPane().add(startCity);

		//降落城市下拉框
		final JComboBox arrivalCity = new JComboBox();
		arrivalCity.setModel(new DefaultComboBoxModel(new String[] {
				"北京", "上海", "天津", "重庆",
				"哈尔滨", "长春", "沈阳",
				"呼和浩特", "石家庄",
				"乌鲁木齐", "兰州", "西宁",
				"西安 ", "银川", "郑州",
				"济南", "太原", "合肥", "长沙",
				"武汉", "南京", "成都", "贵阳",
				"昆明", "南宁", "拉萨", "杭州",
				"南昌", "广州", "福州", "台北",
				"海口", "香港", "澳门","深圳"}));
		arrivalCity.setToolTipText("");
		arrivalCity.setBounds(208, 62, 127, 37);
		frame.getContentPane().add(arrivalCity);

		//标签降落城市
		JLabel label_1 = new JLabel("降落城市");
		label_1.setBounds(233, 21, 60, 29);
		frame.getContentPane().add(label_1);

		//查询按钮
		JButton search = new JButton("查询");
		search.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String s1 = startCity.getItemAt(startCity.getSelectedIndex())
						.toString();
				String s2 = arrivalCity.getItemAt(
						arrivalCity.getSelectedIndex()).toString();
				String date1 = dateChooser.getText();
				String date2 = dateChooser2.getText();
				flight.Flight[] flights2 = new DbSelect().FlightSelect(date1,  s1, s2);//返回12列
				if (flights2 != null) {
					setTable(frame, flights2);
				} else {
					AllDialog.Dialog(frame, "当前没有符合条件的航班");
				}
			}
		});
		search.setFont(new Font("宋体", Font.PLAIN, 16));
		search.setBounds(700, 66, 69, 42);
		frame.getContentPane().add(search);

		//提示标签
		JLabel label_2 = new JLabel(
				"提示：双击航班即可预定航班");
		label_2.setForeground(Color.RED);
		label_2.setBounds(12, 510, 344, 15);
		frame.getContentPane().add(label_2);

		//按钮_刷新列表
		JButton fresh = new JButton("刷新列表");
		fresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setTable(frame);
			}
		});
		fresh.setBounds(837, 56, 87, 42);
		frame.getContentPane().add(fresh);

		//按钮_退出登录
		JButton button_2 = new JButton("退出登录");
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Login.PassengerId = 0;
				Login.OrderId = 0;
				frame.setVisible(false);
				Login window = new Login();
				window.getFrame().setVisible(true);
			}
		});
		button_2.setBounds(800, 516, 109, 36);
		frame.getContentPane().add(button_2);
	}

	//创建表格_by frame 初始化/刷新
	private void setTable(final JFrame frame) {
		if (scrollPane != null) {
			frame.getContentPane().remove(scrollPane); // 清除旧的表格
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		String[] columnNames = { "ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "价格", "是否预定","模式" };
		Flight[] flights = new DbSelect().FlightSelectForPass();
		String[][] flight_ob = new String[flights.length][9];
		//遍历 flights 数组，将每个航班的信息存入 flight_ob 二维数组，准备显示在表格中。
		for (int i = 0; i < flights.length; i++) {
			flight_ob[i][0] = Integer.toString(flights[i].getId());
			flight_ob[i][1] = flights[i].getFlightName();
			flight_ob[i][2] = flights[i].getStartCity();
			flight_ob[i][3] = flights[i].getArrivalCity();
			flight_ob[i][4] = flights[i].getStartTime().format(formatter);
			flight_ob[i][5] = flights[i].getArrivalTime().format(formatter);
			flight_ob[i][6] = String.valueOf(flights[i].getPrice());
			//检查当前登录的乘客是否已经预定该航班
			if (Order.IsHasOrder(Login.PassengerId, flights[i].getId())) {
				flight_ob[i][7] = "未预定";
			} else {
				flight_ob[i][7] = "已预定";
			}
			flight_ob[i][8] = "直飞";
		}

		//创建 JTable 表格，显示航班数据，并设置表格不可编辑。
		Flight_Table = new JTable(flight_ob, columnNames) {
			//serialVersionUID 是一个唯一的标识符，用于验证序列化和反序列化的对象版本一致性。
			// 如果没有一致的 serialVersionUID，在序列化版本不匹配时可能会抛出 InvalidClassException
			@Serial
			private static final long serialVersionUID = -4737302915707325665L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		//设置表格列的宽度，选择模式为单选，设置选中航班时的背景色和前景色，并设置表格的位置和大小
		TableColumn column = null;
		int colunms = Flight_Table.getColumnCount();
		//遍历列
		for (int i = 0; i < colunms; i++) {
			column = Flight_Table.getColumnModel().getColumn(i);
			column.setPreferredWidth(100);
		}
		Flight_Table.getColumnModel().getColumn(0).setPreferredWidth(20);//第一列
		Flight_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//单选模式
		Flight_Table.setSelectionBackground(Color.LIGHT_GRAY);//选中后
		Flight_Table.setSelectionForeground(Color.yellow);
		Flight_Table.setBounds(21, 143, 700, 363);


		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 143, 912, 363);

		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(Flight_Table);
		Flight_Table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				//跳转预定航班界面
				if (e.getClickCount() == 2) {//双击事件
					int row = Flight_Table.getSelectedRow();
					String preId1 = Flight_Table.getValueAt(row, 0).toString();
					frame.setVisible(false);
					Login.FlightId = Integer.parseInt(preId1);
					ReserveFlight window = new ReserveFlight();
					window.getFrame().setVisible(true);
				}
			}
		});
	}

	//创建表格_by frame 精准查询
	private void setTable(final JFrame frame, Flight[] flights) {
		if (scrollPane != null) {
			frame.getContentPane().remove(scrollPane); // 清除旧的表格
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		String[] columnNames = { "ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "价格", "是否预定","模式" };
		String[][] flight_ob = new String[flights.length][9];
		for (int i = 0; i < flights.length; i++) {
			flight_ob[i][0] = Integer.toString(flights[i].getId());
			flight_ob[i][1] = flights[i].getFlightName();
			flight_ob[i][2] = flights[i].getStartCity();
			flight_ob[i][3] = flights[i].getArrivalCity();
			flight_ob[i][4] = flights[i].getStartTime().format(formatter);
			flight_ob[i][5] = flights[i].getArrivalTime().format(formatter);
			flight_ob[i][6] = String.valueOf(flights[i].getPrice());
			if (Order.IsHasOrder(Login.PassengerId, flights[i].getId())) {
				flight_ob[i][7] = "已预定";
			} else {
				flight_ob[i][7] = "未预定";
			}
			flight_ob[i][8] = "直飞";
		}
		Flight_Table = new JTable(flight_ob, columnNames) {
			private static final long serialVersionUID = -5723427406160453043L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		TableColumn column = null;
		int colunms = Flight_Table.getColumnCount();
		for (int i = 0; i < colunms; i++) {
			column = Flight_Table.getColumnModel().getColumn(i);
			column.setPreferredWidth(100);
		}
		Flight_Table.getColumnModel().getColumn(0).setPreferredWidth(20);
		Flight_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Flight_Table.setSelectionBackground(Color.LIGHT_GRAY);
		Flight_Table.setSelectionForeground(Color.yellow);
		Flight_Table.setBounds(21, 143, 700, 363);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 143, 912, 363);

		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(Flight_Table);
		Flight_Table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = Flight_Table.getSelectedRow();
					String preId1 = Flight_Table.getValueAt(row, 0).toString();
					frame.setVisible(false);
					Login.FlightId = Integer.parseInt(preId1);
					ReserveFlight window = new ReserveFlight();
					window.getFrame().setVisible(true);
				}
			}
		});
	}
}

