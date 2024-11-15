package frame;

import java.awt.Window;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;


import javax.swing.JTextField;
import javax.swing.JComboBox;

import flight.*;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.time.format.DateTimeFormatter;

public class EditFlight {

	private JFrame frame;
	private JTextField IdtextField;
	private JTextField STtextField;
	private JTextField ATtextField_1;
	private JTextField Capp;
	private JTextField PriceText;


	public EditFlight() {
		initialize();
	}

	public JFrame getFrame() {
		return this.frame;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	private void initialize() {

		//// 从数据库中获取航班信息
		Flight f = new DbSelect().FlightSelect(Login.FlightId,AdminFunction.isDomestic);
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 728, 556);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		//标签-航班号
		JLabel FlightId = new JLabel("航班号");
		FlightId.setBounds(20, 22, 60, 29);
		frame.getContentPane().add(FlightId);

		//输入框-航班号
		IdtextField = new JTextField();
		IdtextField.setBounds(90, 26, 161, 21);
		frame.getContentPane().add(IdtextField);
		IdtextField.setColumns(10);


		//标签-起飞城市
		JLabel StartCity = new JLabel("起飞城市");
		StartCity.setBounds(20, 61, 60, 29);
		frame.getContentPane().add(StartCity);

		//下拉框-出发城市
		final JComboBox StartCitycomboBox = new JComboBox();
		StartCitycomboBox.setModel(new DefaultComboBoxModel(new String[] {
				f.getStartCity(), "北京", "上海",
				"天津", "重庆", "哈尔滨",
				"长春", "沈阳", "呼和浩特",
				"石家庄", "乌鲁木齐",
				"兰州", "西宁", "西安 ",
				"银川", "郑州", "济南", "太原",
				"合肥", "长沙", "武汉", "南京",
				"成都", "贵阳", "昆明", "南宁",
				"拉萨", "杭州", "南昌", "广州",
				"福州", "台北", "海口", "香港",
				"澳门" }));
		StartCitycomboBox.setToolTipText("");
		StartCitycomboBox.setBounds(90, 57, 127, 37);
		frame.getContentPane().add(StartCitycomboBox);

		//标签-降落城市
		JLabel ArrCity = new JLabel("降落城市");
		ArrCity.setBounds(20, 116, 60, 29);
		frame.getContentPane().add(ArrCity);


		//下拉框-到达城市
		final JComboBox ArrCitycomboBox = new JComboBox();
		if(AdminFunction.isDomestic){
			ArrCitycomboBox.setModel(new DefaultComboBoxModel(new String[] {
					f.getArrivalCity(), "北京", "上海",
					"天津", "重庆", "哈尔滨",
					"长春", "沈阳", "呼和浩特",
					"石家庄", "乌鲁木齐",
					"兰州", "西宁", "西安 ",
					"银川", "郑州", "济南", "太原",
					"合肥", "长沙", "武汉", "南京",
					"成都", "贵阳", "昆明", "南宁",
					"拉萨", "杭州", "南昌", "广州",
					"福州", "台北", "海口", "香港",
					"澳门" }));
			ArrCitycomboBox.setToolTipText("");
			ArrCitycomboBox.setBounds(90, 112, 127, 37);
			frame.getContentPane().add(ArrCitycomboBox);

		}else{
			ArrCitycomboBox.setModel(new DefaultComboBoxModel(new String[] {
					f.getArrivalCity(), "纽约", "伦敦", "巴黎", "柏林", "阿姆斯特丹", "慕尼黑",
					"罗马", "东京", "首尔", "曼谷", "悉尼", "奥克兰",
					"温哥华", "莫斯科", "芝加哥", "洛杉矶", "新加坡",
					"旧金山" }));
			ArrCitycomboBox.setToolTipText("");
			ArrCitycomboBox.setBounds(90, 112, 127, 37);
			frame.getContentPane().add(ArrCitycomboBox);
		}

		//标签-起飞时间
		JLabel StartTime = new JLabel("起飞时间");
		StartTime.setBounds(20, 173, 60, 29);
		frame.getContentPane().add(StartTime);

		//输入框_起飞时间
		STtextField = new JTextField();
		STtextField.setBounds(90, 177, 153, 21);
		frame.getContentPane().add(STtextField);
		STtextField.setColumns(10);

		//标签-降落时间
		JLabel ArrTime = new JLabel("降落时间");
		ArrTime.setBounds(20, 212, 60, 29);
		frame.getContentPane().add(ArrTime);

		//输入框_降落时间
		ATtextField_1 = new JTextField();
		ATtextField_1.setBounds(90, 216, 153, 21);
		frame.getContentPane().add(ATtextField_1);
		ATtextField_1.setColumns(10);

		//标签_格式提醒
		JLabel Warning = new JLabel("");
		Warning.setFont(new Font("宋体", Font.PLAIN, 15));
		Warning.setForeground(Color.RED);
		Warning.setBounds(307, 36, 300, 37);

		frame.getContentPane().add(Warning);
		JLabel lblyyyymmddhhmmss = new JLabel(
				"时间格式为YYYY-MM-DD-HH-MM-SS");
		lblyyyymmddhhmmss.setForeground(Color.GRAY);
		lblyyyymmddhhmmss.setBounds(307, 180, 200, 15);
		frame.getContentPane().add(lblyyyymmddhhmmss);

		JLabel label = new JLabel("例如:2017-05-02-00-00-00");
		label.setForeground(Color.GRAY);
		label.setBounds(307, 219, 180, 15);
		frame.getContentPane().add(label);

		//标签-容量
		JLabel Seat = new JLabel("容量");
		Seat.setBounds(20, 261, 48, 29);
		frame.getContentPane().add(Seat);

		//输入框_容量
		Capp = new JTextField();
		Capp.setBounds(90, 265, 153, 21);
		frame.getContentPane().add(Capp);
		Capp.setColumns(10);

		//标签-价格
		JLabel Price = new JLabel("价格");
		Price.setBounds(20, 300, 60, 29);
		frame.getContentPane().add(Price);

		//输入框_价格
		PriceText = new JTextField();
		PriceText.setBounds(90, 304, 153, 21);
		frame.getContentPane().add(PriceText);
		PriceText.setColumns(10);

		//标签_航班状态
		JLabel FlightState = new JLabel("航班状态");
		FlightState.setBounds(20, 364, 54, 15);
		frame.getContentPane().add(FlightState);

		//下拉框-航班状态
		final JComboBox FlightStateCombox = new JComboBox();
		FlightStateCombox.setFont(new Font("宋体", Font.PLAIN, 14));
		FlightStateCombox.setModel(new DefaultComboBoxModel(new String[] {
				f.getFlightStatus(), "UNPUBLISHED", "AVAILABLE", "FULL",
				"TERMINATE" }));

		FlightStateCombox.setBounds(90, 353, 116, 37);
		frame.getContentPane().add(FlightStateCombox);

		//按钮_修改
		JButton Modify = new JButton("确认修改");
		Modify.setBounds(160, 428, 153, 37);
		frame.getContentPane().add(Modify);
		Modify.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Flight f = new DbSelect().FlightSelect(Login.FlightId,AdminFunction.isDomestic);
				String name = IdtextField.getText();
				String st = STtextField.getText();
				String at = ATtextField_1.getText();
				String sc = StartCitycomboBox.getItemAt(
						StartCitycomboBox.getSelectedIndex()).toString();
				String ac = ArrCitycomboBox.getItemAt(
						ArrCitycomboBox.getSelectedIndex()).toString();
				String ca = Capp.getText();
				String price = PriceText.getText();
				String FS = FlightStateCombox.getItemAt(
						FlightStateCombox.getSelectedIndex()).toString();
				// 更新航班信息
				boolean x = new DbUpdate().FlightUpdate(Login.FlightId, st, at,
						sc, ac, st, Float.parseFloat(price),
						f.getCurrentPassengers(), Integer.parseInt(ca), FS,
						f.GetPassengerString(f.getPassengerId()), name,AdminFunction.isDomestic);
				if (x) {
					frame.setVisible(false);
					AdminFunction window = new AdminFunction();
					window.getAdminFrame().setVisible(true);
					AllDialog.Dialog(window.getAdminFrame(), "修改成功");
				} else {
					AllDialog.Dialog(frame, "修改失败");
				}
			}
		});
		// 检查航班状态是否为 TERMINATE
		if (f.getFlightStatus().equals("TERMINATE")) {
			IdtextField.disable();
			STtextField.disable();
			ATtextField_1.disable();
			Capp.disable();
			PriceText.disable();
			ArrCitycomboBox.disable();
			StartCitycomboBox.disable();
			FlightStateCombox.disable();
			Warning.setText("航班已终止，无法操作");
		}
		// 检查航班状态是否为 AVAILABLE/FULL
		if (f.getFlightStatus().equals("AVAILABLE") || f.getFlightStatus().equals("FULL")) {
			IdtextField.disable();
			STtextField.disable();
			ATtextField_1.disable();
			ArrCitycomboBox.disable();
			StartCitycomboBox.disable();
			Warning.setText("航班已发布，部分功能无法修改");
		}
		// 设置各个输入框的初始值
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		IdtextField.setText(f.getFlightName());
		STtextField.setText(f.getStartTime().format(formatter));
		ATtextField_1.setText(f.getArrivalTime().format(formatter));
		Capp.setText(Integer.toString(f.getSeatCapacity()));
		PriceText.setText(Float.toString(f.getPrice()));

		// 返回按钮
		JButton button = new JButton("返回");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				AdminFunction w = new AdminFunction();
				w.getAdminFrame().setVisible(true);
			}
		});
		button.setBounds(394, 428, 115, 37);
		frame.getContentPane().add(button);

		// 删除航班按钮
		JButton del = new JButton("删除航班");
		del.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Flight f=new DbSelect().FlightSelect(Login.FlightId,AdminFunction.isDomestic);
				if(f.getFlightStatus().equals("UNPUBLISHED")) // 检查状态
				{
				boolean x=new DbDelete().FlightDelete(Login.FlightId,AdminFunction.isDomestic);// 删除航班
				if(x)// 删除成功
				{
					frame.setVisible(false);
					AdminFunction window=new AdminFunction();
					window.getAdminFrame().setVisible(true);
					AllDialog.Dialog(window.getAdminFrame(), "删除成功");
					
				}
				else {
					AllDialog.Dialog(frame, "删除失败");
				}
				}
				else{
					AllDialog.Dialog(frame, "航班已发布，无法删除");
				}
			}
		});
		del.setBounds(619, 10, 93, 23);
		frame.getContentPane().add(del);

	}

	public Window EditFlightFrame() {
		return frame;
	}
}
