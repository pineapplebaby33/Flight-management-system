//创建和获取订单的详细信息

package flight;

public class Order {
	private int id = 0;
	private Passenger PassengerId = null;
	private int Seat = 0;
	private Flight FlightId = null;
	private DateTime CreateDate;
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
	public Order(int id, int PassengerId, int Seat, int FlightId,
			String CreateDate, String Status) {
		DbSelect dbSelect = new DbSelect();

		this.id = id;
		this.PassengerId = dbSelect.PassengerSelect(PassengerId);
		this.Seat = Seat;
		this.FlightId = dbSelect.FlightSelect(FlightId);
		DateTime d = new DateTime(CreateDate);
		this.CreateDate = d;
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

	public DateTime getCreateDate() {
		return CreateDate;
	}

	public String getStatus() {
		return Status;
	}

}
