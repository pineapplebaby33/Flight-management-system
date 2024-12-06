package frame;
import flight.DbSelect;
import flight.Flight;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.TableColumn;

import flight.*;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;


import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;



public class FlightRecommendation {
    private JFrame frame;
    private JTable Flight_Table;
    private JScrollPane scrollPane;
    static boolean isDomestic =true;
    private Flight[] currentFlights; // 新增：用于保存当前表格中显示的航班数据
    private boolean HasOrderPackage = false;
    private boolean Hasuse = false;

    // 声明需要动态更新的 JLabel
    private JLabel label1; // 国内外偏好
    private JLabel label8; // 最常落地城市
    private JLabel label3; // 出发日期偏好
    private JLabel label2; // 时间偏好
    private JLabel label4; // 历史价格


    public static String selectstatue = "选择的套餐名";
    //遍历收集信息
    String mostFrequentDestination = null;
    int maxDestinationCount = 0;
    //目的城市
    Map<String, Integer> destinationCount = new HashMap<>();
    //出发日期
    int summerVacationCount = 0;
    int holidayCount = 0;
    int workdayCount = 0;

    //出发时间
    int morningCount = 0; // 早晨 (00:00-11:59)
    int afternoonCount = 0; // 下午 (12:00-17:59)
    int eveningCount = 0; // 晚上 (18:00-23:59)

    //价格区间
    float minPrice = Float.MAX_VALUE;
    float maxPrice = Float.MIN_VALUE;


    public JFrame getFrame() {

        return this.frame;
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FlightRecommendation window = new FlightRecommendation();
                    window.frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //查询用户的订单信息
        Flight[] filghts = new DbSelect().FlightSelectByPassengerId(Login.PassengerId,true);
        if (filghts != null) {
            for (Flight flight : filghts) {
                System.out.println("Flight ID: " + flight.getId());
                System.out.println("Flight Name: " + flight.getFlightName());
                System.out.println("Start City: " + flight.getStartCity());
                System.out.println("Arrival City: " + flight.getArrivalCity());
                System.out.println("Passenger ID: " + flight.getPassengerId());
                System.out.println("----------------------------");
            }
        } else {
            System.out.println("No flights found.");
        }


    }
    public FlightRecommendation() {
        initialize();
    }

