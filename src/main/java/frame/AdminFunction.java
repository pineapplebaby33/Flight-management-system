package frame;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import javax.swing.table.TableColumn;

import flight.*;
//import com.flight.java.DateTime;
//import com.flight.java.DbSelect;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;

public class AdminFunction {
    private JFrame AdminFrame;
    private JTable Flight_Table;
    private JScrollPane scrollPane;
    static boolean isDomestic =true;
    private Flight[] currentFlights; // 新增：用于保存当前表格中显示的航班数据

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
        show_table1(AdminFrame);


        //按钮_添加管理员
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

        //按钮_修改个人信息
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

        //按钮_退出登录
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

        //标签_起飞城市
        JLabel label = new JLabel("起始城市");
        label.setBounds(34, 60, 108, 29);
        AdminFrame.getContentPane().add(label);

        //出发城市下拉框
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

        //标签降落城市
        JLabel label_1 = new JLabel("降落城市");
        label_1.setBounds(238, 60, 108, 29);
        AdminFrame.getContentPane().add(label_1);

        //降落城市下拉框
        // 初始化降落城市下拉框
        final JComboBox<String> arrivalCity = new JComboBox<>();
        updateArrivalCity(arrivalCity, isDomestic);
        arrivalCity.setToolTipText("");
        arrivalCity.setBounds(228, 96, 127, 37);
        AdminFrame.getContentPane().add(arrivalCity);

