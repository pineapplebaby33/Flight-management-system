/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightinfo;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.LocalDate;

public class FlightGraph {
    // 使用一个 Map 存储航班图(邻接表)，其中键是起点城市的名称，值是一个从该城市出发的航班列表
    private Map<String, List<FlightInfo>> adjList = new HashMap<>();

    // 从指定文件路径读取航班信息并将其存储到航班图中
    public void loadFlightsFromFile(String filePath) throws IOException {
        // 创建一个 BufferedReader 来读取文件
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line; // 用于存储每一行读取的数据

        // 逐行读取文件，直到文件末尾
        while ((line = br.readLine()) != null) {
            // 使用逗号和空格分隔符将每一行的数据分割为数组
            String[] data = line.split(", ");
            String flightNumber = data[0]; // 航班号
            String fromCity = data[1];     // 起点城市
            String toCity = data[2];       // 终点城市
            String airline = data[3];      // 航空公司
            double price = Double.parseDouble(data[4]); // 票价，将字符串转为 double
            int departureTime = convertTimeToMinutes(data[5]); // 起飞时间，转换为分钟
            int arrivalTime = convertTimeToMinutes(data[6]);   // 到达时间，转换为分钟
            
            // 创建一个 FlightInfo 对象，存储该航班的详细信息
            FlightInfo flight = new FlightInfo(flightNumber, fromCity, toCity, airline, price, departureTime, arrivalTime);
            
            // 如果起点城市还没有在图中，则为它创建一个新的 ArrayList
            adjList.putIfAbsent(fromCity, new ArrayList<>());
            
            // 将航班信息添加到起点城市对应的航班列表中
            adjList.get(fromCity).add(flight);
        }

        // 关闭 BufferedReader
        br.close();
    }

    // 将时间字符串（格式为 "HH:mm"）转换为总分钟数，方便计算
    private int convertTimeToMinutes(String time) {
        // 使用冒号分割时间字符串为小时和分钟
        String[] parts = time.split(":");
        // 计算总分钟数，小时乘以 60 加上分钟
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    // 返回从指定城市出发的所有航班的列表，如果城市不存在则返回一个空列表
    public List<FlightInfo> getFlightsFromCity(String city) {
        return adjList.getOrDefault(city, new ArrayList<>());
    }
}
