package frame;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import flight.DbSelect;

public class PassengerOrder {

	private JFrame frame;
	private JTable table;
	private JScrollPane scrollPane;

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

		/*
		//按钮_退出登录
		JButton btnNewButton_1 = new JButton("退出登录");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				Login window = new Login();
				window.getFrame().setVisible(true);
			}
		});
		btnNewButton_1.setFont(new Font("宋体", Font.PLAIN, 40));
		btnNewButton_1.setBounds(1611, 0, 361, 49);
		frame.getContentPane().add(btnNewButton_1);


		JButton button = new JButton("搜索");
		button.setFont(new Font("宋体", Font.PLAIN, 40));
		button.setBounds(1717, 83, 173, 41);
		frame.getContentPane().add(button);

		JLabel lblNewLabel_8 = new JLabel("价格");
		lblNewLabel_8.setFont(new Font("宋体", Font.PLAIN, 40));
		lblNewLabel_8.setBounds(1423, 205, 92, 44);
		frame.getContentPane().add(lblNewLabel_8);

		JLabel lblNewLabel_9 = new JLabel("航班状态");
		lblNewLabel_9.setFont(new Font("宋体", Font.PLAIN, 40));
		lblNewLabel_9.setBounds(1611, 205, 169, 44);
		frame.getContentPane().add(lblNewLabel_9);

		 */

		//取消订单提示
		JLabel label = new JLabel("双击订单可以进入取消订单页面");
		label.setForeground(Color.RED);
		label.setBounds(26, 401, 301, 15);
		frame.getContentPane().add(label);

		//查询所有订单
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		String[] columnNames = {"ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "价格", "是否预定"};
		flight.Order[] order = new DbSelect().OrderSelect(Login.PassengerId,Research.isDomestic,"yes");
		String[][] o_ob2 =null;
		if(order!=null)
		{
			o_ob2 = new String[order.length][8];
			if (order != null) {
				for (int i = 0; i < order.length; i++) {
					o_ob2[i][0] = Integer.toString(order[i].getId());
					o_ob2[i][1] = Integer.toString(order[i].getSeat());
					o_ob2[i][2] = order[i].getFlightId().getStartCity();
					o_ob2[i][3] = order[i].getFlightId().getArrivalCity();
					o_ob2[i][4] = order[i].getFlightId().getStartTime().format(formatter);
					o_ob2[i][5] = order[i].getFlightId().getArrivalTime().format(formatter);
					o_ob2[i][6] = String.valueOf(order[i].getFlightId().getPrice());
					o_ob2[i][7] = "已预定";
				}

			}
		}else{
			o_ob2 = new String[1][8];
			label.setText("当前无订单!");
		}
		table = new JTable(o_ob2, columnNames) {
			private static final long serialVersionUID = -7902867902078729470L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {// 检测双击事件
					// 获取选中的行
					int row = table.getSelectedRow();
					String preId1 = table.getValueAt(row, 0).toString();
					frame.setVisible(false);
					Login.OrderId = Integer.parseInt(preId1);
					CancelOrder window = new CancelOrder();
					window.getFrame().setVisible(true);
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
