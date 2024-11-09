package flight;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

enum Flight_Status {
	UNPUBLISHED("UNPUBLISHED"), AVAILABLE("AVAILABLE"), FULL("FULL"), TERMINATE(
			"TERMINATE");
	private String status;

	Flight_Status(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

};

public class Flight {
	private int id = 0;
	private LocalDateTime StartTime = null;
	private LocalDateTime ArrivalTime = null;
	private String StartCity = "";
	private String ArrivalCity = "";
	private String DepartureDate = "";
	private float price = 0f;
	private int CurrentPassengers = 0;
	private int SeatCapacity = 0;
	private String FlightStatus = "";
	private int[] PassengerId;
	private String FlightName = "";

	public Flight(int id, String StartTime, String ArrivalTime,
			String StartCity, String ArrivalCity, String DepartureDate,
			float price, int CurrentPassengers, int SeatCapacity,
			String FlightStatus, String PassengerId, String FlightName) {
		// 根据时间字符串的格式创建 DateTimeFormatter
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		/*
		this.id = id;
		this.StartTime = LocalDateTime.parse(StartTime, formatter);
		this.ArrivalTime = LocalDateTime.parse(ArrivalTime, formatter);
		this.StartCity = StartCity;
		this.ArrivalCity = ArrivalCity;
		this.DepartureDate = DepartureDate;
		this.price = price;
		this.CurrentPassengers = CurrentPassengers;
		this.SeatCapacity = SeatCapacity;
		this.FlightStatus = FlightStatus;
		this.PassengerId = this.GetPassengerList(PassengerId);
		this.FlightName = FlightName;
		*/
		this.id = id;
		this.StartTime = (StartTime != null) ? LocalDateTime.parse(StartTime, formatter) : null;
		this.ArrivalTime = (ArrivalTime != null) ? LocalDateTime.parse(ArrivalTime, formatter) : null;

		// Debug输出
		//System.out.println("Initializing Flight ID: " + id + ", StartTime: " + this.StartTime + ", ArrivalTime: " + this.ArrivalTime);

		this.StartCity = (StartCity != null) ? StartCity : "";
		this.ArrivalCity = (ArrivalCity != null) ? ArrivalCity : "";
		this.DepartureDate = (DepartureDate != null) ? DepartureDate : "";
		this.price = price;
		this.CurrentPassengers = CurrentPassengers;
		this.SeatCapacity = SeatCapacity;
		this.FlightStatus = (FlightStatus != null) ? FlightStatus : "";
		this.PassengerId = GetPassengerList(PassengerId != null ? PassengerId : "");
		this.FlightName = (FlightName != null) ? FlightName : "";
	}

	//根据航班状态返回不同的整数值
	public static int IsUpdateFlight(int id, String StartTime,
		String ArrivalTime, String StartCity, String ArrivalCity,
		String DepartureDate, float price, int CurrentPassengers,
		int SeatCapacity, String FlightStatus, String PassengerId,
		String FlightName) {
		if (FlightStatus.equals(Flight_Status.TERMINATE.getStatus())) {
			return 1;
		}
		if (FlightStatus.equals(Flight_Status.UNPUBLISHED.getStatus())) {
			return 2;
		}
		if (FlightStatus.equals(Flight_Status.FULL.getStatus())
				|| FlightStatus.equals(Flight_Status.AVAILABLE.getStatus())) {
			return 3;
		}
		return 0;
	}

	//验证航班信息的有效性
	public static boolean IsFlight(String StartTime, String ArrivalTime,
		String StartCity, String ArrivalCity, String DepartureDate,
		float price, int CurrentPassengers, int SeatCapacity,
		String FlightStatus, String PassengerId, String FlightName) {
		// 定义日期时间格式
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		// 使用 LocalDateTime.parse() 方法将字符串解析为 LocalDateTime 对象
		LocalDateTime startTime = LocalDateTime.parse(StartTime, formatter);
		LocalDateTime arrivalTime = LocalDateTime.parse(ArrivalTime, formatter);
		if (!startTime.isBefore(arrivalTime)) {
			return false;
		}
		if (StartCity.equals(ArrivalCity)) {
			return false;
		}//出发城市和到达城市不能相同
		return true;
	}

