//航班查询界面，用户可以选择出发城市、到达城市、起飞时间、到达时间等条件来查询航班信息，
// 并显示在表格中。用户可以通过点击表格中的航班条目进行预定
package frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import flight.*;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class Research {

	private JFrame frame;
	private JTable Flight_Table;
	private JScrollPane scrollPane;
	static boolean isDomestic =true;
	private Flight[] currentFlights; // 新增：用于保存当前表格中显示的航班数据

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




		//标签_起飞城市
		JLabel label = new JLabel("起飞城市");
		//label.setBounds(79, 21, 60, 29);
		label.setBounds(233, 21, 60, 29);
		frame.getContentPane().add(label);

		//下拉框-起飞城市
		final JComboBox startCity = new JComboBox();
		/*
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

		 */
		startCity.setToolTipText("");
		startCity.setBounds(208, 62, 127, 37);
		frame.getContentPane().add(startCity);

		//标签-降落城市
		JLabel label_1 = new JLabel("降落城市");
		label_1.setBounds(391, 14, 92, 42);
		frame.getContentPane().add(label_1);

		//下拉框-降落城市
		final JComboBox<String> arrivalCity = new JComboBox<>();
		updateFlightCities(startCity, arrivalCity, isDomestic);
		arrivalCity.setToolTipText("");
		arrivalCity.setBounds(366, 62, 127, 38);
		frame.getContentPane().add(arrivalCity);

		//按钮_国内航班
		JButton button_00 = new JButton("国内航班");
		button_00.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isDomestic =true;
				//updateArrivalCity(arrivalCity, isDomestic);
				updateFlightCities(startCity, arrivalCity, isDomestic);
				setTable(frame);

			}
		});
		button_00.setBounds(79, 21, 87, 42);
		frame.getContentPane().add(button_00);

		//按钮_国外航班
		JButton button_01 = new JButton("国外航班");
		button_01.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isDomestic =false;
				//updateArrivalCity(arrivalCity, isDomestic);
				updateFlightCities(startCity, arrivalCity, isDomestic);
				setTable(frame);
			}
		});
		button_01.setBounds(79, 70, 87, 42);
		frame.getContentPane().add(button_01);


		//按钮_交换
		JButton Exchange = new JButton("交换");
		Exchange.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
					String s1 = startCity.getItemAt(startCity.getSelectedIndex()).toString();
					String s2 = arrivalCity.getItemAt(arrivalCity.getSelectedIndex()).toString();
				if (isDomestic) {
					// 国内航班：直接交换选中项
					startCity.setSelectedItem(s2);
					arrivalCity.setSelectedItem(s1);
				} else {
					// 国外航班：交换选项列表和选中值
					String[] tempOptions = getCityOptions(arrivalCity);//临时列表获得到达城市的列表
					updateComboBox(arrivalCity, getCityOptions(startCity), s1); // 设置 arrivalCity 为 startCity 的选项
					updateComboBox(startCity, tempOptions, s2); // 设置 startCity 为 arrivalCity 的选项

				}
			}
		});
		Exchange.setFont(new Font("宋体", Font.PLAIN, 12));
		Exchange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		Exchange.setBounds(320, 100, 63, 37);
		frame.getContentPane().add(Exchange);

		//标签_起飞时间
		JLabel StartTimeLabel = new JLabel("起飞时间");
		StartTimeLabel.setBounds(546, 28, 54, 15);
		frame.getContentPane().add(StartTimeLabel);

		//日期选择器
		final DateChooser dateChooser = new DateChooser(frame.getContentPane(),
				100);
		dateChooser.setBounds(521, 66, 126, 42);
		frame.getContentPane().add(dateChooser);





		//查询按钮
		JButton search = new JButton("查询");
		search.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String s1 = startCity.getItemAt(startCity.getSelectedIndex())
						.toString();
				String s2 = arrivalCity.getItemAt(
						arrivalCity.getSelectedIndex()).toString();
				//用户选择的日期
				String date1 = dateChooser.getText();
				//String date2 = dateChooser2.getText();
				DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				// 将日期字符串转换为 LocalDate
				LocalDate localDate = LocalDate.parse(date1, dateFormatter);
				// 将 LocalDate 转换为 LocalDateTime 的当天开始时间
				LocalDateTime startDate = LocalDateTime.of(localDate, LocalTime.MIN);  // 2024-11-30T00:00
				// 创建 FlightProcessor 对象
				FlightProcessor processor = new FlightProcessor();
				// 先获得所有航班信息
				currentFlights = new DbSelect().FlightSelectForPass(isDomestic);
				// 处理航班数据，查找从 "北京" 到 "深圳" 的路径
				Flight[] processedFlights = processor.processFlights(currentFlights, s1, s2,startDate);
				if (processedFlights != null) {
					setTable(frame, processedFlights,s1,s2);
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


		//当前套餐标签
		JLabel label_3 = new JLabel("当前套餐:");
		label_3.setForeground(Color.BLACK);
		label_3.setBounds(650, 28, 54, 15);
		frame.getContentPane().add(label_3);
		label_3.setForeground(new Color(20, 149, 231));

		// 创建一个 JLabel 作为链接
		DbSelect sa = new DbSelect();
		Login.packagestatus = sa.queryPackageStatus(Login.PassengerId);
		System.out.println("查询结果packagestatus"+Login.packagestatus);
		JLabel lblLink = new JLabel("<html><a href=''>"+Login.packagestatus+"</a></html>");
		lblLink.setBounds(720, 15, 87, 42); // 设置位置和大小
		lblLink.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 鼠标悬停时显示手型光标
		lblLink.setToolTipText("了解更多");
		lblLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false); // 隐藏当前界面
				FlightRecommendation window = new FlightRecommendation(); // 创建航班推荐界面
				window.getFrame().setVisible(true); // 显示航班推荐界面
			}
		});

