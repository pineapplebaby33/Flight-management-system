package flight;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

class FlightGenerator {
    private int currentId;
    private Calendar departureDate;
    private int defaultCount;
    private boolean isDomestic; // 新增参数
    private Random random = new Random();

    // 定义国内出发地和目的地选项
    private final String[] domesticLocations = {
            "北京", "上海", "天津", "重庆", "哈尔滨", "长春", "沈阳",
            "呼和浩特", "石家庄", "乌鲁木齐", "兰州", "西宁", "西安",
            "银川", "郑州", "济南", "太原", "合肥", "长沙", "武汉",
            "南京", "成都", "贵阳", "昆明", "南宁", "拉萨", "杭州",
            "南昌", "广州", "福州", "台北", "海口", "香港", "澳门", "深圳"
    };

    private final String[] domesticCodes = {
            "BJ", "SH", "TJ", "CQ", "HR", "CC", "SY",
            "HH", "SJ", "WLMQ", "LZ", "XN", "XA",
            "YC", "ZZ", "JN", "TY", "HF", "CS", "WH",
            "NJ", "CD", "GY", "KM", "NN", "LS", "HZ",
            "NC", "GZ", "FZ", "TP", "HK", "HK", "MO", "SZ"
    };

    // 定义国外出发地和目的地选项
    private final String[] internationalLocations = {
            "纽约", "伦敦", "巴黎", "柏林", "阿姆斯特丹", "慕尼黑",
            "罗马", "东京", "首尔", "曼谷", "悉尼", "奥克兰",
            "温哥华", "莫斯科", "芝加哥", "洛杉矶", "新加坡", "旧金山"
    };

    private final String[] internationalCodes = {
            "NY", "LD", "PA", "BL", "AM", "MU",
            "RM", "TK", "SL", "BK", "SY", "AK",
            "VC", "MC", "CG", "LA", "SG", "SF"
    };

    public FlightGenerator(int startId, String departureDate, int count, boolean isDomestic) throws Exception {
        this.currentId = startId;
        this.departureDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        this.departureDate.setTime(sdf.parse(departureDate));
        this.defaultCount = count;
        this.isDomestic = isDomestic;
    }


    // 生成单条航班数据
    private String generateFlight() {
        // 根据 isDomestic 参数选择对应的城市和代码数组
        String[] domesticLocations = this.domesticLocations;
        String[] domesticCodes = this.domesticCodes;
        String[] internationalLocations = this.internationalLocations;
        String[] internationalCodes = this.internationalCodes;

        String origin, destination, originCode, destinationCode;

        if (isDomestic) {
            // 国内航班：始发地和目的地都在国内
            int originIndex = random.nextInt(domesticLocations.length);
            int destinationIndex = random.nextInt(domesticLocations.length);
            while (destinationIndex == originIndex) {
                destinationIndex = random.nextInt(domesticLocations.length);
            }

            origin = domesticLocations[originIndex];
            destination = domesticLocations[destinationIndex];
            originCode = domesticCodes[originIndex];
            destinationCode = domesticCodes[destinationIndex];

        } else {
            // 国际航班：在国内和国外城市之间随机选择
            boolean originIsDomestic = random.nextBoolean();

            if (originIsDomestic) {
                // 始发地为国内城市，目的地为国际城市
                int originIndex = random.nextInt(domesticLocations.length);
                int destinationIndex = random.nextInt(internationalLocations.length);

                origin = domesticLocations[originIndex];
                destination = internationalLocations[destinationIndex];
                originCode = domesticCodes[originIndex];
                destinationCode = internationalCodes[destinationIndex];

            } else {
                // 始发地为国际城市，目的地为国内城市
                int originIndex = random.nextInt(internationalLocations.length);
                int destinationIndex = random.nextInt(domesticLocations.length);

                origin = internationalLocations[originIndex];
                destination = domesticLocations[destinationIndex];
                originCode = internationalCodes[originIndex];
                destinationCode = domesticCodes[destinationIndex];
            }
        }

        // 生成出发和到达时间
        Calendar departureTime = (Calendar) departureDate.clone();
        departureTime.add(Calendar.HOUR_OF_DAY, random.nextInt(15) + 7);
        departureTime.add(Calendar.MINUTE, random.nextBoolean() ? 0 : 30);

        int duration = random.nextInt(61) + 90; // 航班时长（分钟）
        Calendar arrivalTime = (Calendar) departureTime.clone();
        arrivalTime.add(Calendar.MINUTE, duration);

        // 生成价格、折扣、座位数和状态
        int price = random.nextInt(251) + 450;
        int seats = random.nextInt(51) + 100;

        // 90% 的几率为 AVAILABLE，10% 为 FULL
        String status = random.nextInt(10) < 9 ? "AVAILABLE" : "FULL";

        // 根据状态决定折扣
        int discount = status.equals("FULL") ? seats : random.nextInt(46) + 55;

        // 生成航班编号
        String flightCode = originCode + destinationCode + (random.nextInt(9000) + 1000);

        // 格式化航班数据
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String flightData = String.format("%d,%s,%s,%s,%s,%s,%d,%d,%d,%s,%s,%s",
                currentId,
                dateTimeFormat.format(departureTime.getTime()),
                dateTimeFormat.format(arrivalTime.getTime()),
                origin,
                destination,
                dateTimeFormat.format(departureTime.getTime()),
                price,
                discount,
                seats,
                status,
                " ",
                flightCode
        );

        // 更新ID
        currentId++;
        return flightData;
    }


