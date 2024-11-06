//创建和获取订单的详细信息

package flight;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
	private int id = 0;
	private Passenger PassengerId = null;
	private int Seat = 0;
	private Flight FlightId = null;
	private LocalDateTime CreateDate;
	private String Status = "";


	public static boolean IsHasOrder(int pid,int fid)
	{
		boolean re=false;
		Order x=new DbSelect().OrderSelect(pid, fid,"yes");
		if(x==null)
		{
			re=true;
		}
		return re;
	}
	public Order(int id, int PassengerId, int Seat, int FlightId, String CreateDate, String Status) {
		DbSelect dbSelect = new DbSelect();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		this.id = id;
		this.PassengerId = dbSelect.PassengerSelect(PassengerId);
		this.Seat = Seat;
		this.FlightId = dbSelect.FlightSelect(FlightId);
		this.CreateDate = LocalDateTime.parse(CreateDate, formatter);
		this.Status = Status;
	}


	public int getId() {
		return id;
	}

	public Passenger getPassengerId() {
		return PassengerId;
	}

	public int getSeat() {
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

}
