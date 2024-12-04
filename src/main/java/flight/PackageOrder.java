package flight;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import frame.Research;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class PackageOrder {//对标package表
    private int id = 0;
    private int PassengerId = 0;
    private String PackageStuta = "";
    private float Price;
    private int OId = 0;


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
    //p-订单构造函数
    public PackageOrder(int id, int PassengerId, String PackageStuta, float Price, int OId) {
        this.id = id;
        this.PassengerId = PassengerId;
        this.PackageStuta = PackageStuta;
        this.Price = Price;
        this.OId = OId;
    }

    public static int ReservePackageOrder(String pwd,int PassengerId, String PackageStuta, float Price, int OId) {
        DbInsert dbInsert = new DbInsert();
        DbSelect dbSelect = new DbSelect();
        Passenger p = dbSelect.PassengerSelect(PassengerId);
        if (Passenger.CheckPwd(p.getRealName(), pwd)) {//验证密码
            float discountPrice = discountPrice(Price,PackageStuta);//生成折扣价格
            boolean re = dbInsert.PackageInsert(PassengerId,PackageStuta,discountPrice,OId);//插入套餐订单
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

    public static float discountPrice(float currentPrice, String PackageStuta) {//计算折扣价格
        float discountPrice = currentPrice;//先默认折扣价格为当前价格
        if(PackageStuta.equals("学生寒暑假")) {
            discountPrice = 300;
        }else if(PackageStuta.equals("国内随心飞")){
            Random random = new Random();
            // 生成一个[0.5, 1.0]范围内的随机折扣系数
            float discountFactor = (float) (0.5 + (0.5 * random.nextDouble()));
            // 计算折后价格
            discountPrice = currentPrice * discountFactor;
            System.out.printf("原价: %.2f\n折扣: %.2f\n折后价: %.2f\n", currentPrice, discountFactor, discountPrice);
        }else if(PackageStuta.equals("国外随心飞")){
            discountPrice = 1000;
        }
        return discountPrice;
    }

    public String getPackage() {
        return PackageStuta;
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
