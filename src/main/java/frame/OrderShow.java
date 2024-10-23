package frame;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import flight.DateTime;
import flight.DbSelect;
import flight.Order;

public class OrderShow {

	private JFrame frame;
	private JTable Order_Table;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OrderShow window = new OrderShow();
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
	public OrderShow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("订单一览");
		frame.setBounds(100, 100, 594, 487);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		String[] columnNames = { "订单ID", "乘客", "剩余座位", "航班ID", "时间", "航班状态"};
		Order[] orders = new DbSelect().OrderSelect();
		String[][] order_ob = new String[orders.length][6];
		for (int i = 0; i < orders.length; i++) {
			order_ob[i][0] = Integer.toString(orders[i].getId());
			order_ob[i][1] = orders[i].getPassengerId().getRealName();
			order_ob[i][2] = Integer.toString(orders[i].getSeat());
			order_ob[i][3] = orders[i].getFlightId().getFlightName();
			order_ob[i][4] = DateTime.GetDateTimeStr(orders[i].getCreateDate());
			order_ob[i][5] = orders[i].getStatus();
			// System.out.println(flights[i].getFlightStatus());
		}
		Order_Table = new JTable(order_ob, columnNames) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -9518924674449654L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}// ��������༭
		};
		TableColumn column = null;
		int colunms = Order_Table.getColumnCount();
		for (int i = 0; i < colunms; i++) {
			column = Order_Table.getColumnModel().getColumn(i);
			column.setPreferredWidth(100);
		}
		Order_Table.getColumnModel().getColumn(0).setPreferredWidth(20);
		Order_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Order_Table.setSelectionBackground(Color.LIGHT_GRAY);
		Order_Table.setSelectionForeground(Color.yellow);
		Order_Table.setBounds(33, 34, 512, 334);

		JButton button = new JButton("返回");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.setVisible(false);
				AdminFunction w = new AdminFunction();
				w.getAdminFrame().setVisible(true);
			}
		});
		button.setFont(new Font("宋体", Font.PLAIN, 32));
		button.setBounds(185, 385, 176, 54);
		frame.getContentPane().add(button);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(33, 34, 512, 334);

		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(Order_Table);
	}

	public JFrame getFrame() {
		return frame;
	}
}
