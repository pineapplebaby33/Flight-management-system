package frame;

import flight.Order;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightMapGenerator {

    // 模拟地理编码服务，城市到经纬度的映射
    private static final Map<String, double[]> cityCoordinates = new HashMap<>();

    static {
        // 示例城市及其经纬度（您需要将这些替换为实际的城市和经纬度）
        cityCoordinates.put("北京", new double[]{39.9042, 116.4074});
        cityCoordinates.put("上海", new double[]{31.2304, 121.4737});
        cityCoordinates.put("天津", new double[]{39.3434, 117.3616});
        cityCoordinates.put("重庆", new double[]{29.5630, 106.5516});
        cityCoordinates.put("哈尔滨", new double[]{45.8038, 126.5349});
        cityCoordinates.put("长春", new double[]{43.8171, 125.3235});
        cityCoordinates.put("沈阳", new double[]{41.8057, 123.4315});
        cityCoordinates.put("呼和浩特", new double[]{40.8415, 111.7519});
        cityCoordinates.put("石家庄", new double[]{38.0428, 114.5149});
        cityCoordinates.put("乌鲁木齐", new double[]{43.8256, 87.6168});
        cityCoordinates.put("兰州", new double[]{36.0611, 103.8343});
        cityCoordinates.put("西宁", new double[]{36.6171, 101.7782});
        cityCoordinates.put("西安", new double[]{34.3416, 108.9398});
        cityCoordinates.put("银川", new double[]{38.4872, 106.2309});
        cityCoordinates.put("郑州", new double[]{34.7466, 113.6254});
        cityCoordinates.put("济南", new double[]{36.6512, 117.1201});
        cityCoordinates.put("太原", new double[]{37.8706, 112.5489});
        cityCoordinates.put("合肥", new double[]{31.8206, 117.2272});
        cityCoordinates.put("长沙", new double[]{28.2282, 112.9388});
        cityCoordinates.put("武汉", new double[]{30.5928, 114.3055});
        cityCoordinates.put("南京", new double[]{32.0603, 118.7969});
        cityCoordinates.put("成都", new double[]{30.5728, 104.0668});
        cityCoordinates.put("贵阳", new double[]{26.6470, 106.6302});
        cityCoordinates.put("昆明", new double[]{25.0406, 102.7123});
        cityCoordinates.put("南宁", new double[]{22.8170, 108.3669});
        cityCoordinates.put("拉萨", new double[]{29.6525, 91.1721});
        cityCoordinates.put("杭州", new double[]{30.2741, 120.1551});
        cityCoordinates.put("南昌", new double[]{28.6820, 115.8579});
        cityCoordinates.put("广州", new double[]{23.1291, 113.2644});
        cityCoordinates.put("福州", new double[]{26.0745, 119.2965});
        cityCoordinates.put("台北", new double[]{25.0330, 121.5654});
        cityCoordinates.put("海口", new double[]{20.0440, 110.1999});
        cityCoordinates.put("香港", new double[]{22.3193, 114.1694});
        cityCoordinates.put("澳门", new double[]{22.1987, 113.5439});
        cityCoordinates.put("深圳", new double[]{22.5431, 114.0579});
        cityCoordinates.put("纽约", new double[]{40.7128, -74.0060});
        cityCoordinates.put("伦敦", new double[]{51.5074, -0.1278});
        cityCoordinates.put("巴黎", new double[]{48.8566, 2.3522});
        cityCoordinates.put("柏林", new double[]{52.5200, 13.4050});
        cityCoordinates.put("阿姆斯特丹", new double[]{52.3676, 4.9041});
        cityCoordinates.put("慕尼黑", new double[]{48.1351, 11.5820});
        cityCoordinates.put("罗马", new double[]{41.9028, 12.4964});
        cityCoordinates.put("东京", new double[]{35.6895, 139.6917});
        cityCoordinates.put("首尔", new double[]{37.5665, 126.9780});
        cityCoordinates.put("曼谷", new double[]{13.7563, 100.5018});
        cityCoordinates.put("悉尼", new double[]{-33.8688, 151.2093});
        cityCoordinates.put("奥克兰", new double[]{-36.8485, 174.7633});
        cityCoordinates.put("温哥华", new double[]{49.2827, -123.1207});
        cityCoordinates.put("莫斯科", new double[]{55.7558, 37.6176});
        cityCoordinates.put("芝加哥", new double[]{41.8781, -87.6298});
        cityCoordinates.put("洛杉矶", new double[]{34.0522, -118.2437});
        cityCoordinates.put("新加坡", new double[]{1.3521, 103.8198});
        cityCoordinates.put("旧金山", new double[]{37.7749, -122.4194});

    }

    // 创建航班记录的 HTML 文件并自动打开浏览器
    public void generateMap(List<Order> flights) throws IOException {
        // HTML 模板
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("""
                <!DOCTYPE html>
                <html lang="zh">
                <head>
                    <meta charset="UTF-8">
                    <title>航班路线图</title>
                    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css" />
                    <style>
                        #map { height: 100vh; width: 100%; }
                    </style>
                </head>
                <body>
                    <div id="map"></div>
                    <script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"></script>
                    <script src="leaflet.curve.js"></script>
                    <script>
                        var map = L.map('map').setView([20.0, 0.0], 2);
                        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                            maxZoom: 18,
                            attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                        }).addTo(map);

                        var flights = [
                """);

        // 将每个航班转换为 JavaScript 对象
        for (Order flight : flights) {
            // 假设 Order 类中包含以下方法：
            // getFlightId() 返回航班编号
            // getPassengerId() 返回乘客编号
            // getStartCity() 和 getArrivalCity() 返回中文城市名
            // getCreateDate() 返回订单创建时间
            // getStatus() 返回订单状态

            String startCity = flight.getFlightId().getStartCity();
            String endCity = flight.getFlightId().getArrivalCity();
            double[] startCoords = cityCoordinates.getOrDefault(startCity, new double[]{0.0, 0.0});
            double[] endCoords = cityCoordinates.getOrDefault(endCity, new double[]{0.0, 0.0});

            htmlContent.append(String.format(
                    "{start: [%f, %f], end: [%f, %f], startCity: '%s', endCity: '%s', info: '航班号: %s, 出发城市: %s, 到达城市: %s, 起飞时间: %s, 降落时间: %s'},\n",
                    startCoords[0], startCoords[1], endCoords[0], endCoords[1],
                    startCity, endCity,
                    flight.getFlightId().getFlightName(),
                    startCity,
                    endCity,
                    flight.getFlightId().getStartTime(),
                    flight.getFlightId().getArrivalTime()
            ));
        }

        htmlContent.append("""
                        ];

                        // 绘制航班路线
                        flights.forEach(function(flight) {
                            var startMarker = L.marker(flight.start).addTo(map)
                                .bindPopup("出发: " + flight.startCity);
                            var endMarker = L.marker(flight.end).addTo(map)
                                .bindPopup("到达: " + flight.endCity);

                            // 创建带弧度的贝塞尔曲线
                            var latlngs = [
                                'M', flight.start,
                                'Q', [((flight.start[0] + flight.end[0]) / 2) + 10, ((flight.start[1] + flight.end[1]) / 2)],
                                flight.end
                            ];

                            var curvedRoute = L.curve(latlngs, {color: 'pink', weight: 2})
                                .on('mouseover', function(e) {
                                    this.setStyle({color: 'blue', weight: 4});
                                    var popup = L.popup()
                                        .setLatLng(e.latlng)
                                        .setContent(flight.info)
                                        .openOn(map);
                                })
                                .on('mouseout', function(e) {
                                    this.setStyle({color: 'pink', weight: 2});
                                    map.closePopup();
                                })
                                .addTo(map);
                        });
                    </script>
                </body>
                </html>
                """);

        // 保存 HTML 文件
        File file = new File("FlightMap.html");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(htmlContent.toString());
        }

        // 自动打开浏览器显示地图
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(file.toURI());
        }
    }

    public static void main(String[] args) throws IOException {
        // 创建测试订单
        List<Order> testOrders = new ArrayList<>();
        testOrders.add(new Order(1, 1, "12A", 1, "2016-03-04-05-20-01", "PAID", true));
        testOrders.add(new Order(2, 2, "44E", 2, "2024-11-12-10-00-00", "PAID", true));

        // 生成地图
        new FlightMapGenerator().generateMap(testOrders);
    }
}
