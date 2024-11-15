package frame;

import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.TableColumn;

import flight.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class Reschedule {
    private JFrame frame;
    private JPasswordField passwordField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Reschedule window = new Reschedule("a","b");
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Reschedule(String info,String flightname) {
        initialize(info,flightname);
    }

    private void initialize(String info,String flightname) {
            frame = new JFrame();
            frame.setTitle("改签订单");
            frame.setBounds(100, 100, 850, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setLayout(null);

        //取消订单提示
        JLabel label1 = new JLabel("温馨提示：当前航班属于中转航班");
        JLabel label3 = new JLabel("如果要改签，将为您提供从出发城市到到达城市的所有时间航班");
        JLabel label2 = new JLabel(info);

        label1.setFont(new Font("宋体", Font.PLAIN, 18));
        label1.setForeground(Color.RED);
        label1.setBounds(60, 38, 500, 63);
        frame.getContentPane().add(label1);

        label2.setFont(new Font("宋体", Font.PLAIN, 15));
        label2.setForeground(Color.BLACK);
        label2.setBounds(60, 170, 750, 63);
        frame.getContentPane().add(label2);

        label3.setFont(new Font("宋体", Font.PLAIN, 18));
        label3.setForeground(Color.RED);
        label3.setBounds(60, 100, 7500, 63);
        frame.getContentPane().add(label3);

        //返回按钮
        JButton button = new JButton("返回");
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
                PassengerOrder wMyOrder = new PassengerOrder();
                wMyOrder.getFrame().setVisible(true);
            }
        });
        button.setBounds(48, 280, 93, 47);
        frame.getContentPane().add(button);

        //确认按钮
        JButton button1 = new JButton("确定");
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
                RescheduleSearch window = new RescheduleSearch(info,flightname,false);//查询中转
                //RescheduleSearch window = new RescheduleSearch("a","BJ122452");
                window.getFrame().setVisible(true);
            }
        });
        button1.setBounds(500, 280, 93, 47);
        frame.getContentPane().add(button1);
    }

    public JFrame getFrame() {
        return frame;
    }

}

class RescheduleSearch {
    private JFrame frame;
    private JPasswordField passwordField;
    private Flight[] currentFlights;
    private JTable Flight_Table;
    private JScrollPane scrollPane;
    static boolean isDomestic =true;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    RescheduleSearch window = new RescheduleSearch("a","BJ122452",false);
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public RescheduleSearch(String info,String flightname,boolean straight) {
        initialize(info,flightname,straight);
    }