    private void initialize() {
        Flight[] flights = new DbSelect().FlightSelectByPassengerId(Login.PassengerId, true);
        Flight[] flight1s = new DbSelect().FlightSelectByPassengerId(Login.PassengerId, false);

        class bianli{
            public void bianli(Flight[] flights){
                if (flights != null && flights.length > 0) {
                    // 遍历 flights
                    for (Flight flight : flights) {
                        // 1. 最常用的目的地
                        String destination = flight.getArrivalCity();
                        destinationCount.put(destination, destinationCount.getOrDefault(destination, 0) + 1);

                        // 2. 出发日期分类
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
                        String startTime = flight.getStartTime().format(formatter); // 假设格式为 YYYY-MM-DD-HH-mm-ss
                        String[] timeParts = startTime.split("-"); // 按 '-' 分割时间
                        String date = timeParts[0] + "-" + timeParts[1] + "-" + timeParts[2]; // 提取 YYYY-MM-DD
                        int hour = Integer.parseInt(timeParts[3]); // 提取小时 (HH)
                        if (isSummerVacation(date)) {
                            summerVacationCount++;
                        } else if (isHoliday(date)) {
                            holidayCount++;
                        } else {
                            workdayCount++;
                        }

                        // 3. 起飞时间段统计
                        if (hour < 12) {
                            morningCount++;
                        } else if (hour < 18) {
                            afternoonCount++;
                        } else {
                            eveningCount++;
                        }


                        // 4. 历史最低价格和最高价格
                        if(flights.length==0){
                            minPrice =0;
                            maxPrice =0;
                            System.out.println("flights.length==0" + minPrice+maxPrice);
                        }else{
                            float price = flight.getPrice();
                            if (price < minPrice) minPrice = price;
                            if (price > maxPrice) maxPrice = price;
                            System.out.println("flights.length!=0" + minPrice+maxPrice);
                        }



                    }

                    // 找出最常用的目的地
                    //mostFrequentDestination = null;
                    //int maxDestinationCount = 0;
                    for (Map.Entry<String, Integer> entry : destinationCount.entrySet()) {
                        if (entry.getValue() > maxDestinationCount) {
                            mostFrequentDestination = entry.getKey();
                            maxDestinationCount = entry.getValue();
                        }
                    }

                    // 输出统计结果
                    System.out.println("感谢与您的相遇！在为您服务期间：");
                    System.out.println("您预定了国内/外航班：" + flights.length + "次");
                    System.out.println("其中，您最常落地的城市是：" + mostFrequentDestination + "，有 " + maxDestinationCount + " 次");
                    System.out.println("您的航班出发日期在寒暑假：" + summerVacationCount + " 次，节假日：" + holidayCount + " 次，在工作日：" + workdayCount + " 次");
                    System.out.println("您的航班历史最低价格：" + minPrice + " 元，历史最高价格：" + maxPrice + " 元");
                    System.out.println("您的航班起飞时间分布如下：");
                    System.out.println("早晨 (00:00-11:59): " + morningCount + " 次");
                    System.out.println("下午 (12:00-17:59): " + afternoonCount + " 次");
                    System.out.println("晚上 (18:00-23:59): " + eveningCount + " 次");
                } else {
                    minPrice =0;
                    maxPrice =0;
                    System.out.println("No flights found.");
                }
            }

        }


        bianli bl =new bianli();
        if((flights==null?0:flights.length)<(flight1s==null?0:flight1s.length)){
            bl.bianli(flight1s);
        }else{
            bl.bianli(flights);
        }


        //窗口信息
        frame = new JFrame();
        Passenger p = new DbSelect().PassengerSelect(Login.PassengerId);
        if (p != null) {
            String Frametext = "个性推荐   欢迎," + p.getRealName();
            frame.setTitle(Frametext);
        } else {
            frame.setTitle("个性信息");
        }

        // 页面布局
        frame.setBounds(250, 50, 950, 800); // 增加高度到 800
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        //按钮_国内航班
        JButton button_00 = new JButton("国内航班");
        button_00.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isDomestic =true;
                updateStatistics(flights);
                recommendFlights(flights);
            }
        });
        button_00.setBounds(350, 70, 100, 32);
        frame.getContentPane().add(button_00);

        //按钮_国内航班
        JButton button_01 = new JButton("国外航班");
        button_01.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isDomestic =true;
                updateStatistics(flight1s);
                recommendFlights(flight1s);
            }
        });
        button_01.setBounds(460, 70, 100, 32);
        frame.getContentPane().add(button_01);

        // 问候语，天蓝色字体
        JLabel label0 = new JLabel("亲爱的 " + p.getRealName() + " 乘客您好！感谢与您的相遇！在为您服务期间：");
        label0.setBounds(50, 20, 850, 40); // 调整宽度和高度
        label0.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
        label0.setForeground(new Color(173, 193, 212));
        frame.getContentPane().add(label0);

        // 国内外偏好
        label1 = new JLabel("<html>您预定了国内航班: <font color='rgb(173,193,212)'>" + (flights==null?0:flights.length)
                + "</font> 次，国际航班: <font color='rgb(173,193,212)'>" + (flight1s==null?0:flight1s.length) + "</font> 次</html>");
        label1.setBounds(50, 70, 850, 30); // 适当下移
        label1.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        label1.setForeground(Color.BLACK);
        frame.getContentPane().add(label1);

        // 最常落地城市
        label8 = new JLabel("<html>其中，您最常落地的城市是: <font color='rgb(173,193,212)'>"
                + mostFrequentDestination + "</font>，有 <font color='rgb(173,193,212)'>" + maxDestinationCount + "</font> 次</html>");
        label8.setBounds(50, 110, 850, 30);
        label8.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        label8.setForeground(Color.BLACK);
        frame.getContentPane().add(label8);

        // 出发日期偏好
        label3 = new JLabel("<html>您的航班出发日期在:<br>寒暑假: <font color='rgb(173,193,212')>"
                + summerVacationCount + "</font> 次，<br>节假日: <font color='rgb(173,193,212')>" + holidayCount
                + "</font> 次，<br>工作日: <font color='rgb(173,193,212')>" + workdayCount + "</font> 次</html>");
        label3.setBounds(50, 150, 850, 83); // 增加垂直间距
        label3.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        label3.setForeground(Color.BLACK);
        frame.getContentPane().add(label3);

        // 时间偏好
        label2 = new JLabel("<html>您的航班起飞时间在:<br>早晨 (00:00-11:59): <font color='rgb(173,193,212')>"
                + morningCount + "</font> 次，<br>下午 (12:00-17:59): <font color='rgb(173,193,212')>" + afternoonCount
                + "</font> 次，<br>晚上 (18:00-23:59): <font color='rgb(173,193,212')>" + eveningCount + "</font> 次</html>");
        label2.setBounds(50, 245, 850, 83); // 向下调整位置
        label2.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        label2.setForeground(Color.BLACK);
        frame.getContentPane().add(label2);

        // 历史最低和最高价格
        label4 = new JLabel("<html>您的航班历史最低价格: <font color='rgb(173,193,212')>"
                + minPrice + "</font> 元，<br>历史最高价格: <font color='rgb(173,193,212')>" + maxPrice + "</font> 元</html>");
        label4.setBounds(50, 330, 850, 50); // 间距进一步调整
        label4.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        label4.setForeground(Color.BLACK);
        frame.getContentPane().add(label4);

        // 推荐套餐标题
        JLabel label6 = new JLabel("<html><font color='rgb(173,193,212')>以下是我们根据您的个人偏好为您推荐的套餐:</font></html>");
        label6.setBounds(50, 400, 850, 30); // 向下移动
        label6.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        frame.getContentPane().add(label6);

        // 添加推荐套餐的下拉框和文字框
        JLabel packageLabel = new JLabel("请选择套餐:");
        packageLabel.setBounds(50, 450, 100, 30);
        frame.getContentPane().add(packageLabel);

        // 下拉框选项
        String[] packages = {"国内随心飞", "国外随心飞", "学生寒暑假"};
        //String[] packages = {};
        JComboBox<String> packageComboBox = new JComboBox<>(packages);
        packageComboBox.setBounds(150, 450, 150, 30);
        frame.getContentPane().add(packageComboBox);

        // 显示套餐内容的文本框
        JTextArea packageDetails = new JTextArea();
        packageDetails.setBounds(320, 450, 400, 100);
        packageDetails.setLineWrap(true);
        packageDetails.setWrapStyleWord(true);
        packageDetails.setEditable(false);
        packageDetails.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        packageDetails.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        frame.getContentPane().add(packageDetails);

        System.out.println((flights==null?0:flights.length)+"  "+(flight1s==null?0:flight1s.length));
        // 初始化套餐内容
        if((flights==null?0:flights.length)<(flight1s==null?0:flight1s.length)){//国外航班
            packageDetails.setText(FlightRecommendation.getPackageDetails("国外随心飞"));
            packages = new String[]{"国外随心飞", "国内随心飞", "学生寒暑假"};
            System.out.println("国外随心飞");
        } else if (summerVacationCount > holidayCount && summerVacationCount > workdayCount) {
            packages = new String[]{"学生寒暑假", "国外随心飞", "国内随心飞"};
            packageDetails.setText(FlightRecommendation.getPackageDetails("学生寒暑假"));
        }else{
            packages = new String[]{"国内随心飞", "国外随心飞", "学生寒暑假"};
            packageDetails.setText(FlightRecommendation.getPackageDetails("国内随心飞"));
        }

        // 更新 JComboBox 的内容
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(packages);
        packageComboBox.setModel(comboBoxModel);

        // 设置默认选中的第一个选项
        packageComboBox.setSelectedIndex(0);




        // 添加下拉框监听器，动态更新文本框内容
        packageComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedPackage = (String) packageComboBox.getSelectedItem();
                packageDetails.setText(getPackageDetails(selectedPackage));
            }
        });


        //输出订购信息
        //判断订单能否生效
        DbSelect sa = new DbSelect();
        //返回当前状态
        //String packagestatus = sa.queryPackageStatus(33);
        String packagestatus = sa.queryPackageStatus(Login.PassengerId);
        System.out.println("当前状态: " + packagestatus);
        JLabel show1 = new JLabel("当前套餐:"+packagestatus);
        show1.setBounds(420, 400, 120, 30);
        frame.getContentPane().add(show1);



        //返回所有已购状态
        //List<Map<String, Object>> packages1 = sa.queryAllPackageStatus(33);
        List<Map<String, Object>> packages1 = sa.queryAllPackageStatus(Login.PassengerId);
        System.out.println("Login.PassengerId: " + Login.PassengerId);
        // 用于存储所有已满状态的套餐名称
        List<String> fullPackages = new ArrayList<>();
        // 遍历返回结果，筛选出已满状态的套餐名称
        for (Map<String, Object> packageInfo : packages1) {
            if ((boolean) packageInfo.get("IsFull")) {
                fullPackages.add((String) packageInfo.get("Package"));
            }
        }
        // 转换为数组
        String[] fullPackageArray = fullPackages.toArray(new String[0]);
        // 输出已满状态的套餐名称数组
        System.out.println("Full Packages: " + Arrays.toString(fullPackageArray));


        String history = (fullPackageArray.length > 0)
                ? String.join(", ", fullPackageArray)
                : "无已购套餐";
        JLabel show2 = new JLabel("历史已购套餐: " + history);
        show2.setBounds(570, 400, 150, 30);
        frame.getContentPane().add(show2);

        //跳转支付界面
        JButton Create = new JButton("立马下单❤");
        Create.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HasOrderPackage = false;
                for(String packagename : fullPackageArray){
                    System.out.println("packagename："+packagename);
                    if(packagename.equals(packageComboBox.getSelectedItem()))
                        HasOrderPackage = true;

                }
                System.out.println("packageComboBox.getSelectedItem(): " + packageComboBox.getSelectedItem());
                selectstatue=(String)packageComboBox.getSelectedItem();
                System.out.println("是否重复预定："+HasOrderPackage);

                if(!Objects.equals(packagestatus, "无")){//在订购中
                    AllDialog.Dialog(frame, "您当前正在享受"+packagestatus+"套餐,\n等待当前订单结束之后再订购其他套餐");
                }else if(HasOrderPackage){//是否重复预定
                    AllDialog.Dialog(frame, "您已订购过该套餐，请勿重复预定！\n可以选择其他未订购过的套餐");
                }else{//可以预定
                    frame.setVisible(false);
                    System.out.println("FlightRecommendation跳转预定套餐界面");
                    PackagePay pp = new PackagePay((String)packageComboBox.getSelectedItem());
                    System.out.println("预定套餐界面跳转FlightRecommendation");
                    pp.getFrame().setVisible(true);
                }

            }
        });
        Create.setBounds(92, 500, 153, 37);
        frame.getContentPane().add(Create);

        //退订界面
        JButton cancel = new JButton("狠心退订");
        cancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Hasuse = sa.checkPackageExists(Login.PassengerId,packagestatus);
                System.out.println("当前要取消的套餐"+packagestatus);
                if(Hasuse){
                    AllDialog.Dialog(frame, "您当前已经使用了"+packagestatus+"套餐，无法退订");
                }else{
                    DbDelete d = new DbDelete();
                    boolean da =d.deletePackageOrder(Login.PassengerId,packagestatus,0);
                    if(da){
                        System.out.println("已经删除套餐"+packagestatus);
                    }else{
                        System.out.println("删除套餐"+packagestatus+"失败");
                    }
                    AllDialog.Dialog(frame, "已经删除套餐"+packagestatus);
                    Login.packagestatus = sa.queryPackageStatus(Login.PassengerId);
                    //packagestatus = sa.queryPackageStatus(Login.PassengerId);
                    show1.setText("当前套餐:"+Login.packagestatus) ;
                }



            }
        });
        cancel.setBounds(92, 550, 153, 37);
        frame.getContentPane().add(cancel);

        // 添加推荐套餐的下拉框和文字框
        JLabel cancelwarn = new JLabel("（只能退订当前套餐哦，一经使用套餐权力，无法退订，只有未使用权限才能退订）");
        cancelwarn.setBounds(260, 560, 500, 30);
        cancelwarn.setForeground(Color.red);
        frame.getContentPane().add(cancelwarn);



        // 推荐航班标题
        JLabel label7 = new JLabel("<html><font color='rgb(173,193,212')>以下是我们根据您的个人偏好为您推荐的近日航班:</font></html>");
        label7.setBounds(50, 600, 850, 30); // 向下移动
        label7.setFont(new Font("Microsoft YaHei", Font.BOLD, 16));
        frame.getContentPane().add(label7);

        JButton button_2 = new JButton("返回");
        button_2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
                Research wResearch = new Research();
                wResearch.getFrame().setVisible(true);
            }
        });
        button_2.setFont(new Font("宋体", Font.PLAIN, 12));
        button_2.setBounds(376, 700, 100, 20);
        frame.getContentPane().add(button_2);

        if((flights==null?0:flights.length)<(flight1s==null?0:flight1s.length)){
        }


        // 调用 createRadarChart 函数生成雷达图
        createRadarChart(frame, morningCount, afternoonCount, eveningCount,
                summerVacationCount, holidayCount, workdayCount,(flights==null?0:flights.length),(flight1s==null?0:flight1s.length));


    }

    // FlightRecommendation 类中
    private static String getPackageDetails(String packageName) {
        switch (packageName) {
            case "国内随心飞":
                return "套餐内容:\n- 国内航班10次\n- 有效期：无限时期！！\n- 全场75折扣，最高立500元";
            case "国外随心飞":
                return "套餐内容:\n- 国际航班10次\n- 有效期：无限时期！！\n- 全场：1000元！！\n- 适用于热门国际航线";
            case "学生寒暑假":
                return "套餐内容:\n- 学生专属优惠只要400元！！！\n- 有效期：寒暑假期间！！\n- 限定次数：4次\n- 适用于所有国内航线";
            default:
                return "无相关信息";
        }
    }

    // 判断是否是寒暑假
    public static boolean isSummerVacation(String date) {
        // 提取日期部分 YYYY-MM-DD
        String[] parts = date.split("-");
        int month = Integer.parseInt(parts[1]); // 提取月份部分
        return (month >= 7 && month <= 8) || (month == 1 || month == 2);
    }


    // 判断是否是节假日
    public static boolean isHoliday(String date) {
        // 假设节假日为固定日期
        String[] holidays = {"2024-01-01", "2024-05-01", "2024-10-01"}; // 示例假期
        String formattedDate = date.substring(0, 10); // 提取 YYYY-MM-DD 部分
        for (String holiday : holidays) {
            if (holiday.equals(formattedDate)) {
                return true;
            }
        }
        return false;
    }

    private void updateStatistics(Flight[] flights) {
        // 清空之前的统计数据
        destinationCount.clear();
        summerVacationCount = 0;
        holidayCount = 0;
        workdayCount = 0;
        morningCount = 0;
        afternoonCount = 0;
        eveningCount = 0;
        minPrice = Float.MAX_VALUE;
        maxPrice = Float.MIN_VALUE;
        mostFrequentDestination = null;
        maxDestinationCount = 0;

        // 重新统计数据
        if (flights != null && flights.length > 0) {
            for (Flight flight : flights) {
                // 统计目的地
                String destination = flight.getArrivalCity();
                destinationCount.put(destination, destinationCount.getOrDefault(destination, 0) + 1);

                // 出发日期分类
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
                String startTime = flight.getStartTime().format(formatter);
                String[] timeParts = startTime.split("-");
                String date = timeParts[0] + "-" + timeParts[1] + "-" + timeParts[2];
                int hour = Integer.parseInt(timeParts[3]);

                if (isSummerVacation(date)) summerVacationCount++;
                else if (isHoliday(date)) holidayCount++;
                else workdayCount++;

                // 起飞时间段统计
                if (hour < 12) morningCount++;
                else if (hour < 18) afternoonCount++;
                else eveningCount++;

                // 价格区间统计
                float price = flight.getPrice();
                minPrice = Math.min(minPrice, price);
                maxPrice = Math.max(maxPrice, price);
            }

            // 找到最常落地的城市
            for (Map.Entry<String, Integer> entry : destinationCount.entrySet()) {
                if (entry.getValue() > maxDestinationCount) {
                    mostFrequentDestination = entry.getKey();
                    maxDestinationCount = entry.getValue();
                }
            }
        } else {
            minPrice = 0;
            maxPrice = 0;
        }


        label8.setText("<html>其中，您最常落地的城市是: <font color='rgb(173,193,212')>"
                + mostFrequentDestination + "</font>，有 <font color='rgb(173,193,212')>"
                + maxDestinationCount + "</font> 次</html>");

        label3.setText("<html>您的航班出发日期在:<br>寒暑假: <font color='rgb(173,193,212')>"
                + summerVacationCount + "</font> 次，<br>节假日: <font color='rgb(173,193,212')>"
                + holidayCount + "</font> 次，<br>工作日: <font color='rgb(173,193,212')>"
                + workdayCount + "</font> 次</html>");

        label2.setText("<html>您的航班起飞时间在:<br>早晨 (00:00-11:59): <font color='rgb(173,193,212')>"
                + morningCount + "</font> 次，<br>下午 (12:00-17:59): <font color='rgb(173,193,212')>"
                + afternoonCount + "</font> 次，<br>晚上 (18:00-23:59): <font color='rgb(173,193,212')>"
                + eveningCount + "</font> 次</html>");

        label4.setText("<html>您的航班历史最低价格: <font color='rgb(173,193,212')>"
                + minPrice + "</font> 元，<br>历史最高价格: <font color='rgb(173,193,212')>"
                + maxPrice + "</font> 元</html>");
    }

    private void recommendFlights(Flight[] flights) {
        // 清除之前的推荐内容
        clearPreviousRecommendations();

        if (flights == null || flights.length == 0) {
            JOptionPane.showMessageDialog(frame, "未找到符合条件的航班！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 初始化用户偏好
        Map<String, Double> timePreference = new HashMap<>();
        int all =morningCount+afternoonCount+eveningCount;
        timePreference.put("Morning", (double) (morningCount/all));
        timePreference.put("Afternoon", (double) (afternoonCount/all));
        timePreference.put("Evening", (double) (eveningCount/all));

        FlightRecommendationAlgorithm algorithm = new FlightRecommendationAlgorithm(
                timePreference,
                minPrice,
                maxPrice,
                mostFrequentDestination
        );

        // 对每个航班计算分数
        List<FlightScore> flightScores = new ArrayList<>();
        for (Flight flight : flights) {
            double score = algorithm.calculateFlightScore(flight);
            flightScores.add(new FlightScore(flight, score));
        }

        // 按分数降序排序
        flightScores.sort((a, b) -> Double.compare(b.score, a.score));

        // 取前3个航班
        StringBuilder recommendedFlights = new StringBuilder("<html><ul>");
        for (int i = 0; i < Math.min(3, flightScores.size()); i++) {
            Flight flight = flightScores.get(i).flight;
            recommendedFlights.append("<li>")
                    .append("航班号: ").append(flight.getFlightName())
                    .append(", 出发: ").append(flight.getStartCity())
                    .append(", 到达: ").append(flight.getArrivalCity())
                    .append(", 起飞时间: ").append(flight.getStartTime())
                    .append(", 价格: ").append(flight.getPrice())
                    .append("</li>");
        }
        recommendedFlights.append("</ul></html>");

        // 更新显示
        JLabel recommendedLabel = new JLabel(recommendedFlights.toString());
        recommendedLabel.setBounds(50, 620, 850, 80);
        recommendedLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 14));
        recommendedLabel.setName("recommendationLabel"); // 设置标识，方便清除时定位
        frame.getContentPane().add(recommendedLabel);
        frame.repaint();
    }

    private void clearPreviousRecommendations() {
        // 遍历 Frame 的内容面板，移除所有名为 "recommendationLabel" 的组件
        Component[] components = frame.getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JLabel && "recommendationLabel".equals(((JLabel) component).getName())) {
                frame.getContentPane().remove(component);
            }
        }
        frame.revalidate(); // 重新验证布局
        frame.repaint(); // 刷新界面
    }


    // 辅助类用于存储航班和得分
    private static class FlightScore {
        Flight flight;
        double score;

        FlightScore(Flight flight, double score) {
            this.flight = flight;
            this.score = score;
        }
    }



    public void createRadarChart(JFrame frame, int morningCount, int afternoonCount, int eveningCount,
                                 int summerVacationCount, int holidayCount, int workdayCount,
                                 int domesticCount, int internationalCount) {
        // 创建数据集
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(morningCount, "Flight Count", "早晨 (00:00-11:59)");
        dataset.addValue(afternoonCount, "Flight Count", "下午 (12:00-17:59)");
        dataset.addValue(eveningCount, "Flight Count", "晚上 (18:00-23:59)");
        dataset.addValue(summerVacationCount, "Flight Count", "暑假");
        dataset.addValue(holidayCount, "Flight Count", "节假日");
        dataset.addValue(workdayCount, "Flight Count", "工作日");
        dataset.addValue(domesticCount, "Flight Count", "国内航班");
        dataset.addValue(internationalCount, "Flight Count", "国际航班");

        // 使用 SpiderWebPlot 创建雷达图
        SpiderWebPlot plot = new SpiderWebPlot(dataset);
        JFreeChart radarChart = new JFreeChart("航班数据分布", JFreeChart.DEFAULT_TITLE_FONT, plot, false);

        // 创建 ChartPanel 并设置大小和位置
        ChartPanel chartPanel = new ChartPanel(radarChart);
        chartPanel.setBounds(500, 50, 400, 300); // 设置图表在右上角，宽高为300x300
        frame.getContentPane().add(chartPanel);
    }


}




