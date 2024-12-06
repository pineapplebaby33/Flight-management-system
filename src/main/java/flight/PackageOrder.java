package flight;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import frame.FlightRecommendation;
import frame.Login;
import frame.Research;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PackageOrder {//对标package表
    private int id = 0;
    private int PassengerId = 0;
    private float Price;
    private int OId = 0;


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
    //p-订单构造函数
    public PackageOrder(int id, int PassengerId, String packagestatus, float Price, int OId) {
        this.id = id;
        this.PassengerId = PassengerId;
        Login.packagestatus = packagestatus;
        this.Price = Price;
        this.OId = OId;
    }

    public static int ReservePackageOrder(String pwd,int PassengerId, float Price, int OId,boolean first) {
        DbInsert dbInsert = new DbInsert();
        DbSelect dbSelect = new DbSelect();
        Passenger p = dbSelect.PassengerSelect(PassengerId);
        if (Passenger.CheckPwd(p.getRealName(), pwd)) {//验证密码
            float discountPrice = discountPrice(Price);//生成折扣价格
            boolean re=true;
            if(first){
                 re= dbInsert.PackageInsert(PassengerId, FlightRecommendation.selectstatue,0,0);//初始选择套餐
                System.out.println("初次订购套餐成功");
            }
            else{
                re= dbInsert.PackageInsert(PassengerId, Login.packagestatus,discountPrice,OId);//插入套餐订单
                System.out.println("订购套餐下订单成功");
            }

            if(re){
                System.out.println("在PackageOrder.ReservePackageOrder里dbInsert.PackageInsert成功");
                return 1;//订购套餐成功
            }else{
                System.out.println("在PackageOrder.ReservePackageOrder里dbInsert.PackageInsert失败");
                return 0;//订购套餐失败
            }
        }else{
            System.out.println("定套餐密码错误");
            return 2;//密码错误
        }
    }

    public static float discountPrice(float currentPrice) {//计算折扣价格
        float discountPrice = currentPrice;//先默认折扣价格为当前价格
        if(Login.packagestatus.equals("学生寒暑假")) {
            discountPrice = 300;
        }else if(Login.packagestatus.equals("国内随心飞")){
            Random random = new Random();
            float discountFactor = (float) (0.75);
            // 计算折后价格
            discountPrice = currentPrice * discountFactor;
            //System.out.printf("原价: %.2f\n折扣: %.2f\n折后价: %.2f\n", currentPrice, discountFactor, discountPrice);
        }else if(Login.packagestatus.equals("国外随心飞")){
            discountPrice = 1000;
        }
        return discountPrice;
    }

    public String getPackage() {
        return Login.packagestatus;
    }

    public int getId() {
        return id;
    }

    public int getPassengerId() {
        return PassengerId;
    }
    public float getPrice() {
        return Price;
    }
    public int getOId() {
        return OId;
    }


}
