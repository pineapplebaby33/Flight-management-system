/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightinfo;
import java.util.*;
/**
 *
 * @author shan
 */

public class FlightSearch {
    private FlightGraph flightGraph;

    // 构造函数，接受一个 FlightGraph 对象作为参数
    // FlightGraph 是航班信息的图结构，存储了航班的所有节点和边
    public FlightSearch(FlightGraph flightGraph) {
        this.flightGraph = flightGraph;
    }

    // 计算最快和最便宜的航班路径并同时返回
    // startCity：起始城市
    // destinationCity：目的地城市
    public FlightPathResult findPaths(String startCity, String destinationCity) {
        // 调用 findShortestPath 方法，byTime 为 true 表示按时间找最快路径
        List<FlightInfo> fastestPath = findShortestPath(startCity, destinationCity, true);  // 最快路径
        // 调用 findShortestPath 方法，byTime 为 false 表示按价格找最便宜路径
        List<FlightInfo> cheapestPath = findShortestPath(startCity, destinationCity, false);  // 最便宜路径
        
        // 返回一个 FlightPathResult 对象，包含最快路径和最便宜路径
        return new FlightPathResult(fastestPath, cheapestPath);
    }

    // 使用 Dijkstra 算法查找最快或最便宜的路径
    // byTime 参数为 true 表示按飞行时间计算最短路径，false 表示按票价计算最短路径
    private List<FlightInfo> findShortestPath(String startCity, String destinationCity, boolean byTime) {
        
        // 初始化
        // 优先队列(最小堆)
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> 
            Double.compare(a.timeOrPrice, b.timeOrPrice)
        );
        // 存储每个城市到起始城市的最短（飞行时间或价格）
        Map<String, Double> dist = new HashMap<>();
        // 存储每个城市的上一个飞往它的航班信息，以便重建路径
        Map<String, FlightInfo> previousFlight = new HashMap<>();
        
        // 将起始城市加入优先队列，起始城市到自己的距离为 0
        pq.add(new Node(startCity, 0));
        dist.put(startCity, 0.0);
        
        // 遍历优先队列，直到处理完所有节点或找到目标城市
        while (!pq.isEmpty()) {
            Node node = pq.poll(); // 从优先队列中取出当前距离最短的节点
            String city = node.city; // 当前城市的名称
            
            // 如果当前城市是目的地城市，则结束搜索
            //city.equals(...) 用于比较两个字符串是否相等
            if (city.equals(destinationCity)) break;
            
            // 获取当前城市的所有航班信息
            for (FlightInfo flight : flightGraph.getFlightsFromCity(city)) {
                String nextCity = flight.getToCity(); // 航班的目的地城市
                // 根据 byTime 决定使用飞行时间还是票价来计算距离
                double timeOrPrice = byTime ? flight.getFlightDuration() : flight.getPrice();
                
                // 计算从起始城市到下一个城市的总距离
                double newDist = dist.get(city) + timeOrPrice;
                // 如果这个新距离比之前记录的距离短，更新该城市的最短距离
                //确保无论 nextCity 是否已经在 dist 中有记录，都能返回一个合理的值
                if (newDist < dist.getOrDefault(nextCity, Double.MAX_VALUE)) {
                    dist.put(nextCity, newDist); // 更新最短距离
                    pq.add(new Node(nextCity, newDist)); // 将下一个城市加入优先队列
                    previousFlight.put(nextCity, flight); // 记录到达该城市的航班
                }
            }
        }
        
        // 从 destinationCity 到 startCity 重建路径
        List<FlightInfo> path = new ArrayList<>();
        String city = destinationCity;
        // 根据 previousFlight 回溯航班路径
        while (previousFlight.containsKey(city)) {
            FlightInfo flight = previousFlight.get(city); // 获取到达该城市的航班
            path.add(0, flight); // 将航班添加到路径的开头
            city = flight.getFromCity(); // 继续回溯到上一个城市
        }
        return path; // 返回完整的路径
    }

    // Node 类，用于在优先队列中存储城市名称和到达该城市的距离
    private static class Node {
        String city; // 城市名称
        double timeOrPrice; // 到达该城市的距离（飞行时间或价格）

        // 构造函数，初始化城市和距离
        Node(String city, double timeOrPrice) {
            this.city = city;
            this.timeOrPrice = timeOrPrice;
        }
    }
}
