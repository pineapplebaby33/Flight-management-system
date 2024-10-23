package flight;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DbInsert {
	private DbConnect db = null;
	private Connection cn = null;
	private boolean re = false;
	private PreparedStatement pst = null;

	/*
	 * ������Insert
	 */
	public boolean PassengerInsert(Passenger p) {
		return new DbInsert().PassengerInsert(p.getRealName(),
				p.getIdentityId(), p.getPassword(),
				p.GetOrderString(p.getOrderList()));
	}

	public boolean AdminInsert(Admin admin) {
		return new DbInsert().AdminInsert(admin.getUsername(), admin.getPwd());
	}

	public boolean FlightInsert(Flight f) {
		return new DbInsert().FlightInsert(
				DateTime.GetDateTimeStr(f.getStartTime()),
				DateTime.GetDateTimeStr(f.getArrivalTime()), f.getStartCity(),
				f.getArrivalCity(), f.getDepartureDate(), f.getPrice(),
				f.getCurrentPassengers(), f.getSeatCapacity(),
				f.getFlightStatus(), f.GetPassengerString(f.getPassengerId()),
				f.getFlightName());
	}

	public boolean OrderInsert(Order o) {
		return new DbInsert().OrderInsert(o.getPassengerId().getId(),
				o.getSeat(), o.getFlightId().getId(),
				DateTime.GetDateTimeStr(o.getCreateDate()), o.getStatus());
	}

	/*
	 * ����Insert�����ǡ�����Insert�����е������ֶ�ȫ��ֻ���� �ַ�������Ϊ���ǵ���Щ�����ֶ���Insert��ʱ��Ӧ�ö��ǿհ׵�
	 */
	public boolean PassengerInsert(String RealName, String IdentityId,
			String Password, String OrderList) {
		// ���������md5����Ȼ���ڴ洢
		Password = Encode.MD5(Password);
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn
					.prepareStatement("INSERT INTO `passenger`(`Id`, `RealName`, `IdentityId`, `Password`, `OrderList`) VALUES (NULL,'"
							+ RealName
							+ "','"
							+ IdentityId
							+ "','"
							+ Password
							+ "','" + OrderList + "');");
			this.re = pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;

	}

	public boolean AdminInsert(String username, String pwd) {
		// ���������md5����Ȼ���ڴ洢
		pwd = Encode.MD5(pwd);
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn
					.prepareStatement("INSERT INTO `admin`(`Id`, `Username`, `Password`) VALUES (NULL,'"
							+ username + "','" + pwd + "');");
			this.re = pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;
	}

	public boolean OrderInsert(int PassengerId, int Seat, int FlightId,
			String CreateDate, String Status) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn
					.prepareStatement("INSERT INTO `order`(`Id`, `PassengerId`, `Seat`, `FlightId`, `CreateDate`, `Status`) VALUES (NULL,"
							+ PassengerId
							+ ","
							+ Seat
							+ ","
							+ FlightId
							+ ",'"
							+ CreateDate + "','" + Status + "');");
			this.re = pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;
	}

	public boolean FlightInsert(String StartTime, String ArrivalTime,
			String StartCity, String ArrivalCity, String DepartureDate,
			float price, int CurrentPassengers, int SeatCapacity,
			String FlightStatus, String PassengerId, String FlightName) {
		// ���ݹ淶���
		if (!Flight.IsFlight(StartTime, ArrivalTime, StartCity, ArrivalCity,
				DepartureDate, price, CurrentPassengers, SeatCapacity,
				FlightStatus, PassengerId, FlightName)) {
			System.err.println("Flight���ݲ��淶���޷����");
			return false;
		}
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn
					.prepareStatement("INSERT INTO `flight`(`Id`, `StartTime`, `ArrivalTime`, `StartCity`, `ArrivalCity`, `DepartureDate`, `Price`, `CurrentPassengers`, `SeatCapacity`, `FlightStatus`, `PassengerId`, `FlightName`) VALUES (NULL,'"
							+ StartTime
							+ "','"
							+ ArrivalTime
							+ "','"
							+ StartCity
							+ "','"
							+ ArrivalCity
							+ "','"
							+ DepartureDate
							+ "',"
							+ price
							+ ","
							+ CurrentPassengers
							+ ","
							+ SeatCapacity
							+ ",'"
							+ FlightStatus
							+ "','"
							+ PassengerId
							+ "','"
							+ FlightName + "');");
			this.re = pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;

	}

	public static void main(String[] args) {
		/*
		 * Example �Д�Flight���딵���Ƿ�Ҏ���K�x�����
		DbInsert d = new DbInsert();
		DbSelect s = new DbSelect();
		Flight xFlight = s.FlightSelect(1);
		boolean rex = false;
		System.out.println(DateTime.GetDateTimeStr(xFlight.getStartTime()));
		System.out.println(DateTime.GetDateTimeStr(xFlight.getArrivalTime()));
		DateTime _t1 = DateTime.GetDateTimeOb(DateTime.GetDateTimeStr(xFlight
				.getStartTime()));
		DateTime _t2 = DateTime.GetDateTimeOb(DateTime.GetDateTimeStr(xFlight
				.getArrivalTime()));
		System.out.println("���ʱ���Ƿ���ڵ���ʱ�䣺" + DateTime.CompareDate(_t1, _t2));
		System.out.println("��ʱ�����Ƿ����2Сʱ��" + DateTime.GetSub(_t1, _t2));
		rex = d.FlightInsert(DateTime.GetDateTimeStr(xFlight.getStartTime()),
				DateTime.GetDateTimeStr(xFlight.getArrivalTime()),
				xFlight.getStartCity(), "����", xFlight.getDepartureDate(),
				xFlight.getPrice(), xFlight.getCurrentPassengers(),
				xFlight.getSeatCapacity(), xFlight.getFlightStatus(),
				xFlight.GetPassengerString(xFlight.getPassengerId()), "XZ98521");
		if (rex) {
			System.out.println("��ӳɹ�");
		} else {
			System.out.println("���ʧ��");
		}*/
		/*
		 * Passenger/Admin/Flight/Order Insert Example DbInsert d = new
		 * DbInsert(); DbSelect s=new DbSelect(); Flight
		 * xFlight=s.FlightSelect(1); Passenger passenger=s.PassengerSelect(2);
		 * Order o=s.OrderSelect(1); boolean rex=false;
		 * rex=d.OrderInsert(o.getPassengerId().getId(), o.getSeat(),
		 * o.getFlightId().getId(), DateTime.GetDateTimeStr(o.getCreateDate()),
		 * o.getStatus());
		 * if(rex){System.out.println("��ӳɹ�");}else{System.out.println("���ʧ��");}
		 * rex
		 * =d.FlightInsert(DateTime.GetDateTimeStr(xFlight.getStartTime()),DateTime
		 * .GetDateTimeStr(xFlight.getArrivalTime()),xFlight.getStartCity(),
		 * "����", xFlight.getDepartureDate(),xFlight.getPrice(),
		 * xFlight.getCurrentPassengers(),xFlight.getSeatCapacity(),
		 * xFlight.getFlightStatus
		 * (),xFlight.GetPassengerString(xFlight.getPassengerId()),"XZ98521");
		 * if(rex){System.out.println("��ӳɹ�");}else{System.out.println("���ʧ��");}
		 * rex=d.AdminInsert("Admin888","admin888");
		 * if(rex){System.out.println("��ӳɹ�");}else{System.out.println("���ʧ��");}
		 * rex
		 * =d.PassengerInsert(passenger.getRealName(),passenger.getIdentityId(),
		 * "balabala", "");
		 * if(rex){System.out.println("��ӳɹ�");}else{System.out.println("���ʧ��");}
		 */
	}
}