class FlightRecommendationAlgorithm {
    private Map<String, Double> timePreference; // 时间偏好 {"Morning": 0.6, "Afternoon": 0.3, "Evening": 0.1}
    private double minPrice; // 用户历史最低价格
    private double maxPrice; // 用户历史最高价格
    private String frequentDestinations; // 用户常去目的地
    private boolean prefersDirectFlight; // 是否偏好直飞

    public FlightRecommendationAlgorithm(Map<String, Double> timePreference,
                                         double minPrice,double maxPrice,String frequentDestinations) {
        // 初始化用户偏好
        this.timePreference = timePreference;
        this.minPrice = minPrice;
        this.maxPrice =  maxPrice;
        this.frequentDestinations = frequentDestinations;
        this.prefersDirectFlight = analyzeTransitPreference();
    }


    // 计算单个航班的得分
    double calculateFlightScore(Flight flight) {
        // 权重配置
        double w1 = 0.4, w2 = 0.3, w3 = 0.2, w4 = 0.1;

        // 时间偏好得分
        String timePeriod = getTimePeriod(flight.getStartTime());
        double timeScore = timePreference.getOrDefault(timePeriod, 0.0);

        // 价格偏好得分
        double price = flight.getPrice();
        double priceScore = calculatePriceScore(price);

        // 目的地偏好得分
        String destination = flight.getArrivalCity();
        double destinationScore = frequentDestinations.contains(destination) ? 1.0 : 0.0;

        // 中转偏好得分
        double transitScore = prefersDirectFlight ? 1.0 : 0.0;

        // 综合得分
        return w1 * timeScore + w2 * priceScore + w3 * destinationScore + w4 * transitScore;
    }

