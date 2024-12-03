package flight;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import frame.Research;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PackageOrder {//对标package表
    private int id = 0;
    private int PassengerId = 0;
    private String Package = "";
    private int Price;
    private int OId = 0;

/*
    //p-查询订单√
    public static boolean IsHasPackageOrder(int pid) {
        boolean re=false;
        Order x=new DbSelect().OrderSelect(pid, fid,"yes",isDomestic);
        if(x==null)
        {
            re=true;
        }
        return re;
    }

    //p-订单构造函数
    public Order(int id, int PassengerId, String Seat, int FlightId, String CreateDate, String Status, boolean isDomestic) {
        DbSelect dbSelect = new DbSelect();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        this.id = id;
        this.PassengerId = dbSelect.PassengerSelect(PassengerId);
        this.Seat = Seat;
        this.FlightId = dbSelect.FlightSelect(FlightId,isDomestic);
        this.CreateDate = LocalDateTime.parse(CreateDate, formatter);
        this.Status = Status;
    }


    public int getId() {
        return id;
    }

    public Passenger getPassengerId() {
        return PassengerId;
    }

    public String getSeat() {
        return Seat;
    }

    public Flight getFlightId() {
        return FlightId;
    }

    public LocalDateTime getCreateDate() {
        return CreateDate;
    }

    public String getStatus() {
        return Status;
    }

 */

}