	public static BookingInfo[] SelectFlightInfo(int id) {
		DbSelect sel = new DbSelect();
		Flight f = sel.FlightSelect(id);
		int[] pids = f.getPassengerId();
		BookingInfo[] re = new BookingInfo[pids.length];
		for (int i = 0; i < pids.length; i++) {
			Passenger p = sel.PassengerSelect(pids[i]);
			Order o = sel.OrderSelect(p.getId(), f.getId());
			BookingInfo b = new BookingInfo(f.getId(), p.getId(),
					p.getRealName(), p.getIdentityId(), o.getId(), o.getSeat(),
					o.getCreateDate(), o.getStatus());
			re[i] = b;
		}
		//
		return re;
	}

	//预定航班
	public static boolean ReserveFlight(int pid, int fid) {
		DbUpdate update = new DbUpdate();
		DbSelect select = new DbSelect();
		Flight xFlight = select.FlightSelect(fid);
		String flightStatus = xFlight.getCurrentPassengers() + 1 == xFlight
				.getSeatCapacity() ? Flight_Status.FULL.getStatus() : xFlight
				.getFlightStatus();//是否满员
		int[] pids = xFlight.getPassengerId();
		String passengerIdString = "";
		if (pids != null) {
			for (int i = 0; i < pids.length; i++) {
				passengerIdString += pids[i] + ";";
			}
		}//更新乘客的 ID 列表
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		passengerIdString += pid + ";";
		boolean r = update.FlightUpdate(xFlight.getId(),
				xFlight.getStartTime().format(formatter),
				xFlight.getArrivalTime().format(formatter),
				xFlight.getStartCity(), xFlight.getArrivalCity(),
				xFlight.getDepartureDate(), xFlight.getPrice(),
				(xFlight.getCurrentPassengers() + 1),
				xFlight.getSeatCapacity(), flightStatus, passengerIdString,
				xFlight.getFlightName());
		if (r) {
			return true;
		}
		return false;

	}