// 将 JLabel 添加到窗口中
		frame.getContentPane().add(lblLink);




		//我的订单界面
		JButton btnNewButton = new JButton("我的订单");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				PassengerOrder window = new PassengerOrder();
				window.getFrame().setVisible(true);
			}
		});
		btnNewButton.setBounds(837, 7, 87, 42);
		frame.getContentPane().add(btnNewButton);


		//按钮_刷新列表
		JButton fresh = new JButton("刷新列表");
		fresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setTable(frame);
			}
		});
		fresh.setBounds(837, 53, 87, 42);
		frame.getContentPane().add(fresh);

		//按钮_飞行记录
		JButton map = new JButton("飞行记录");
		map.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				flight.Order[] order = new DbSelect().OrderSelect(Login.PassengerId,Research.isDomestic,"yes");
				List<Order> orderList = Arrays.asList(order);
				try {
					new FlightMapGenerator().generateMap(orderList);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
				//frame.setVisible(false);setTable(frame);
			}
		});
		map.setBounds(837, 100, 87, 42);
		frame.getContentPane().add(map);

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

		setTable(frame);
	}



		// 创建表格_by frame 初始化/刷新
		private void setTable(final JFrame frame) {
			if (scrollPane != null) {
				frame.getContentPane().remove(scrollPane); // 清除旧的表格
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
			System.out.println("初始化生成表格判断packagestatus"+Login.packagestatus);
			String[] columnNames={};
			String[][] flight_ob = new String[0][0];
			// 更新当前表格的航班数据源
			currentFlights = new DbSelect().FlightSelectForPass(isDomestic); // 更新 currentFlights
			System.out.println(isDomestic?"国内":"国外");

			// 按起飞时间排序，从早到晚
			Arrays.sort(currentFlights, Comparator.comparing(Flight::getStartTime));
			if((Objects.equals(Login.packagestatus, "国内随心飞")&&isDomestic)||
					(Objects.equals(Login.packagestatus, "学生寒暑假")&&isDomestic)||
					(Objects.equals(Login.packagestatus, "国外随心飞")&&!isDomestic)){
				columnNames = new String[]{"ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "原价", "特惠价", "是否预定", "模式", "航班状态"};
				// 将航班信息填充到 flight_ob 二维数组中
				flight_ob = new String[currentFlights.length][11];
				for (int i = 0; i < currentFlights.length; i++) {
					flight_ob[i][0] = Integer.toString(currentFlights[i].getId());
					flight_ob[i][1] = currentFlights[i].getFlightName();
					flight_ob[i][2] = currentFlights[i].getStartCity();
					flight_ob[i][3] = currentFlights[i].getArrivalCity();
					flight_ob[i][4] = currentFlights[i].getStartTime().format(formatter);
					flight_ob[i][5] = currentFlights[i].getArrivalTime().format(formatter);
					flight_ob[i][6] = String.valueOf(currentFlights[i].getPrice());
					flight_ob[i][7] = String.format("%.1f", PackageOrder.discountPrice(currentFlights[i].getPrice()));
					//System.out.println(flight_ob[i][7]);
					// 检查当前登录的乘客是否已经预定该航班
					if (Order.IsHasOrder(Login.PassengerId, currentFlights[i].getId(), isDomestic)) {
						flight_ob[i][8] = "未预定";
					} else {
						flight_ob[i][8] = "已预定";
					}
					flight_ob[i][9] = "直飞";
					flight_ob[i][10] = currentFlights[i].getFlightStatus();
				}

			}
			else{
				columnNames = new String[]{"ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "价格", "是否预定", "模式", "航班状态"};

				// 将航班信息填充到 flight_ob 二维数组中
				flight_ob = new String[currentFlights.length][10];
				for (int i = 0; i < currentFlights.length; i++) {
					flight_ob[i][0] = Integer.toString(currentFlights[i].getId());
					flight_ob[i][1] = currentFlights[i].getFlightName();
					flight_ob[i][2] = currentFlights[i].getStartCity();
					flight_ob[i][3] = currentFlights[i].getArrivalCity();
					flight_ob[i][4] = currentFlights[i].getStartTime().format(formatter);
					flight_ob[i][5] = currentFlights[i].getArrivalTime().format(formatter);
					flight_ob[i][6] = String.valueOf(currentFlights[i].getPrice());
					// 检查当前登录的乘客是否已经预定该航班
					if (Order.IsHasOrder(Login.PassengerId, currentFlights[i].getId(), isDomestic)) {
						flight_ob[i][7] = "未预定";
					} else {
						flight_ob[i][7] = "已预定";
					}
					flight_ob[i][8] = "直飞";
					flight_ob[i][9] = currentFlights[i].getFlightStatus();
				}
			}

			// 创建 JTable 表格，显示航班数据，并设置表格不可编辑。
			Flight_Table = new JTable(flight_ob, columnNames) {
				@Serial
				private static final long serialVersionUID = -4737302915707325665L;

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};

			// 设置表格列的宽度和其他属性
			TableColumn column;
			int columns = Flight_Table.getColumnCount();
			for (int i = 0; i < columns; i++) {
				column = Flight_Table.getColumnModel().getColumn(i);
				column.setPreferredWidth(100);
			}
			Flight_Table.getColumnModel().getColumn(0).setPreferredWidth(20); // 第一列
			Flight_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 单选模式
			Flight_Table.setSelectionBackground(Color.LIGHT_GRAY); // 选中后背景色
			Flight_Table.setSelectionForeground(Color.yellow); // 选中后前景色
			Flight_Table.setBounds(21, 143, 700, 363);
			// 为第 6 列设置黑色文字 + 红色删除线的渲染器
			if((Objects.equals(Login.packagestatus, "国内随心飞")&&isDomestic)||
					(Objects.equals(Login.packagestatus, "学生寒暑假")&&isDomestic)||
					(Objects.equals(Login.packagestatus, "国外随心飞")&&!isDomestic))
			Research.setThickRedStrikeThroughRenderer(Flight_Table, 6);


			// 添加滚动面板
			scrollPane = new JScrollPane();
			scrollPane.setBounds(12, 143, 912, 363);
			frame.getContentPane().add(scrollPane);
			scrollPane.setViewportView(Flight_Table);

			// 鼠标监听事件
			Flight_Table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					// 双击事件
					if (e.getClickCount() == 2) {
						int row = Flight_Table.getSelectedRow();
						if (row != -1) {
							// 使用 currentFlights 数组而不是原来的数据源
							Flight selectedFlight = currentFlights[row];
							frame.setVisible(false);
							Login.FlightId = selectedFlight.getId();
							ReserveFlight window = new ReserveFlight(isDomestic);
							window.getFrame().setVisible(true);
						}
					}
				}
			});
		}




	//创建表格_by frame 精准查询
	private void setTable(final JFrame frame, Flight[] flights,String startcity,String arrivalcity) {
		if (scrollPane != null) {
			frame.getContentPane().remove(scrollPane); // 清除旧的表格
		}

		System.out.println("精准查询生成表格判断packagestatus"+Login.packagestatus);
		String[] columnNames={};
		String[][] flight_ob = new String[0][0];
		// 更新当前表格的航班数据源

		if((Objects.equals(Login.packagestatus, "国内随心飞")&&isDomestic)||
				(Objects.equals(Login.packagestatus, "学生寒暑假")&&isDomestic)||
				(Objects.equals(Login.packagestatus, "国外随心飞")&&!isDomestic)) {
			columnNames = new String[]{"ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "原价", "特惠价", "是否预定", "模式", "航班状态"};
			flight_ob = new String[flights.length][11];
			for (int i = 0; i < flights.length; i++) {
				flight_ob[i][0] = Integer.toString(flights[i].getId());
				flight_ob[i][1] = flights[i].getFlightName();
				flight_ob[i][2] = flights[i].getStartCity();
				flight_ob[i][3] = flights[i].getArrivalCity();
				flight_ob[i][4] = (flights[i].getStartTime() != null) ?
						flights[i].getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) :
						"";
				flight_ob[i][5] = (flights[i].getArrivalTime() != null) ?
						flights[i].getArrivalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) :
						"";
				flight_ob[i][6] = (flights[i].getPrice() == 0.0f) ? "" : String.valueOf(flights[i].getPrice());
				flight_ob[i][7] = (flights[i].getPrice() == 0.0f) ? "" : String.format("%.1f", PackageOrder.discountPrice(flights[i].getPrice()));
				//是否预定栏
				if (flights[i].getStartTime() == null) {
					flight_ob[i][8] = "";
				} else if (Order.IsHasOrder(Login.PassengerId, flights[i].getId(), isDomestic) && flights[i].getStartCity() != null) {
					flight_ob[i][8] = "未预定";
				} else {
					flight_ob[i][8] = "已预定";
				}
				//直飞中转栏
				if (flights[i].getStartTime() == null) {
					flight_ob[i][9] = "";
				} else if (Objects.equals(flights[i].getStartCity(), startcity) && Objects.equals(flights[i].getArrivalCity(), arrivalcity)) {
					flight_ob[i][9] = "直飞";
				} else
					flight_ob[i][9] = "中转";

				if (flights[i].getStartTime() == null) {
					flight_ob[i][10] = "";
				} else {
					flight_ob[i][10] = flights[i].getFlightStatus();
				}

			}
		}
		else{
			columnNames = new String[]{"ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "价格", "是否预定", "模式", "航班状态"};
			flight_ob = new String[flights.length][10];
			for (int i = 0; i < flights.length; i++) {
				flight_ob[i][0] = Integer.toString(flights[i].getId());
				flight_ob[i][1] = flights[i].getFlightName();
				flight_ob[i][2] = flights[i].getStartCity();
				flight_ob[i][3] = flights[i].getArrivalCity();
				flight_ob[i][4] = (flights[i].getStartTime() != null) ?
						flights[i].getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) :
						"";
				flight_ob[i][5] = (flights[i].getArrivalTime() != null) ?
						flights[i].getArrivalTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) :
						"";
				flight_ob[i][6] = (flights[i].getPrice() == 0.0f) ? "" : String.valueOf(flights[i].getPrice());
				//是否预定栏
				if (flights[i].getStartTime() == null) {
					flight_ob[i][7] = "";
				} else if (Order.IsHasOrder(Login.PassengerId, flights[i].getId(), isDomestic) && flights[i].getStartCity() != null) {
					flight_ob[i][7] = "未预定";
				} else {
					flight_ob[i][7] = "已预定";
				}
				//直飞中转栏
				if (flights[i].getStartTime() == null) {
					flight_ob[i][8] = "";
				} else if (Objects.equals(flights[i].getStartCity(), startcity) && Objects.equals(flights[i].getArrivalCity(), arrivalcity)) {
					flight_ob[i][8] = "直飞";
				} else
					flight_ob[i][8] = "中转";

				if (flights[i].getStartTime() == null) {
					flight_ob[i][9] = "";
				} else {
					flight_ob[i][9] = flights[i].getFlightStatus();
				}

			}
		}

		Flight_Table = new JTable(flight_ob, columnNames) {
			private static final long serialVersionUID = -5723427406160453043L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		TableColumn column ;
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

		// 为第 6 列设置黑色文字 + 红色删除线的渲染器
		if((Objects.equals(Login.packagestatus, "国内随心飞")&&isDomestic)||
				(Objects.equals(Login.packagestatus, "学生寒暑假")&&isDomestic)||
				(Objects.equals(Login.packagestatus, "国外随心飞")&&!isDomestic))
			Research.setThickRedStrikeThroughRenderer(Flight_Table, 6);


		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 143, 912, 363);

		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(Flight_Table);
		Flight_Table.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = Flight_Table.getSelectedRow();
					if (row == -1) return;

					//获取航班信息
					String flightId = Flight_Table.getValueAt(row, 0).toString();

					String transferFlag ="";
					if((Objects.equals(Login.packagestatus, "国内随心飞")&&isDomestic)||
							(Objects.equals(Login.packagestatus, "学生寒暑假")&&isDomestic)||
							(Objects.equals(Login.packagestatus, "国外随心飞")&&!isDomestic)){
						transferFlag = Flight_Table.getValueAt(row, 9).toString();
						System.out.println("中转，无套餐，取第9列");
					}
					else{
						transferFlag = Flight_Table.getValueAt(row, 8).toString();
						System.out.println("中转，无套餐，取第8列");
					}
					//String transferFlag = Flight_Table.getValueAt(row, 8).toString();

					String startname = Flight_Table.getValueAt(row, 1).toString();
					String startLocation = Flight_Table.getValueAt(row, 2).toString();
					String endLocation = Flight_Table.getValueAt(row, 3).toString();

					frame.setVisible(false);
					Login.FlightId = Integer.parseInt(flightId);
					ReserveFlight window = new ReserveFlight(isDomestic);
					window.getFrame().setVisible(true);


					if ("中转".equals(transferFlag)) {
						//初始化中转航班列表
						List<String> routeList = new ArrayList<>();
						routeList.add(startname);//添加起始航班号
						routeList.add(startLocation);//添加起始地址

						int[] rowWrapper = {row + 1};//选中列表序数
						List<ReserveFlight> transitWindows = new ArrayList<>();
						Timer timer = new Timer(10000, null);//10秒检查一次

						//计时器任务
						timer.addActionListener(actionEvent -> {
							if (rowWrapper[0] < Flight_Table.getRowCount()) {//有剩余航班
								//检查是否为中转
								String nextTransferFlag =" ";
								if((Objects.equals(Login.packagestatus, "国内随心飞")&&isDomestic)||
										(Objects.equals(Login.packagestatus, "学生寒暑假")&&isDomestic)||
										(Objects.equals(Login.packagestatus, "国外随心飞")&&!isDomestic)){
									nextTransferFlag = Flight_Table.getValueAt(rowWrapper[0], 9).toString();
									System.out.println("中转，无套餐，取第9列");
								}
								else{
									nextTransferFlag = Flight_Table.getValueAt(rowWrapper[0], 8).toString();
									System.out.println("中转，无套餐，取第8列");
								}
								String nextEndLocation = Flight_Table.getValueAt(rowWrapper[0], 3).toString();

								//不是中转
								if ("".equals(nextTransferFlag)) {

									Login.transferFlightsMap.put(Integer.parseInt(flightId), routeList);
									timer.stop();

									// 输出 transferFlightsMap
									System.out.println("空空最终的中转航班路线:");
									Login.transferFlightsMap.forEach((key, value) -> System.out.println("航班ID: " + key + " 路线: " + value));
									boolean transit = new DbInsert().insertTransitData(Login.transferFlightsMap,Login.PassengerId);
									System.out.println("生成中转航空"+transit);
								} else {
									String nextFlightId = Flight_Table.getValueAt(rowWrapper[0], 0).toString();//下一个航班ID更新
									String transferLocation = Flight_Table.getValueAt(rowWrapper[0], 2).toString();//中转地址
									String arriavlename = Flight_Table.getValueAt(rowWrapper[0], 1).toString();//下一个航班名
									routeList.add(transferLocation);
									routeList.add(nextEndLocation);//添加终点地址
									routeList.add(arriavlename);

									//更新为下一个中转航班
									Login.FlightId = Integer.parseInt(nextFlightId);
									ReserveFlight nextWindow = new ReserveFlight(isDomestic);
									nextWindow.getFrame().setVisible(true);
									transitWindows.add(nextWindow);

									rowWrapper[0]++;
								}
							} else {
								if (!routeList.contains(endLocation)) {
									routeList.add(endLocation);
								}
								Login.transferFlightsMap.put(Integer.parseInt(flightId), routeList);
								timer.stop();

								// 输出 transferFlightsMap
								System.out.println("最终的中转航班路线:");
								Login.transferFlightsMap.forEach((key, value) -> System.out.println("航班ID: " + key + " 路线: " + value));

							}
						});
						timer.setRepeats(true);
						timer.start();

					}

				}

				// 在停止计时器后输出 transferFlightsMap
				System.out.println("最终的中转航班路线:");
				Login.transferFlightsMap.forEach((key, value) -> System.out.println("航班ID: " + key + " 路线: " + value));
			}


		});
	}

	private String[] getCityOptions(JComboBox<String> comboBox) {
		int itemCount = comboBox.getItemCount();
		String[] options = new String[itemCount];
		for (int i = 0; i < itemCount; i++) {
			options[i] = comboBox.getItemAt(i);
		}
		return options;
	}

	private void updateComboBox(JComboBox<String> comboBox, String[] newOptions, String selectedValue) {
		comboBox.setModel(new DefaultComboBoxModel<>(newOptions));
		comboBox.setSelectedItem(selectedValue);
		comboBox.revalidate();
		comboBox.repaint();
	}



	private void updateFlightCities(JComboBox<String> startCity, JComboBox<String> arrivalCity, boolean isDomestic) {
		String[] domesticCities = {
				"北京", "上海", "天津", "重庆", "哈尔滨", "长春", "沈阳",
				"呼和浩特", "石家庄", "乌鲁木齐", "兰州", "西宁", "西安",
				"银川", "郑州", "济南", "太原", "合肥", "长沙", "武汉",
				"南京", "成都", "贵阳", "昆明", "南宁", "拉萨", "杭州",
				"南昌", "广州", "福州", "台北", "海口", "香港", "澳门", "深圳"
		};

		String[] internationalCitiesStart = {
				"北京", "上海", "天津", "重庆", "哈尔滨", "长春", "沈阳",
				"呼和浩特", "石家庄", "乌鲁木齐", "兰州", "西宁", "西安",
				"银川", "郑州", "济南", "太原", "合肥", "长沙", "武汉",
				"南京", "成都", "贵阳", "昆明", "南宁", "拉萨", "杭州",
				"南昌", "广州", "福州", "台北", "海口", "香港", "澳门", "深圳"
		};

		String[] internationalCitiesArrival = {

				"纽约", "伦敦", "巴黎", "柏林", "阿姆斯特丹", "慕尼黑",
				"罗马", "东京", "首尔", "曼谷", "悉尼", "奥克兰",
				"温哥华", "莫斯科", "芝加哥", "洛杉矶", "新加坡", "旧金山"
		};

		if (isDomestic) {
			startCity.setModel(new DefaultComboBoxModel<>(domesticCities));
			arrivalCity.setModel(new DefaultComboBoxModel<>(domesticCities));
		} else {
			startCity.setModel(new DefaultComboBoxModel<>(internationalCitiesStart));
			arrivalCity.setModel(new DefaultComboBoxModel<>(internationalCitiesArrival));
		}

		startCity.setSelectedIndex(0);
		arrivalCity.setSelectedIndex(0);
	}

	//套餐原价红色删除线
	public static void setThickRedStrikeThroughRenderer(JTable table, int columnIndex) {
		table.getColumnModel().getColumn(columnIndex).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				// 获取当前文字的宽高
				FontMetrics fm = g.getFontMetrics();
				int textWidth = fm.stringWidth(getText());
				int textHeight = fm.getHeight();

				// 设置删除线颜色为红色
				g.setColor(Color.RED);

				// 设置粗删除线的粗细
				Graphics2D g2d = (Graphics2D) g;
				g2d.setStroke(new BasicStroke(1)); // 删除线粗细：2px

				// 绘制删除线（位置：文字中间）
				int y = getHeight() / 2; // 水平线位置
				g2d.drawLine(0, y, textWidth, y);
			}

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				// 设置字体颜色为黑色
				label.setForeground(Color.BLACK);

				return label;
			}
		});
	}

}