        //按钮_国内航班
        JButton button_00 = new JButton("国内航班");
        button_00.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isDomestic =true;
                updateArrivalCity(arrivalCity, isDomestic);
                show_table1(AdminFrame);

            }
        });
        button_00.setBounds(0, 0, 87, 42);
        AdminFrame.getContentPane().add(button_00);

        //按钮_国外航班
        JButton button_01 = new JButton("国外航班");
        button_01.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isDomestic =false;
                updateArrivalCity(arrivalCity, isDomestic);
                show_table1(AdminFrame);
            }
        });
        button_01.setBounds(90, 0, 87, 42);
        AdminFrame.getContentPane().add(button_01);

        //按钮_交换
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

        //标签_起飞时间
        JLabel StartLabel = new JLabel("起飞日期");
        StartLabel.setBounds(394, 60, 108, 29);
        AdminFrame.getContentPane().add(StartLabel);

        //日期选择器
        final DateChooser dateChooser = new DateChooser(AdminFrame.getContentPane());
        dateChooser.setBounds(364, 96, 153, 37);
        AdminFrame.getContentPane().add(dateChooser);


        // 创建复选框
        JCheckBox checkBox = new JCheckBox("无时间查找");
        checkBox.setBounds(520, 96, 153, 37); // 设置位置和大小
        // 添加鼠标监听器
        checkBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (checkBox.isSelected()) {
                    System.out.println("复选框已选中：执行选中操作");
                } else {
                    System.out.println("复选框未选中：执行取消操作");
                }
            }
        });
        AdminFrame.getContentPane().add(checkBox);

        //标签
        JLabel Introedit = new JLabel(
                "提示:双击表格中的数据可以进入编辑页面   ");
        Introedit.setFont(new Font("宋体", Font.ITALIC, 12));
        Introedit.setForeground(Color.GRAY);
        Introedit.setBounds(34, 516, 734, 15);
        AdminFrame.getContentPane().add(Introedit);

        //按钮_搜索
        JButton button = new JButton("搜索");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                String s1 = startCity.getItemAt(startCity.getSelectedIndex()).toString();
                String s2 = arrivalCity.getItemAt(arrivalCity.getSelectedIndex()).toString();
                String date1 = dateChooser.getText();
                    if (s1.equals(" ") && s2.equals(" ")) {
                        show_table(AdminFrame,new DbSelect().FlightSelect(AdminFunction.isDomestic));//刷新
                    } else if(checkBox.isSelected()){
                        show_table(AdminFrame, new DbSelect().FlightSelect(date1, s1, s2,isDomestic));
                        }
                        else{
                            show_table(AdminFrame, new DbSelect().FlightSelect(date1, s1, s2,isDomestic));
                        }

            }
        });
        button.setBounds(694, 96, 81, 37);
        AdminFrame.getContentPane().add(button);

        //标签_欢迎
        JLabel hallolabel = new JLabel("New label");
        hallolabel.setFont(new Font("宋体", Font.PLAIN, 18));
        hallolabel.setForeground(Color.BLUE);
        hallolabel.setBounds(0, 578, 199, 50);
        AdminFrame.getContentPane().add(hallolabel);

        if (Login.AdminId != 0) {
            Admin nowadmin = new DbSelect().AdminSelect(Login.AdminId);

            hallolabel.setText("你好，管理员" + nowadmin.getUsername());
        }

        //按钮_创建新航班
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

        /*
        //按钮_查询订单
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

         */

        //按钮_查询预定信息
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



    }

    //初始化界面
    private void show_table1(final JFrame AdminFrame){
        if (scrollPane != null) {
            AdminFrame.getContentPane().remove(scrollPane); // 清除旧的表格
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String[] columnNames = {"ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "价格", "航班状态"};
        currentFlights = new DbSelect().FlightSelect(isDomestic);
        String[][] flight_ob = new String[currentFlights.length][9];
        for (int i = 0; i < currentFlights.length; i++) {
            flight_ob[i][0] = Integer.toString(currentFlights[i].getId());
            flight_ob[i][1] = currentFlights[i].getFlightName();
            flight_ob[i][2] = currentFlights[i].getStartCity();
            flight_ob[i][3] = currentFlights[i].getArrivalCity();
            flight_ob[i][4] = currentFlights[i].getStartTime().format(formatter);
            flight_ob[i][5] = currentFlights[i].getArrivalTime().format(formatter);
            flight_ob[i][6] = String.valueOf(currentFlights[i].getPrice());
            flight_ob[i][7] = currentFlights[i].getFlightStatus();
            flight_ob[i][8] = "直飞";
        }
        Flight_Table = new JTable(flight_ob, columnNames) {
            private static final long serialVersionUID = 2386572100105572210L;

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
        Flight_Table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Flight_Table.setSelectionBackground(Color.LIGHT_GRAY);
        Flight_Table.setSelectionForeground(Color.yellow);
        Flight_Table.setBounds(21, 143, 822, 363);

        //滚轴
        scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 143, 800, 363);
        AdminFrame.getContentPane().add(scrollPane);
        scrollPane.setViewportView(Flight_Table);

        Flight_Table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = Flight_Table.getSelectedRow();
                    if (row != -1) {
                        // 使用 currentFlights 数组而不是原来的数据源
                        Flight selectedFlight = currentFlights[row];
                        AdminFrame.setVisible(false);
                        Login.FlightId = selectedFlight.getId();
                        EditFlight window = new EditFlight();
                        window.getFrame().setVisible(true);
                    }
                }
            }
        });

    }

    //精准查询
    public void show_table(final JFrame AdminFrame, flight.Flight[] flights) {
        String[] columnNames = { "ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "价格", "航班状态" };
        // com.flight.java.Flight[] flights = new DbSelect().FlightSelect();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        if (flights != null) {
            String[][] flight_ob = new String[flights.length][8];
            for (int i = 0; i < flights.length; i++) {
                flight_ob[i][0] = Integer.toString(flights[i].getId());
                flight_ob[i][1] = flights[i].getFlightName();
                flight_ob[i][2] = flights[i].getStartCity();
                flight_ob[i][3] = flights[i].getArrivalCity();
                flight_ob[i][4] = flights[i].getStartTime().format(formatter);
                flight_ob[i][5] = flights[i].getArrivalTime().format(formatter);
                flight_ob[i][6] = String.valueOf(flights[i].getPrice());
                flight_ob[i][7] = flights[i].getFlightStatus();

            }
            Flight_Table = new JTable(flight_ob, columnNames) {
                private static final long serialVersionUID = 176942350943898960L;

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

    // 方法：根据 isDomestic 更新 arrivalCity 下拉框的内容
    private void updateArrivalCity(JComboBox<String> comboBox, boolean isDomestic) {
        if (isDomestic) {
            comboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                    " ",
                    "北京", "上海", "天津", "重庆",
                    "哈尔滨", "长春", "沈阳",
                    "呼和浩特", "石家庄",
                    "乌鲁木齐", "兰州", "西宁",
                    "西安 ", "银川", "郑州",
                    "济南", "太原", "合肥", "长沙",
                    "武汉", "南京", "成都", "贵阳",
                    "昆明", "南宁", "拉萨", "杭州",
                    "南昌", "广州", "福州", "台北",
                    "海口", "香港", "澳门", "深圳"
            }));
        } else {
            comboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                    " ",
                    "纽约", "伦敦", "巴黎", "柏林", "阿姆斯特丹", "慕尼黑",
                    "罗马", "东京", "首尔", "曼谷", "悉尼", "奥克兰",
                    "温哥华", "莫斯科", "芝加哥", "洛杉矶", "新加坡",
                    "旧金山"
            }));
        }
        comboBox.revalidate();
        comboBox.repaint();
    }
}
