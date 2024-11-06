package frame;

import flight.DbSelect;
import flight.Flight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

// 定义航班图结构类
class FlightGraph {
    private Map<String, List<Flight>> adjList = new HashMap<>();

    public void addFlight(Flight flight) {
        adjList.computeIfAbsent(flight.getStartCity(), k -> new ArrayList<>()).add(flight);
    }

    public List<Flight> getFlightsFromCity(String city) {
        return adjList.getOrDefault(city, new ArrayList<>());
    }

    public List<List<Flight>> findAllPaths(String start, String destination) {
        List<List<Flight>> paths = new ArrayList<>();
        List<Flight> path = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        findAllPathsUtil(start, destination, visited, path, paths);
        return paths;
    }

    private void findAllPathsUtil(String current, String destination, Set<String> visited,
                                  List<Flight> path, List<List<Flight>> paths) {
        visited.add(current);
        if (current.equals(destination)) {
            paths.add(new ArrayList<>(path));
        } else {
            for (Flight flight : getFlightsFromCity(current)) {
                if (!visited.contains(flight.getArrivalCity())) {
                    path.add(flight);
                    findAllPathsUtil(flight.getArrivalCity(), destination, visited, path, paths);
                    path.remove(path.size() - 1);
                }
            }
        }
        visited.remove(current);
    }
}

// 定义搜索结果类
class FlightPathResult {
    private List<Flight> fastestPath;
    private List<Flight> cheapestPath;

    public FlightPathResult(List<Flight> fastestPath, List<Flight> cheapestPath) {
        this.fastestPath = fastestPath;
        this.cheapestPath = cheapestPath;
    }

    public List<Flight> getFastestPath() { return fastestPath; }
    public List<Flight> getCheapestPath() { return cheapestPath; }
}

// 航班搜索类
class FlightSearch {
    private FlightGraph flightGraph;

    public FlightSearch(FlightGraph flightGraph) {
        this.flightGraph = flightGraph;
    }

    public FlightPathResult findPaths(String startCity, String destinationCity) {
        List<Flight> fastestPath = findPath(startCity, destinationCity, true); // true for time comparison
        List<Flight> cheapestPath = findPath(startCity, destinationCity, false); // false for price comparison
        return new FlightPathResult(fastestPath, cheapestPath);
    }

    private List<Flight> findPath(String startCity, String destinationCity, boolean byTime) {
        List<List<Flight>> allPaths = flightGraph.findAllPaths(startCity, destinationCity);

        return allPaths.stream()
                .min(Comparator.comparingDouble(path -> calculatePathCost(path, byTime)))
                .orElse(new ArrayList<>());
    }

    private double calculatePathCost(List<Flight> path, boolean byTime) {
        double totalCost = 0.0;

        if (byTime) {
            for (int i = 0; i < path.size(); i++) {
                Flight flight = path.get(i);

                // 计算飞行时间
                long flightDuration = ChronoUnit.MINUTES.between(flight.getStartTime(), flight.getArrivalTime());
                totalCost += flightDuration;

                // 计算中转等待时间（如果有下一段航班）
                if (i < path.size() - 1) {
                    Flight nextFlight = path.get(i + 1);
                    long layoverDuration = ChronoUnit.MINUTES.between(flight.getArrivalTime(), nextFlight.getStartTime());
                    totalCost += layoverDuration;
                }
            }
        } else {
            // 如果按价格计算，则仅累加每段航班的价格
            totalCost = path.stream()
                    .mapToDouble(Flight::getPrice)
                    .sum();
        }

        return totalCost;
    }
}


class FlightProcessor {
    public Flight[] processFlights(Flight[] flights, String startCity, String destinationCity, LocalDateTime startDate) {
        // 创建航班图
        FlightGraph flightGraph = new FlightGraph();

        // 添加符合日期条件的航班信息到图中
        for (Flight flight : flights) {
            // 只判断 startTime 的日期部分是否等于 startDate 的日期部分
            if (flight.getStartTime().toLocalDate().isEqual(startDate.toLocalDate())) {
                flightGraph.addFlight(flight);
            }
        }

        // 创建航班搜索对象
        FlightSearch flightSearch = new FlightSearch(flightGraph);

        // 查找最快和最便宜路径
        FlightPathResult result = flightSearch.findPaths(startCity, destinationCity);

        // 获取所有可能的路径
        List<List<Flight>> allPaths = flightGraph.findAllPaths(startCity, destinationCity);

        // 构建 FlightInfo[] flights 数组
        List<Flight> flightsList = new ArrayList<>();

        // 添加"最快路径"标签
        flightsList.add(new Flight(-1, null, null, "", "", "", 0.0f, 0, 0, "","0", "最快路径："));
        flightsList.addAll(result.getFastestPath());

        // 添加"最便宜路径"标签
        flightsList.add(new Flight(-2, null, null, "", "", "", 0.0f, 0, 0, "", "0", "最便宜路径："));
        flightsList.addAll(result.getCheapestPath());

        // 添加剩余路径（排除最快和最便宜的路径）
        flightsList.add(new Flight(-3, null, null, "", "", "", 0.0f, 0, 0, "", "0", "剩下的路径："));
        for (List<Flight> path : allPaths) {
            if (!path.equals(result.getFastestPath()) && !path.equals(result.getCheapestPath())) {
                flightsList.addAll(path);
            }
        }

        // 返回构建好的 FlightInfo[] 数组
        return flightsList.toArray(new Flight[0]);
    }
}


