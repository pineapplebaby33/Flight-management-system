package frame;
import flight.DbSelect;
import flight.Flight;

import java.util.*;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
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

import java.awt.Window;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;


import javax.swing.JTextField;
import javax.swing.JComboBox;



public class FlightRecommendation {
    private JFrame frame;
    private JTable Flight_Table;
    private JScrollPane scrollPane;
    static boolean isDomestic =true;
    private Flight[] currentFlights; // 新增：用于保存当前表格中显示的航班数据
    private boolean HasOrderPackage = false;

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
        Flight[] filghts = new DbSelect().FlightSelectByPassengerId(12,true);
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
        Flight[] flights = new DbSelect().FlightSelectByPassengerId(12, true);
        Flight[] flight1s = new DbSelect().FlightSelectByPassengerId(12, false);

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
        //遍历收集信息
        String mostFrequentDestination = null;
        int maxDestinationCount = 0;

        if (flights != null && flights.length > 0) {
            /*
            // 初始化统计变量
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

             */

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
                float price = flight.getPrice();
                if (price < minPrice) minPrice = price;
                if (price > maxPrice) maxPrice = price;
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
            System.out.println("您预定了国内航班：" + flights.length + "次");
            System.out.println("其中，您最常落地的城市是：" + mostFrequentDestination + "，有 " + maxDestinationCount + " 次");
            System.out.println("您的航班出发日期在寒暑假：" + summerVacationCount + " 次，节假日：" + holidayCount + " 次，在工作日：" + workdayCount + " 次");
            System.out.println("您的航班历史最低价格：" + minPrice + " 元，历史最高价格：" + maxPrice + " 元");
            System.out.println("您的航班起飞时间分布如下：");
            System.out.println("早晨 (00:00-11:59): " + morningCount + " 次");
            System.out.println("下午 (12:00-17:59): " + afternoonCount + " 次");
            System.out.println("晚上 (18:00-23:59): " + eveningCount + " 次");
        } else {
            System.out.println("No flights found.");
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

        // 问候语，天蓝色字体
        JLabel label0 = new JLabel("亲爱的 " + p.getRealName() + " 乘客您好！感谢与您的相遇！在为您服务期间：");
        label0.setBounds(50, 20, 850, 40); // 调整宽度和高度
        label0.setFont(new Font("Microsoft YaHei", Font.BOLD, 18));
        label0.setForeground(new Color(173, 193, 212));
        frame.getContentPane().add(label0);

        // 国内外偏好
        JLabel label1 = new JLabel("<html>您预定了国内航班: <font color='rgb(173,193,212)'>" + flights.length
                + "</font> 次，国际航班: <font color='rgb(173,193,212)'>" + flight1s.length + "</font> 次</html>");
        label1.setBounds(50, 70, 850, 30); // 适当下移
        label1.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        label1.setForeground(Color.BLACK);
        frame.getContentPane().add(label1);

        // 最常落地城市
        JLabel label8 = new JLabel("<html>其中，您最常落地的城市是: <font color='rgb(173,193,212)'>"
                + mostFrequentDestination + "</font>，有 <font color='rgb(173,193,212)'>" + maxDestinationCount + "</font> 次</html>");
        label8.setBounds(50, 110, 850, 30);
        label8.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        label8.setForeground(Color.BLACK);
        frame.getContentPane().add(label8);

        // 出发日期偏好
        JLabel label3 = new JLabel("<html>您的航班出发日期在:<br>寒暑假: <font color='rgb(173,193,212')>"
                + summerVacationCount + "</font> 次，<br>节假日: <font color='rgb(173,193,212')>" + holidayCount
                + "</font> 次，<br>工作日: <font color='rgb(173,193,212')>" + workdayCount + "</font> 次</html>");
        label3.setBounds(50, 150, 850, 83); // 增加垂直间距
        label3.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        label3.setForeground(Color.BLACK);
        frame.getContentPane().add(label3);

        // 时间偏好
        JLabel label2 = new JLabel("<html>您的航班起飞时间在:<br>早晨 (00:00-11:59): <font color='rgb(173,193,212')>"
                + morningCount + "</font> 次，<br>下午 (12:00-17:59): <font color='rgb(173,193,212')>" + afternoonCount
                + "</font> 次，<br>晚上 (18:00-23:59): <font color='rgb(173,193,212')>" + eveningCount + "</font> 次</html>");
        label2.setBounds(50, 245, 850, 83); // 向下调整位置
        label2.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        label2.setForeground(Color.BLACK);
        frame.getContentPane().add(label2);

        // 历史最低和最高价格
        JLabel label4 = new JLabel("<html>您的航班历史最低价格: <font color='rgb(173,193,212')>"
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

        // 初始化套餐内容
        packageDetails.setText(FlightRecommendation.getPackageDetails("国内随心飞"));


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

        // 推荐航班标题
        JLabel label7 = new JLabel("<html><font color='rgb(173,193,212')>以下是我们根据您的个人偏好为您推荐的近日航班:</font></html>");
        label7.setBounds(50, 640, 850, 30); // 向下移动
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

        // 调用 createRadarChart 函数生成雷达图
        createRadarChart(frame, morningCount, afternoonCount, eveningCount,
                summerVacationCount, holidayCount, workdayCount,flights.length,flight1s.length);


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
        chartPanel.setBounds(500, 50, 350, 300); // 设置图表在右上角，宽高为300x300
        frame.getContentPane().add(chartPanel);
    }



}


class FlightRecommendationAlgorithm {
    private Map<String, Double> timePreference; // 时间偏好 {"Morning": 0.6, "Afternoon": 0.3, "Evening": 0.1}
    private double minPrice; // 用户历史最低价格
    private double maxPrice; // 用户历史最高价格
    private List<String> frequentDestinations; // 用户常去目的地
    private boolean prefersDirectFlight; // 是否偏好直飞