    private void initialize(String info,String flightname,boolean straight) {
        frame = new JFrame();
        frame.setTitle("改签搜索界面");
        frame.setBounds(100, 100, 950, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        List<String> cities;

        if(straight){
            cities = new DbSelect().getCitiesByFlightName(flightname);//直飞查找出发城市和目的城市
            if (cities.get(0) != null && cities.get(1) != null) {
                System.out.println("起始城市: " + cities.get(0));
                System.out.println("目的城市: " + cities.get(1));
            } else {
                System.out.println("未找到对应的航班记录");
            }
        }else{
            cities = new DbSelect().findCitiesByFlightNameAndPid(flightname, Login.PassengerId);
            //cities = new DbSelect().findCitiesByFlightNameAndPid(flightname,12);//查询中转航班的起始地址和目的地址
            if (!cities.isEmpty()) {
                System.out.println("起始地址: " + cities.get(0));
                System.out.println("目的地址: " + cities.get(1));
            } else {
                System.out.println("未找到匹配的记录");
            }

        }
        setTable1(frame,cities,flightname,straight);

        JLabel label2 = new JLabel("起始地址: " + cities.get(0)+"     "+"目的地址: " + cities.get(1));
        label2.setFont(new Font("宋体", Font.PLAIN, 15));
        label2.setForeground(Color.BLACK);
        label2.setBounds(0, 0, 750, 63);
        frame.getContentPane().add(label2);


        //日期选择器
        final DateChooser dateChooser = new DateChooser(frame.getContentPane(),
                100);
        dateChooser.setBounds(300, 0, 126, 63);
        frame.getContentPane().add(dateChooser);

        //按钮_刷新列表
        JButton fresh = new JButton("刷新列表");
        fresh.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTable1(frame,cities,flightname,straight);
            }
        });
        fresh.setBounds(837, 53, 87, 42);
        frame.getContentPane().add(fresh);

        //返回登录界面
        JButton button_1 = new JButton("返回");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                PassengerOrder wMyOrder = new PassengerOrder();
                wMyOrder.getFrame().setVisible(true);
            }
        });

        button_1.setFont(new Font("宋体", Font.PLAIN, 14));
        button_1.setBounds(150, 500, 150, 35);
        frame.getContentPane().add(button_1);

        //查询按钮
        JButton search = new JButton("查询");
        search.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                String s1 = cities.get(0);
                String s2 = cities.get(1);
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
                    setTable(frame, processedFlights,s1,s2,flightname,straight);
                } else {
                    AllDialog.Dialog(frame, "当前没有符合条件的航班");
                }
            }
        });
        search.setFont(new Font("宋体", Font.PLAIN, 14));
        search.setBounds(600, 10, 80, 40);
        frame.getContentPane().add(search);

    }

    // 创建表格_by frame 初始化/刷新
    private void setTable1(final JFrame frame,List<String> cities,String flightname,boolean straight) {
        if (scrollPane != null) {
            frame.getContentPane().remove(scrollPane); // 清除旧的表格
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String[] columnNames = {"ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "价格", "是否预定", "模式","航班状态"};

        // 更新当前表格的航班数据源
        currentFlights = new DbSelect().FlightSelectForTransit(cities.get(0),cities.get(1),isDomestic); // 更新 currentFlights

        // 按起飞时间排序，从早到晚
        Arrays.sort(currentFlights, Comparator.comparing(Flight::getStartTime));

        // 将航班信息填充到 flight_ob 二维数组中
        String[][] flight_ob = new String[currentFlights.length][10];
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
        Flight_Table.setBounds(21, 100, 700, 363);

        // 添加滚动面板
        scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 100, 912, 363);
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
                        ReserveFlight window = new ReserveFlight(isDomestic);//预定直飞
                        System.out.println("无查询-改签预定直飞路线");
                        DeleteTransit(flightname,straight);//删除原来的航班
                        System.out.println("无查询-改签删除原来"+(straight?"直飞":"中转")+"路线");
                        window.getFrame().setVisible(true);
                    }
                }
            }
        });
    }

    //创建表格_by frame 精准查询
    private void setTable(final JFrame frame, Flight[] flights,String startcity,String arrivalcity,String flightname,boolean straight) {
        if (scrollPane != null) {
            frame.getContentPane().remove(scrollPane); // 清除旧的表格
        }
        String[] columnNames = { "ID", "航班号", "起飞城市", "到达城市", "起飞时间", "到达时间", "价格", "是否预定","模式","航班状态" };
        String[][] flight_ob = new String[flights.length][10];
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
            flight_ob[i][6] = (flights[i].getPrice()==0.0f)?"":String.valueOf(flights[i].getPrice());
            if(flights[i].getStartTime()==null){
                flight_ob[i][7] = "";
            }
            else if (Order.IsHasOrder(Login.PassengerId, flights[i].getId(),isDomestic)&&flights[i].getStartCity()!=null) {
                flight_ob[i][7] = "未预定";
            }else{
                flight_ob[i][7] = "已预定";
            }
            if(flights[i].getStartTime()==null){
                flight_ob[i][8] = "";
            }
            else if(Objects.equals(flights[i].getStartCity(), startcity)&&Objects.equals(flights[i].getArrivalCity(), arrivalcity)){
                flight_ob[i][8] = "直飞";
            }
            else
                flight_ob[i][8] = "中转";
            if(flights[i].getStartTime()==null){
                flight_ob[i][9] = "";
            }else {
                flight_ob[i][9] = currentFlights[i].getFlightStatus();
            }

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
        Flight_Table.setBounds(21, 100, 700, 363);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 100, 912, 363);

        frame.getContentPane().add(scrollPane);
        scrollPane.setViewportView(Flight_Table);
        Flight_Table.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = Flight_Table.getSelectedRow();
                    if (row == -1) return;

                    //获取航班信息
                    String flightId = Flight_Table.getValueAt(row, 0).toString();
                    String transferFlag = Flight_Table.getValueAt(row, 8).toString();
                    String startname = Flight_Table.getValueAt(row, 1).toString();
                    String startLocation = Flight_Table.getValueAt(row, 2).toString();
                    String endLocation = Flight_Table.getValueAt(row, 3).toString();

                    frame.setVisible(false);
                    Login.FlightId = Integer.parseInt(flightId);
                    ReserveFlight window = new ReserveFlight(isDomestic);//预定直飞或中转第一班
                    System.out.println("有查询-改签预定直飞/中转第一班路线");
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
                                String nextTransferFlag = Flight_Table.getValueAt(rowWrapper[0], 8).toString();
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

                                    DeleteTransit(flightname,straight);//删除原来中转航班
                                    System.out.println("有查询-改签中转航班-删除原来"+(straight?"直飞":"中转")+"路线");
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
                                    System.out.println("有查询-改签预定中转第二班路线");
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

                    }else{
                        // 直飞航班的删除操作
                        DeleteTransit(startname, straight);
                        System.out.println("有查询-改签直飞航班-删除原来"+(straight?"直飞":"中转")+"路线");
                    }

                }
                // 在停止计时器后输出 transferFlightsMap
                System.out.println("最终的中转航班路线:");
                Login.transferFlightsMap.forEach((key, value) -> System.out.println("航班ID: " + key + " 路线: " + value));
            }


        });
    }

    private  void DeleteTransit(String flightname,boolean straight) {
        boolean x;
        boolean x1=false;
        //删除原订单
        Order o = new DbSelect().OrderSelect(Login.OrderId,Research.isDomestic);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        x = new DbUpdate().OrderUpdate(Login.OrderId, o.getPassengerId().getId(), o.getSeat(),
                o.getFlightId().getId(),
                o.getCreateDate().format(formatter), "CANCEL",Research.isDomestic) //更新订单表，状态列为CANCEL
                && Passenger.UnsubscribeFlight(o.getPassengerId().getId(),o.getFlightId().getId(),Login.Pwd,Research.isDomestic);//成功取消航班
        System.out.println("已删除第一班路线"+flightname);

        if(!straight){
            System.out.println("原订单是中转航班");
            String anotherflightname = new DbSelect().findOtherFlightName(Login.PassengerId,flightname);
            System.out.println("另一个航班名为"+anotherflightname);
            int anotherfid = new DbSelect().findFlightIdByName(anotherflightname);
            System.out.println("另一个航班ID为"+anotherfid);
            int anotherOrderid = new DbSelect().queryOrderId(Login.PassengerId,anotherfid);
            System.out.println("另一个航班订单ID为"+anotherfid);
            //删除其他订单
            Order o1 = new DbSelect().OrderSelect(anotherOrderid,Research.isDomestic);
            x1 = new DbUpdate().OrderUpdate(anotherOrderid, o1.getPassengerId().getId(), o1.getSeat(),
                    o1.getFlightId().getId(),
                    o1.getCreateDate().format(formatter), "CANCEL",Research.isDomestic) //更新订单表，状态列为CANCEL
                    && Passenger.UnsubscribeFlight(o1.getPassengerId().getId(),o1.getFlightId().getId(),Login.Pwd,Research.isDomestic);//成功取消航班
            System.out.println("已删除第二班路线");
        }

        if (x) {
            frame.setVisible(false);
            PassengerOrder wMyOrder = new PassengerOrder();
            wMyOrder.getFrame().setVisible(true);
            AllDialog.Dialog(wMyOrder.getFrame(), "操作成功");
        } else {
            AllDialog.Dialog(frame, "操作失败，请重试");
        }


    }


    public Frame getFrame() {
        return frame;
    }
}