    // 生成多个航班数据
    public List<String> generateFlights() {
        return generateFlights(this.defaultCount);
    }

    public List<String> generateFlights(int count) {
        List<String> flights = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            flights.add(generateFlight());
        }
        return flights;
    }

    public static void main(String[] args) throws Exception {
        /*
        // 示例：生成默认数量的国内航班（15 条）
        FlightGenerator generatorDomestic = new FlightGenerator(128, "2024-11-20", 15, true);
        List<String> flightsDomestic = generatorDomestic.generateFlights();
        System.out.println("国内航班：");
        for (String flight : flightsDomestic) {
            System.out.println(flight);
        }

         */

        // 示例：生成指定数量的国际航班（10 条）
        FlightGenerator generatorInternational = new FlightGenerator(101, "2024-11-19", 15, false);
        List<String> flightsInternational = generatorInternational.generateFlights();
        System.out.println("国际航班：");
        for (String flight : flightsInternational) {
            System.out.println(flight);
        }
    }
}

public class Generate{
    private DbSelect sel = null;

    public void judgment(){
        this.sel = new DbSelect();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysLater = now.plusDays(7);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedNow = sevenDaysLater.format(df);
        System.out.println(formattedNow);
        boolean HasData = sel.hasFlightOnDate(formattedNow,true);
        if(!HasData){
            System.out.println("正在生成7天后的国内航班");
            AUTOgenerate(true);
        }
        else System.out.println("已有7天后的国内航班");
        boolean HasData1 = sel.hasFlightOnDate(formattedNow,false);
        if(!HasData1){
            System.out.println("正在生成7天后的国外航班");
            AUTOgenerate(false);
        }
        else System.out.println("已有7天后的国外航班");

    }



    public void AUTOgenerate(boolean isDomestic) {
        this.sel = new DbSelect();
        int newid = sel.NewGetId(isDomestic) + 1; //
        // 使用获取到的newid
        try {
            FlightGenerator generatorDomestic = new FlightGenerator(newid, "2024-11-20", 15, isDomestic);
            List<String> flightsDomestic = generatorDomestic.generateFlights();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

            for (String flight : flightsDomestic) {
                System.out.println(flight);
                String[] flightData = flight.split(",");

                if (flightData.length >= 12) {
                    try {
                        int FlightId = Integer.parseInt(flightData[0]); // 航班ID
                        String st = flightData[1]; // 出发时间
                        String at = flightData[2]; // 到达时间
                        String sc = flightData[3]; // 出发城市
                        String ac = flightData[4]; // 到达城市
                        float price = Float.parseFloat(flightData[6]); // 票价
                        int currentPassengers = Integer.parseInt(flightData[7]); // 当前乘客数
                        int ca = Integer.parseInt(flightData[8]); // 座位容量
                        String FS = flightData[9]; // 航班状态
                        String name = flightData[11]; // 航班名称

                        // 解析出发和到达时间
                        LocalDateTime parsedDepartureTime = LocalDateTime.parse(st, formatter);
                        LocalDateTime parsedArrivalTime = LocalDateTime.parse(at, formatter);

                        System.out.println("成功解析的出发时间：" + parsedDepartureTime);
                        System.out.println("成功解析的到达时间：" + parsedArrivalTime);

                        // 调用 DbUpdate 方法导入数据库
                        boolean x = new DbInsert().FlightInsert(st, at,
                                sc, ac, st, price, currentPassengers, ca,
                                FS, "", name, isDomestic);

                        if (x) {
                            System.out.println("航班 " + name + " 成功导入数据库");
                        } else {
                            System.out.println("航班 " + name + " 导入失败");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("解析数字时出错，跳过此航班：" + flight);
                    } catch (DateTimeParseException e) {
                        System.out.println("解析日期格式时出错，跳过此航班：" + flight);
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("航班数据格式错误: " + flight);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        Generate gen = new Generate();
        gen.judgment();
    }
}