	//取消预定
	public boolean UnreserveFlight(int pid, int fid) {
		DbUpdate update = new DbUpdate();
		DbSelect select = new DbSelect();
		Flight xFlight = select.FlightSelect(fid);
		String flightStatus = xFlight.getCurrentPassengers() - 1 < xFlight
				.getSeatCapacity() ? Flight_Status.AVAILABLE.getStatus()
				: xFlight.getFlightStatus();
		int[] pids = xFlight.getPassengerId();
		String passengerIdString = "";
		if (pids != null) {
			for (int i = 0; i < pids.length; i++) {
				if (pids[i] != pid) {
					passengerIdString += pids[i] + ";";
				}
			}
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		boolean r = update.FlightUpdate(xFlight.getId(),
				xFlight.getStartTime().format(formatter),
				xFlight.getArrivalTime().format(formatter),
				xFlight.getStartCity(), xFlight.getArrivalCity(),
				xFlight.getDepartureDate(), xFlight.getPrice(),
				(xFlight.getCurrentPassengers() - 1),
				xFlight.getSeatCapacity(), flightStatus, passengerIdString,
				xFlight.getFlightName());
		if (r) {
			return true;
		}
		return false;

	}

	//自动更新航班状态
	public static void AutoUpdateStatus(LocalDateTime NowDate) {
		DbSelect sel = new DbSelect();
		Flight[] flights = sel.FlightSelect();
		DbUpdate up = new DbUpdate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		for (int i = 0; i < flights.length; i++) {
			if (flights[i].getFlightStatus().equals(Flight_Status.AVAILABLE.getStatus())
					|| flights[i].getFlightStatus().equals(Flight_Status.FULL.getStatus())) {
				// 计算当前时间与航班出发时间的分钟差
				LocalDateTime startTime = flights[i].getStartTime();
				long minutesDifference = Duration.between(NowDate, startTime).toMinutes();
				// 如果出发时间在当前时间之后且不超过120分钟，则更新航班状态
				if (minutesDifference >= 0 && minutesDifference <= 120){
					boolean re = up.FlightUpdate(
									flights[i].id,
									flights[i].getStartTime().format(formatter),
									flights[i].getArrivalTime().format(formatter),
									flights[i].StartCity,
									flights[i].ArrivalCity,
									flights[i].DepartureDate,
									flights[i].price,
									flights[i].CurrentPassengers,
									flights[i].SeatCapacity,
									Flight_Status.TERMINATE.getStatus(),
									flights[i]
											.GetPassengerString(flights[i].PassengerId),
									flights[i].FlightName);
					if (!re) {
						System.err.println("航班状态更新失败");
					}
				}
			}
		}
	}

	public int getId() {
		return id;
	}

	public LocalDateTime getStartTime() {
		return StartTime;
	}

	public LocalDateTime getArrivalTime() {
		return ArrivalTime;
	}

	public String getStartCity() {
		return StartCity;
	}

	public String getArrivalCity() {
		return ArrivalCity;
	}

	public String getDepartureDate() {
		return DepartureDate;
	}

	public float getPrice() {
		return price;
	}

	public int getCurrentPassengers() {
		return CurrentPassengers;
	}

	public int getSeatCapacity() {
		return SeatCapacity;
	}

	public String getFlightStatus() {
		return FlightStatus;
	}

	public int[] getPassengerId() {
		return PassengerId;
	}

	public String getFlightName() {
		return FlightName;
	}

	public int[] GetPassengerList(String _o) {
		if (_o.length() > 0) {
			String[] _s;
			_s = _o.split(";");
			int[] _t = new int[_s.length];
			for (int i = 0; i < _s.length; i++) {
				_t[i] = Integer.parseInt(_s[i]);
			}
			return _t;
		} else {
			return null;
		}
	}

	public String GetPassengerString(int[] i) {
		String s = "";
		if (i != null) {
			for (int j = 0; j < i.length; j++) {
				s += Integer.toString(i[j]) + ";";
			}
		}
		return s;
	}

	/*
	public static DateTime GetTime(String time) {
		DateTime x = new DateTime(time);
		return x;
	}*/
	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

		String formattedStartTime = (StartTime != null) ? StartTime.format(formatter) : "N/A";
		String formattedArrivalTime = (ArrivalTime != null) ? ArrivalTime.format(formatter) : "N/A";

		return "Flight ID: " + id +
				", Start Time: " + formattedStartTime +
				", Arrival Time: " + formattedArrivalTime +
				", Start City: " + StartCity +
				", Arrival City: " + ArrivalCity +
				", Departure Date: " + DepartureDate +
				", Price: " + price +
				", Current Passengers: " + CurrentPassengers +
				", Seat Capacity: " + SeatCapacity +
				", Flight Status: " + FlightStatus +
				", Passenger ID: " + Arrays.toString(PassengerId) +
				", Flight Name: " + FlightName;
	}




	public static void main(String[] args) {
		Flight[] flights = {
				new Flight(1, "2024-11-30-06-00-00", "2024-11-30-08-00-00", "北京", "上海", "2024-11-30", 500.0f, 100, 150, "AVAILABLE", "0", "Flight A"),
				new Flight(2, "2024-11-30-09-00-00", "2024-11-30-11-30-00",  "上海", "广州", "2024-11-30", 400.0f, 100, 150, "AVAILABLE","0", "Flight C"),
				new Flight(3, "2024-11-30-12-00-00", "2024-11-30-14-00-00",  "广州", "深圳", "2024-11-30", 300.0f, 100, 150, "AVAILABLE", "0", "Flight D"),
				new Flight(4, "2024-11-30-21-30-00", "2024-12-01-00-30-00",  "北京", "杭州", "2024-11-30", 450.0f, 100, 150, "AVAILABLE", "0", "Flight B"),
				new Flight(5, "2024-12-01-09-30-00", "2024-12-01-12-00-00",  "杭州", "深圳", "2024-12-01", 350.0f, 100, 150, "AVAILABLE", "0", "Flight E"),
				new Flight(6, "2024-11-30-09-30-00", "2024-11-30-11-30-00",  "北京", "深圳", "2024-11-30", 1000.0f, 100, 150, "AVAILABLE", "0", "Flight F"),
				new Flight(7, "2024-12-30-09-30-00", "2024-12-30-11-30-00", "北京", "深圳", "2024-12-30", 1000.0f, 100, 150, "AVAILABLE","0", "Flight G")
		};

		for (Flight flight : flights) {
			System.out.println(flight);
		}

	}

}