    public FlightRecommendationAlgorithm(String passengerId) {
        // 初始化用户偏好
        this.timePreference = analyzeTimePreference(passengerId);
        this.minPrice = analyzePriceRange(passengerId)[0];
        this.maxPrice = analyzePriceRange(passengerId)[1];
        this.frequentDestinations = analyzeFrequentDestinations(passengerId);
        this.prefersDirectFlight = analyzeTransitPreference(passengerId);
    }

    // 计算推荐航班得分
    public String recommendFlight(List<Map<String, Object>> flights) {
        double bestScore = -1;
        String bestFlightId = null;

        for (Map<String, Object> flight : flights) {
            double score = calculateFlightScore(flight);
            if (score > bestScore) {
                bestScore = score;
                bestFlightId = flight.get("FlightId").toString();
            }
        }

        return bestFlightId;
    }

    // 计算单个航班的得分
    private double calculateFlightScore(Map<String, Object> flight) {
        // 权重配置
        double w1 = 0.4, w2 = 0.3, w3 = 0.2, w4 = 0.1;

        // 时间偏好得分
        String departureTime = flight.get("DepartureTime").toString();
        double timeScore = timePreference.getOrDefault(getTimePeriod(departureTime), 0.0);

        // 价格偏好得分
        double price = Double.parseDouble(flight.get("Price").toString());
        double priceScore = calculatePriceScore(price);

        // 目的地偏好得分
        String destination = flight.get("Destination").toString();
        double destinationScore = frequentDestinations.contains(destination) ? 1.0 : 0.0;

        // 中转偏好得分
        boolean isDirect = (boolean) flight.get("IsDirect");
        double transitScore = prefersDirectFlight == isDirect ? 1.0 : 0.0;

        // 综合得分
        return w1 * timeScore + w2 * priceScore + w3 * destinationScore + w4 * transitScore;
    }

    // 时间段转换
    private String getTimePeriod(String departureTime) {
        int hour = Integer.parseInt(departureTime.split(":")[0]);
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

    // 分析时间偏好
    private Map<String, Double> analyzeTimePreference(String passengerId) {
        // 模拟分析，实际需要从数据库中查询用户航班数据
        Map<String, Double> preference = new HashMap<>();
        preference.put("Morning", 0.6);
        preference.put("Afternoon", 0.3);
        preference.put("Evening", 0.1);
        return preference;
    }

    // 分析价格范围
    private double[] analyzePriceRange(String passengerId) {
        // 模拟分析，实际需要从数据库中查询用户航班数据
        return new double[]{100.0, 500.0}; // 假设价格范围为100-500
    }

    // 分析用户常去目的地
    private List<String> analyzeFrequentDestinations(String passengerId) {
        // 模拟分析，实际需要从数据库中查询用户航班数据
        return Arrays.asList("New York", "Tokyo", "London");
    }

    // 分析中转偏好
    private boolean analyzeTransitPreference(String passengerId) {
        // 模拟分析，实际需要从 Transit 表中查询用户数据
        return true; // 假设用户偏好直飞
    }
    public static void main(String[] args) {
        // 模拟航班数据
        List<Map<String, Object>> flights = new ArrayList<>();
        Map<String, Object> flight1 = Map.of("FlightId", "1001", "DepartureTime", "08:00", "Price", 200.0, "Destination", "New York", "IsDirect", true);
        Map<String, Object> flight2 = Map.of("FlightId", "1002", "DepartureTime", "15:00", "Price", 150.0, "Destination", "Tokyo", "IsDirect", false);
        flights.add(flight1);
        flights.add(flight2);

        // 初始化推荐系统
        FlightRecommendationAlgorithm recommendation = new FlightRecommendationAlgorithm("12345");



        // 获取推荐航班
        String bestFlightId = recommendation.recommendFlight(flights);
        System.out.println("Recommended Flight ID: " + bestFlightId);
    }

}