    // 时间段解析
    private String getTimePeriod(LocalDateTime startTime) {
        int hour = startTime.getHour(); // 从 LocalDateTime 获取小时
        if (hour >= 6 && hour < 12) return "Morning";
        if (hour >= 12 && hour < 18) return "Afternoon";
        return "Evening";
    }

    // 价格偏好得分计算
    private double calculatePriceScore(double price) {
        if (price < minPrice) return 1.0; // 最符合用户低价偏好
        if (price > maxPrice) return 0.0; // 超出用户高价范围
        return 1 - (price - minPrice) / (maxPrice - minPrice); // 按比例降低分数
    }


    // 分析中转偏好
    private boolean analyzeTransitPreference() {
        // 模拟分析，实际需要从 Transit 表中查询用户数据
        DbSelect d = new DbSelect();
        boolean ht = d.hasTransitData(Login.PassengerId);
        return ht; // 假设用户偏好直飞
    }

    // 测试主方法
    public static void main(String[] args) {
        // 示例航班数据
        Flight flight1 = new Flight(
                1,
                "2024-12-06-08-00-00", // 起飞时间
                "2024-12-06-10-30-00", // 降落时间
                "Beijing", // 起始城市
                "Shanghai", // 到达城市
                "2024-12-06", // 出发日期
                800.0f, // 价格
                120, // 当前乘客数
                150, // 座位容量
                "Direct", // 航班状态
                "12345", // 乘客ID
                "AirChina 101" // 航班名称
        );


        Map<String, Double> T = new HashMap<>();
        T.put("Morning", 0.6);
        T.put("Afternoon", 0.3);
        T.put("Evening", 0.1);

        // 初始化推荐系统
        FlightRecommendationAlgorithm recommendation = new FlightRecommendationAlgorithm(
                T,200,2000,"北京");

        double core = recommendation.calculateFlightScore(flight1);
        System.out.println(core);

    }
}