class filter {
    public static void main(String[] args) {
        // 创建 DateTimeFormatter 用于解析时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

        // 假设从 DateChooser 获取的日期字符串（例如用户选择的日期）
        //String dateStr = dateChooser.getText();  // 例如 "2024-11-30"
        String startime = "2024-11-30";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 将日期字符串转换为 LocalDate
        LocalDate localDate = LocalDate.parse(startime, dateFormatter);
        // 将 LocalDate 转换为 LocalDateTime 的当天开始时间
        LocalDateTime startDate = LocalDateTime.of(localDate, LocalTime.MIN);  // 2024-11-30T00:00
        /*
        flight.Flight[] flights = new DbSelect().FlightSelect(startime,  "北京", "深圳");//返回12列
        if (flights == null) {
            System.err.println("No flights returned from database.");
            flights = new Flight[0]; // 初始化为空数组，避免 NullPointerException
        }*/
        // 构建 FlightInfo[] flights 数组

        Flight[] flights = {
                new Flight(1, "2024-11-30-06-00-00", "2024-11-30-08-00-00", "北京", "上海", "2024-11-30", 500.0f, 100, 150, "On Time", "0", "Flight A"),
                new Flight(2, "2024-11-30-09-00-00", "2024-11-30-11-30-00",  "上海", "广州", "2024-11-30", 400.0f, 100, 150, "On Time","0", "Flight C"),
                new Flight(3, "2024-11-30-12-00-00", "2024-11-30-14-00-00",  "广州", "深圳", "2024-11-30", 300.0f, 100, 150, "On Time", "0", "Flight D"),
                new Flight(4, "2024-11-30-21-30-00", "2024-12-01-00-30-00",  "北京", "杭州", "2024-11-30", 450.0f, 100, 150, "On Time", "0", "Flight B"),
                new Flight(5, "2024-12-01-09-30-00", "2024-12-01-12-00-00",  "杭州", "深圳", "2024-12-01", 350.0f, 100, 150, "On Time", "0", "Flight E"),
                new Flight(6, "2024-11-30-09-30-00", "2024-11-30-11-30-00",  "北京", "深圳", "2024-11-30", 1000.0f, 100, 150, "On Time", "0", "Flight F"),
                new Flight(7, "2024-12-30-09-30-00", "2024-12-30-11-30-00", "北京", "深圳", "2024-12-30", 1000.0f, 100, 150, "On Time","0", "Flight G")
        };
        for (Flight flight : flights) {
            System.out.println("ID: " + flight.getId());
            System.out.println("Start Time: " + flight.getStartTime());
            System.out.println("Arrival Time: " + flight.getArrivalTime());
            System.out.println("Start City: " + flight.getStartCity());
            System.out.println("Arrival City: " + flight.getArrivalCity());
            System.out.println("Departure Date: " + flight.getDepartureDate());
            System.out.println("Price: " + flight.getPrice());
            System.out.println("Current Passengers: " + flight.getCurrentPassengers());
            System.out.println("Seat Capacity: " + flight.getSeatCapacity());
            System.out.println("Flight Status: " + flight.getFlightStatus());
            System.out.println("Passenger ID: " + flight.getPassengerId());
            System.out.println("Flight Name: " + flight.getFlightName());
            System.out.println("-------------------------------------------------");
        }


        // 创建 FlightProcessor 对象
        FlightProcessor processor = new FlightProcessor();
        System.out.println("FlightProcessor对象创建成功 " );
        // 处理航班数据，查找从 "北京" 到 "深圳" 的路径
        Flight[] processedFlights = processor.processFlights(flights, "北京", "深圳",startDate);
        System.out.println("processor.processFlights查找成功 " );
        // 输出结果
        for (Flight flight : processedFlights) {
            if (flight.getStartTime() == null && flight.getArrivalTime() == null) {
                // 输出标签
                System.out.println(flight.getFlightName());
            } else {
                // 输出具体航班信息
                System.out.println(flight.toString());
            }
        }
    }
}