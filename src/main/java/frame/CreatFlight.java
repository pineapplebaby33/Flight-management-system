package frame;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import flight.DbInsert;
import flight.DbSelect;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreatFlight {

	private JFrame frame;
	private JTextField PriceText;
	private JTextField seatCapacityText;
	private JTextField flightnameText;
	private JTextField ArrTimeText;
	private JTextField StartTimeText;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreatFlight window = new CreatFlight();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//方法_页面函数
	public JFrame getFrame() {
		return this.frame;
	}

	//构造函数
	public CreatFlight() {
		initialize();
	}

	//方法_初始化
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 738, 548);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		//标签_航班号
		JLabel flightNamelabel = new JLabel("航班号：");
		flightNamelabel.setBounds(43, 36, 62, 29);
		frame.getContentPane().add(flightNamelabel);

		//输入框_航班号
		flightnameText = new JTextField();
		flightnameText.setFont(new Font("宋体", Font.PLAIN, 14));
		flightnameText.setText("");
		flightnameText.setBounds(188, 27, 126, 35);
		frame.getContentPane().add(flightnameText);
		flightnameText.setColumns(10);

		//标签_起飞城市
		JLabel label = new JLabel("起飞城市");
		label.setBounds(43, 75, 108, 29);
		frame.getContentPane().add(label);

		//下拉框_起飞城市
		final JComboBox StartCitycomboBox = new JComboBox();
		StartCitycomboBox.setModel(new DefaultComboBoxModel(new String[] {
				"北京", "上海", "天津", "重庆",
				"哈尔滨", "长春", "沈阳",
				"呼和浩特", "石家庄",
				"乌鲁木齐", "兰州", "西宁",
				"西安 ", "银川", "郑州",
				"济南", "太原", "合肥", "长沙",
				"武汉", "南京", "成都", "贵阳",
				"昆明", "南宁", "拉萨", "杭州",
				"南昌", "广州", "福州", "台北",
				"海口", "香港", "澳门" }));
		StartCitycomboBox.setToolTipText("");
		StartCitycomboBox.setBounds(188, 75, 126, 35);
		frame.getContentPane().add(StartCitycomboBox);

		//标签_降落城市
		JLabel label2 = new JLabel("降落城市");
		label2.setBounds(43, 133, 108, 29);
		frame.getContentPane().add(label2);

		//标签_降落城市
		final JComboBox ArrCitycomboBox = new JComboBox();
		if(AdminFunction.isDomestic){
			ArrCitycomboBox.setModel(new DefaultComboBoxModel(new String[] {
					"北京", "上海", "天津", "重庆",
					"哈尔滨", "长春", "沈阳",
					"呼和浩特", "石家庄",
					"乌鲁木齐", "兰州", "西宁",
					"西安 ", "银川", "郑州",
					"济南", "太原", "合肥", "长沙",
					"武汉", "南京", "成都", "贵阳",
					"昆明", "南宁", "拉萨", "杭州",
					"南昌", "广州", "福州", "台北",
					"海口", "香港", "澳门" }));
			ArrCitycomboBox.setToolTipText("");
			ArrCitycomboBox.setBounds(188, 141, 126, 35);
			frame.getContentPane().add(ArrCitycomboBox);
		}else{
			ArrCitycomboBox.setModel(new DefaultComboBoxModel(new String[] {
					"纽约", "伦敦", "巴黎", "柏林", "阿姆斯特丹", "慕尼黑",
					"罗马", "东京", "首尔", "曼谷", "悉尼", "奥克兰",
					"温哥华", "莫斯科", "芝加哥", "洛杉矶", "新加坡",
					"旧金山" }));
			ArrCitycomboBox.setToolTipText("");
			ArrCitycomboBox.setBounds(188, 141, 126, 35);
			frame.getContentPane().add(ArrCitycomboBox);
		}

		//标签_起飞时间
		JLabel label_2 = new JLabel("起飞时间");
		label_2.setBounds(43, 212, 62, 29);
		frame.getContentPane().add(label_2);

		//输入框_起飞时间
		StartTimeText = new JTextField();
		StartTimeText.setFont(new Font("宋体", Font.PLAIN, 14));
		StartTimeText.setBounds(188, 212, 126, 29);
		frame.getContentPane().add(StartTimeText);
		StartTimeText.setColumns(10);

		//标签_降落时间
		JLabel label_3 = new JLabel("降落时间");
		label_3.setBounds(43, 264, 54, 15);
		frame.getContentPane().add(label_3);

		//输入框_降落时间
		ArrTimeText = new JTextField();
		ArrTimeText.setFont(new Font("宋体", Font.PLAIN, 14));
		ArrTimeText.setBounds(188, 261, 126, 35);
		frame.getContentPane().add(ArrTimeText);
		ArrTimeText.setColumns(10);

		//标签_时间格式提示
		JLabel label_4 = new JLabel(
				"时间格式为YYYY-MM-DD-HH-MM-SS");
		label_4.setForeground(Color.GRAY);
		label_4.setBounds(359, 226, 286, 15);
		frame.getContentPane().add(label_4);

		JLabel label_5 = new JLabel("例如:2017-05-02-00-00-00");
		label_5.setForeground(Color.GRAY);
		label_5.setBounds(359, 264, 180, 15);
		frame.getContentPane().add(label_5);

		//标签_价格
		JLabel lblNewLabel = new JLabel("价格");
		lblNewLabel.setBounds(43, 306, 44, 29);
		frame.getContentPane().add(lblNewLabel);

		//输入框_价格
		PriceText = new JTextField();
		PriceText.setFont(new Font("宋体", Font.PLAIN, 14));
		PriceText.setBounds(188, 306, 126, 31);
		frame.getContentPane().add(PriceText);
		PriceText.setColumns(10);

		JLabel label_6 = new JLabel("元");
		label_6.setBounds(341, 306, 33, 30);
		frame.getContentPane().add(label_6);

		//标签_航班状态
		JLabel lblNewLabel_1 = new JLabel("航班状态");
		lblNewLabel_1.setBounds(439, 89, 54, 15);
		frame.getContentPane().add(lblNewLabel_1);

		//下拉框_航班状态
		final JComboBox FlightStateCombox = new JComboBox();
		FlightStateCombox.setFont(new Font("宋体", Font.PLAIN, 14));
		FlightStateCombox.setModel(new DefaultComboBoxModel(new String[] {
				"UNPUBLISHED", "AVAILABLE", "FULL", "TERMINATE" }));
		FlightStateCombox.setBounds(558, 79, 126, 31);
		frame.getContentPane().add(FlightStateCombox);

		//标签_容量
		JLabel label_1 = new JLabel("容量");
		label_1.setBounds(439, 36, 55, 29);
		frame.getContentPane().add(label_1);

		//输入框_容量
		seatCapacityText = new JTextField();
		seatCapacityText.setFont(new Font("宋体", Font.PLAIN, 14));
		seatCapacityText.setBounds(558, 36, 126, 29);
		frame.getContentPane().add(seatCapacityText);
		seatCapacityText.setColumns(10);


		//按钮_确认创建
		JButton Create = new JButton("确认创建");
		Create.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String FlightName = flightnameText.getText();
				String StartCity = StartCitycomboBox.getItemAt(
						StartCitycomboBox.getSelectedIndex()).toString();
				String ArrCity = ArrCitycomboBox.getItemAt(
						ArrCitycomboBox.getSelectedIndex()).toString();
				String StartTime = StartTimeText.getText();
				String ArrTime = ArrTimeText.getText();
				int Seat = Integer.parseInt(seatCapacityText.getText());
				String FlightStatus = FlightStateCombox.getItemAt(
						FlightStateCombox.getSelectedIndex()).toString();
				Float price = Float.parseFloat(PriceText.getText());
				//DbSelect sel = new DbSelect();
				//int newid  =sel.NewGetId(AdminFunction.isDomestic) + 1;
				boolean x = new DbInsert().FlightInsert(StartTime, ArrTime,
						StartCity, ArrCity, ArrTime, price, 0, Seat,
						FlightStatus, "", FlightName,AdminFunction.isDomestic);
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
		Create.setBounds(480, 390, 153, 37);
		frame.getContentPane().add(Create);


		//按钮_返回
		JButton button = new JButton("返回");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				AdminFunction windowAdmin = new AdminFunction();
				windowAdmin.getAdminFrame().setVisible(true);
			}
		});
		button.setBounds(188, 390, 126, 37);
		frame.getContentPane().add(button);

	}

}
