package frame;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.swing.*;
import javax.swing.table.TableColumn;

import flight.DbSelect;
import flight.PackageOrder;

public class PassengerOrder {

	private JFrame frame;
	private JTable table;
	private JScrollPane scrollPane;
	private int  food =0;
	String currentPackageStatus;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PassengerOrder window = new PassengerOrder();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public PassengerOrder() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("我的订单");
		frame.setBounds(250, 40, 950, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);


		//取消订单提示
		JLabel label = new JLabel("双击订单可以进入取消订单页面");
		label.setForeground(Color.RED);
		label.setBounds(26, 401, 301, 15);
		frame.getContentPane().add(label);

		//查询所有订单
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		String[] columnNames = {"ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "价格", "是否预定","座位号","餐食服务"};
		flight.Order[] order = new DbSelect().OrderSelect(Login.PassengerId,Research.isDomestic,"yes");
		String[][] o_ob2 =null;
		currentPackageStatus = Login.packagestatus;
		System.out.println("当前用户套餐状态" + Login.packagestatus);

		if(order!=null) {
			o_ob2 = new String[order.length][10];

			// 查询所有已购状态的套餐
			DbSelect s = new DbSelect();
			List<Map<String, Object>> packages1 = s.queryAllPackageStatus(Login.PassengerId);
			System.out.println("Login.PassengerId: " + Login.PassengerId);

			// 存储所有已购套餐的名称
			/*List<String> fullPackages = new ArrayList<>();
			for (Map<String, Object> packageInfo : packages1) {
				if ((boolean) packageInfo.get("IsFull")) {
					fullPackages.add((String) packageInfo.get("Package"));
				}
			}

			 */
			// 存储所有套餐的名称，不用判断 IsFull
			List<String> fullPackages = new ArrayList<>();
			for (Map<String, Object> packageInfo : packages1) {
				// 直接获取 "Package" 的值并加入列表
				fullPackages.add((String) packageInfo.get("Package"));
			}

// 输出所有已购套餐的状态
			System.out.println("所有套餐状态: " + fullPackages);


			// 将套餐列表转换为数组
			String[] fullPackageArray = fullPackages.toArray(new String[0]);
			System.out.println("Full Packages: " + Arrays.toString(fullPackageArray));

			/*

			if (order != null) {
				for (int i = 0; i < order.length; i++) {
					o_ob2[i][0] = Integer.toString(order[i].getId());
					o_ob2[i][1] = order[i].getFlightId().getFlightName();
					o_ob2[i][2] = order[i].getFlightId().getStartCity();
					o_ob2[i][3] = order[i].getFlightId().getArrivalCity();
					o_ob2[i][4] = order[i].getFlightId().getStartTime().format(formatter);
					o_ob2[i][5] = order[i].getFlightId().getArrivalTime().format(formatter);

					// 遍历已购套餐状态，尝试匹配套餐订单
					PackageOrder matchedPackageOrder = null;
					for (String currentPackageStatus : fullPackageArray) {
						System.out.println("当前检查的套餐状态: " + currentPackageStatus);
						PackageOrder po = s.PackageOrderSelect(Login.PassengerId, currentPackageStatus, order[i].getId());
						if (po != null) {
							matchedPackageOrder = po;
							break; // 如果找到匹配的套餐订单，退出循环
						}
					}

					// 判断是否为套餐订单
				    if(matchedPackageOrder.getId()==0){
						o_ob2[i][6] = String.valueOf(order[i].getFlightId().getPrice());
					 System.out.println(Login.PassengerId + "的订单" + order[i] + "不是套餐订单：");
					}else if (matchedPackageOrder != null) {
						o_ob2[i][6] = String.valueOf(matchedPackageOrder.getPrice());
						System.out.println(Login.PassengerId + "的订单" + o_ob2[i][0] + "是套餐订单：" + matchedPackageOrder.getId());
					}

					o_ob2[i][7] = "已预定";
					o_ob2[i][8] = order[i].getSeat();

					food = new DbSelect().QueryFoodByOrderId(order[i].getId());
					String selectfood;
					if(food == 1){
						selectfood = "已预定餐食-中餐";
					}else if(food == 2){
						selectfood = "已预定餐食-西餐";
					}else{
						selectfood = "未预定餐食";
					}

					o_ob2[i][9] = selectfood ;
				}

			}
		}else{
			o_ob2 = new String[1][9];
			label.setText("当前无订单!");
		}
		 */
			if (order != null) {
				// 遍历所有套餐状态
				for (String currentPackageStatus : fullPackageArray) {
					System.out.println("当前检查的套餐状态: " + currentPackageStatus);

					// 遍历所有订单，筛选出匹配当前套餐状态的订单
					for (int i = 0; i < order.length; i++) {
						PackageOrder matchedPackageOrder = s.PackageOrderSelect(Login.PassengerId, currentPackageStatus, order[i].getId());

						//判断是否为套餐订单
						if(matchedPackageOrder.getId()==0){
							// 如果未匹配当前套餐，则使用默认价格（仅在未被其他套餐处理过时）
							if (o_ob2[i][6] == null) { // 防止被多次覆盖
								o_ob2[i][6] = String.valueOf(order[i].getFlightId().getPrice());
								System.out.println(Login.PassengerId + "的订单" + order[i] + "未匹配到套餐 " + currentPackageStatus);
							}
						}else if (matchedPackageOrder != null) {
							// 设置套餐匹配价格
							o_ob2[i][6] = String.valueOf(matchedPackageOrder.getPrice());
							System.out.println(Login.PassengerId + "的订单" + o_ob2[i][0] + "匹配到套餐 " + currentPackageStatus + "，订单ID：" + matchedPackageOrder.getId());
						}
					}
				}

				// 设置其他信息（餐食服务等）
				for (int i = 0; i < order.length; i++) {
					o_ob2[i][0] = Integer.toString(order[i].getId());
					o_ob2[i][1] = order[i].getFlightId().getFlightName();
					o_ob2[i][2] = order[i].getFlightId().getStartCity();
					o_ob2[i][3] = order[i].getFlightId().getArrivalCity();
					o_ob2[i][4] = order[i].getFlightId().getStartTime().format(formatter);
					o_ob2[i][5] = order[i].getFlightId().getArrivalTime().format(formatter);
					o_ob2[i][7] = "已预定";
					o_ob2[i][8] = order[i].getSeat();

					// 查询餐食服务
					int food = new DbSelect().QueryFoodByOrderId(order[i].getId());
					String selectfood;
					if (food == 1) {
						selectfood = "已预定餐食-中餐";
					} else if (food == 2) {
						selectfood = "已预定餐食-西餐";
					} else {
						selectfood = "未预定餐食";
					}
					o_ob2[i][9] = selectfood;
				}
			} else {
				o_ob2 = new String[1][9];
				label.setText("当前无订单!");
			}

		}

			table = new JTable(o_ob2, columnNames) {
			private static final long serialVersionUID = -7902867902078729470L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
						int row = table.getSelectedRow();
						if (row != -1) {  // 确保选中了有效行
							// 显示弹出菜单
							JPopupMenu popupMenu = new JPopupMenu();
							// 创建第一个菜单项
							JMenuItem option1 = new JMenuItem("取消航班");
							option1.addActionListener(actionEvent -> {
								// 执行第一个选项的操作
								String flightname = table.getValueAt(row, 1).toString();
								String info = new DbSelect().queryTransit(Login.PassengerId,flightname);
								if(info.equals("未找到匹配的记录")){//直飞
									String preId1 = table.getValueAt(row, 0).toString();
									frame.setVisible(false);
									Login.OrderId = Integer.parseInt(preId1);
									CancelOrder window = new CancelOrder();
									window.getFrame().setVisible(true);
								}
								else{
									String preId1 = table.getValueAt(row, 0).toString();
									frame.setVisible(false);
									Login.OrderId = Integer.parseInt(preId1);
									TransitWarn window = new TransitWarn(info,flightname);//先提醒
									window.getFrame().setVisible(true);
								}

							});
							popupMenu.add(option1);

							// 创建第二个菜单项
							JMenuItem option2 = new JMenuItem("改签航班");
							option2.addActionListener(actionEvent -> {
								System.out.println("选择改签航班");
								String flightname = table.getValueAt(row, 1).toString();
								String info = new DbSelect().queryTransit(Login.PassengerId,flightname);
								if(info.equals("未找到匹配的记录")){//直飞
									String preId1 = table.getValueAt(row, 0).toString();
									frame.setVisible(false);
									Login.OrderId = Integer.parseInt(preId1);
									RescheduleSearch window = new RescheduleSearch(info,flightname,true);//直接查询
									System.out.println("直飞航班改签");
									window.getFrame().setVisible(true);
								}
								else{
									String preId1 = table.getValueAt(row, 0).toString();
									frame.setVisible(false);
									Login.OrderId = Integer.parseInt(preId1);
									Reschedule window = new Reschedule(info,flightname);//先提醒，后查询
									System.out.println("中转航班改签");
									window.getFrame().setVisible(true);
								}
							});
							popupMenu.add(option2);


							// 创建第三个菜单项
							JMenuItem option3 = new JMenuItem("订餐");
							option3.addActionListener(actionEvent -> {
								String preId1 = table.getValueAt(row, 0).toString();
								Login.OrderId = Integer.parseInt(preId1);
								System.out.println("选择餐食");
								String starttime = table.getValueAt(row, 4).toString();
								// 定义时间格式，匹配 starttime 的格式
								DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
								// 将字符串解析为 LocalDateTime
								LocalDateTime startTimeParsed = LocalDateTime.parse(starttime, formatter);
								// 获取当前时间
								LocalDateTime now = LocalDateTime.now();
								// 比较时间
								String foodstasua = table.getValueAt(row, 9).toString();
								if(!Objects.equals(foodstasua, "未预定餐食")){
									AllDialog.Dialog(frame, "您已预定餐食，无法重复预定");
								}
								else if (startTimeParsed.isAfter(now)) {
									System.out.println("starttime 在当前时间之后");
									frame.setVisible(false);
									Food window = new Food();
									window.getFrame().setVisible(true);
								}else {
									System.out.println("starttime 在当前时间之前");
									System.out.println("starttime 等于当前时间");
									AllDialog.Dialog(frame, "航班已结束，无法预定餐食");
								}



							});
							popupMenu.add(option3);

							// 在鼠标位置弹出菜单
							popupMenu.show(e.getComponent(), e.getX(), e.getY());
						}
					}
				}



		});
		table.setBounds(44, 129, 823, 396);
		TableColumn column = null;
		int colunms = table.getColumnCount();
		for (int i = 0; i < colunms; i++) {
			column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth(100);
		}
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setSelectionBackground(Color.LIGHT_GRAY);
		table.setSelectionForeground(Color.yellow);
		table.setBounds(21, 143, 822, 363);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 24, 898, 367);

		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(table);

		JButton button_2 = new JButton("返回");
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				Research wResearch = new Research();
				wResearch.getFrame().setVisible(true);
			}
		});
		button_2.setFont(new Font("宋体", Font.PLAIN, 20));
		button_2.setBounds(376, 462, 189, 49);
		frame.getContentPane().add(button_2);



	}

	public JFrame getFrame() {
		return this.frame;
	}
}
