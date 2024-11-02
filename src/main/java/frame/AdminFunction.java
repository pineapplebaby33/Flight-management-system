package frame;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import flight.*;
//import com.flight.java.DateTime;
//import com.flight.java.DbSelect;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.JRadioButton;

public class AdminFunction {
    private JFrame AdminFrame;
    private JTable Flight_Table;
    private JScrollPane scrollPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AdminFunction window = new AdminFunction();
                    window.AdminFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AdminFunction() {
        initialize();
    }

    public JFrame getAdminFrame() {
        return this.AdminFrame;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void initialize() {
        AdminFrame = new JFrame();
        AdminFrame.setTitle("管理页面");
        AdminFrame.setResizable(false);
        AdminFrame.setBounds(100, 100, 859, 666);
        AdminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        AdminFrame.getContentPane().setLayout(null);

        JButton addAdmin = new JButton("添加管理员");
        addAdmin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                AdminFrame.setVisible(false);
                AddAdmin window = new AddAdmin();
                window.getFrame().setVisible(true);
            }
        });
        addAdmin.setBounds(271, 0, 153, 37);
        AdminFrame.getContentPane().add(addAdmin);
        /*
        final JRadioButton CheckDateRadio = new JRadioButton(
                "不选择日期搜索");
        CheckDateRadio.setSelected(true);
        CheckDateRadio.setBounds(691, 63, 121, 23);
        AdminFrame.getContentPane().add(CheckDateRadio);
        */
        JButton ModifyP = new JButton("修改个人信息");
        ModifyP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdminFrame.setVisible(false);
                EditAdmin window = new EditAdmin();
                window.getFrame().setVisible(true);
            }
        });
        ModifyP.setBounds(448, 0, 177, 37);
        AdminFrame.getContentPane().add(ModifyP);

        JButton Out = new JButton("退出登录");
        Out.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Login.AdminId = 0;
                AdminFrame.setVisible(false);
                Login window = new Login();
                window.getFrame().setVisible(true);
            }
        });
        Out.setBounds(665, 0, 147, 37);
        AdminFrame.getContentPane().add(Out);

        final JComboBox startCity = new JComboBox();
        startCity.setModel(new DefaultComboBoxModel(new String[] { " ",
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
        startCity.setToolTipText("");
        startCity.setBounds(21, 96, 127, 37);
        AdminFrame.getContentPane().add(startCity);

        final JComboBox arrivalCity = new JComboBox();
        arrivalCity.setModel(new DefaultComboBoxModel(new String[] { " ",
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
        arrivalCity.setBounds(228, 96, 127, 37);
        AdminFrame.getContentPane().add(arrivalCity);

        JLabel label = new JLabel("起始城市");
        label.setBounds(34, 60, 108, 29);
        AdminFrame.getContentPane().add(label);

        JLabel label_1 = new JLabel("降落城市");
        label_1.setBounds(238, 60, 108, 29);
        AdminFrame.getContentPane().add(label_1);

        JButton Exchange = new JButton("交换");
        Exchange.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                // ��ȡJComboBox��ֵ
                String s1 = startCity.getItemAt(startCity.getSelectedIndex())
                        .toString();
                String s2 = arrivalCity.getItemAt(
                        arrivalCity.getSelectedIndex()).toString();
                startCity.setSelectedItem(s2);
                arrivalCity.setSelectedItem(s1);
            }
        });
        Exchange.setFont(new Font("宋体", Font.PLAIN, 12));
        Exchange.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        Exchange.setBounds(157, 96, 63, 37);
        AdminFrame.getContentPane().add(Exchange);

        JLabel StartLabel = new JLabel("起飞日期");
        StartLabel.setBounds(394, 60, 108, 29);
        AdminFrame.getContentPane().add(StartLabel);

        JLabel ArrLabel = new JLabel("到达日期");
        ArrLabel.setBounds(571, 67, 54, 29);
        AdminFrame.getContentPane().add(ArrLabel);

        String[] columnNames = {"ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "价格", "航班状态"};
        flight.Flight[] flights = new DbSelect().FlightSelect();
        String[][] flight_ob = new String[flights.length][8];
        for (int i = 0; i < flights.length; i++) {
            flight_ob[i][0] = Integer.toString(flights[i].getId());
            flight_ob[i][1] = flights[i].getFlightName();
            flight_ob[i][2] = flights[i].getStartCity();
            flight_ob[i][3] = flights[i].getArrivalCity();
            flight_ob[i][4] = DateTime
                    .GetDateTimeStr(flights[i].getStartTime());
            flight_ob[i][5] = DateTime.GetDateTimeStr(flights[i]
                    .getArrivalTime());
            flight_ob[i][6] = String.valueOf(flights[i].getPrice());
            flight_ob[i][7] = flights[i].getFlightStatus();
            // System.out.println(flights[i].getFlightStatus());
        }
        // DefaultTableModel model=new DefaultTableModel(flight_ob,columnNames);
        Flight_Table = new JTable(flight_ob, columnNames) {
            /**
             *
             */
            private static final long serialVersionUID = 2386572100105572210L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }// ��������༭
        };
        TableColumn column = null;
        int colunms = Flight_Table.getColumnCount();
        for (int i = 0; i < colunms; i++) {
            column = Flight_Table.getColumnModel().getColumn(i);
            /* ��ÿһ�е�Ĭ�Ͽ������Ϊ100 */
            column.setPreferredWidth(100);
        }
        Flight_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Flight_Table.setSelectionBackground(Color.LIGHT_GRAY);
        Flight_Table.setSelectionForeground(Color.yellow);
        Flight_Table.setBounds(21, 143, 822, 363);
        Flight_Table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {// ������Σ�������˫���¼�
                    // ��ת���޸ĺ���
                    int row = Flight_Table.getSelectedRow();
                    String preId1 = Flight_Table.getValueAt(row, 0).toString();
                    // System.out.println(preId1);
                    AdminFrame.setVisible(false);
                    Login.FlightId = Integer.parseInt(preId1);
                    EditFlight window = new EditFlight();
                    window.getFrame().setVisible(true);

                }
            }
        });
        // AdminFrame.getContentPane().add(Flight);
        JButton newFlight = new JButton("创建新航班");
        newFlight.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdminFrame.setVisible(false);

                CreatFlight window = new CreatFlight();
                window.getFrame().setVisible(true);
            }
        });
        newFlight.setBounds(183, 541, 190, 50);
        AdminFrame.getContentPane().add(newFlight);

        final DateChooser dateChooser = new DateChooser(
                AdminFrame.getContentPane());
        dateChooser.setBounds(364, 96, 153, 37);
        AdminFrame.getContentPane().add(dateChooser);

        final DateChooser dateChooser2 = new DateChooser(
                AdminFrame.getContentPane());
        dateChooser2.setBounds(534, 96, 153, 37);
        AdminFrame.getContentPane().add(dateChooser2);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 143, 800, 363);

        AdminFrame.getContentPane().add(scrollPane);
        scrollPane.setViewportView(Flight_Table);

        JLabel Introedit = new JLabel(
                "提示:双击表格中的数据可以进入编辑页面   ");
        Introedit.setFont(new Font("宋体", Font.ITALIC, 12));
        Introedit.setForeground(Color.GRAY);
        Introedit.setBounds(34, 516, 734, 15);
        AdminFrame.getContentPane().add(Introedit);
        JButton button = new JButton("搜索");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                String s1 = startCity.getItemAt(startCity.getSelectedIndex()).toString();
                String s2 = arrivalCity.getItemAt(arrivalCity.getSelectedIndex()).toString();
                String date1 = dateChooser.getText();
                String date2 = dateChooser2.getText();
                    if (s1.equals(" ") && s2.equals(" ")) {
                        show_table(AdminFrame,new DbSelect().FlightSelect());
                        //AllDialog.Dialog(AdminFrame, "你好管理员");
                    } else {
                        show_table(AdminFrame, new DbSelect().FlightSelect(date1, date2, s1, s2));
                }
            }
        });
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        button.setBounds(694, 96, 81, 37);
        AdminFrame.getContentPane().add(button);

        JLabel hallolabel = new JLabel("New label");
        hallolabel.setFont(new Font("宋体", Font.PLAIN, 18));
        hallolabel.setForeground(Color.BLUE);
        hallolabel.setBounds(0, 578, 199, 50);
        AdminFrame.getContentPane().add(hallolabel);

        JButton SelectOrder = new JButton("查询订单");
        SelectOrder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdminFrame.setVisible(false);
                OrderShow w = new OrderShow();
                w.getFrame().setVisible(true);
            }
        });

        SelectOrder.setBounds(424, 541, 153, 50);
        AdminFrame.getContentPane().add(SelectOrder);

        JButton Booking = new JButton("查询预订信息");
        Booking.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AdminFrame.setVisible(false);
                BookingShow wBookingShow = new BookingShow();
                wBookingShow.getFrame().setVisible(true);
            }
        });
        Booking.setBounds(616, 541, 152, 50);
        AdminFrame.getContentPane().add(Booking);

        if (Login.AdminId != 0) {
            Admin nowadmin = new DbSelect().AdminSelect(Login.AdminId);

            hallolabel.setText("你好，管理员" + nowadmin.getUsername());
        }

    }

    public void show_table(final JFrame AdminFrame,
                           flight.Flight[] flights) {
        String[] columnNames = { "ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "价格", "航班状态" };
        // com.flight.java.Flight[] flights = new DbSelect().FlightSelect();
        if (flights != null) {
            String[][] flight_ob = new String[flights.length][8];
            for (int i = 0; i < flights.length; i++) {
                flight_ob[i][0] = Integer.toString(flights[i].getId());
                flight_ob[i][1] = flights[i].getFlightName();
                flight_ob[i][2] = flights[i].getStartCity();
                flight_ob[i][3] = flights[i].getArrivalCity();
                flight_ob[i][4] = DateTime.GetDateTimeStr(flights[i]
                        .getStartTime());
                flight_ob[i][5] = DateTime.GetDateTimeStr(flights[i]
                        .getArrivalTime());
                flight_ob[i][6] = String.valueOf(flights[i].getPrice());
                flight_ob[i][7] = flights[i].getFlightStatus();

            }
            Flight_Table = new JTable(flight_ob, columnNames) {
                private static final long serialVersionUID = 176942350943898960L;

                public boolean isCellEditable(int row, int column) {
                    return false;
                }// ��������༭
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
            Flight_Table.setBounds(21, 143, 822, 363);
            Flight_Table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {

                    if (e.getClickCount() == 2) {
                        int row = Flight_Table.getSelectedRow();
                        String preId1 = Flight_Table.getValueAt(row, 0).toString();
                        AdminFrame.setVisible(false);
                        Login.FlightId = Integer.parseInt(preId1);
                        EditFlight window = new EditFlight();
                        window.getFrame().setVisible(true);
                    }
                }

            });
            scrollPane.setViewportView(Flight_Table);
        } else {
            AllDialog.Dialog(AdminFrame, "没有当前航班数据");
        }
    }
    public JFrame getFrame() {
        return this.AdminFrame;
    }
}
