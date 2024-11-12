package flight;

import frame.AllDialog;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;

public class DbInsert {
	private JFrame frame;
	private DbConnect db = null;
	private Connection cn = null;
	private boolean re = false;
	private PreparedStatement pst = null;

	/*
	public boolean PassengerInsert(Passenger p) {
		return new DbInsert().PassengerInsert(p.getRealName(),
				p.getIdentityId(), p.getPassword(),
				p.GetOrderString(p.getOrderList()));
	}

	 */

	public boolean AdminInsert(Admin admin) {
		return new DbInsert().AdminInsert(admin.getUsername(), admin.getPwd());
	}

	public boolean FlightInsert(Flight f) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		return new DbInsert().FlightInsert(
				f.getStartTime().format(formatter),
				f.getArrivalTime().format(formatter),
				f.getStartCity(),
				f.getArrivalCity(), f.getDepartureDate(), f.getPrice(),
				f.getCurrentPassengers(), f.getSeatCapacity(),
				f.getFlightStatus(), f.GetPassengerString(f.getPassengerId()),
				f.getFlightName(),true);
	}

	public boolean OrderInsert(Order o,boolean isDomestic) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		return new DbInsert().OrderInsert(o.getPassengerId().getId(),
				o.getSeat(), o.getFlightId().getId(),
				o.getCreateDate().format(formatter),
				o.getStatus(),isDomestic);
	}

	//P-注册用户√
	public boolean PassengerInsert(String RealName, String IdentityId, String Password, String OrderList,String OrderList1) {
		Password = Encode.MD5(Password);
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn.prepareStatement(
					"INSERT INTO `passenger`(`Id`, `RealName`, `IdentityId`, `Password`, `OrderList`, `OrderList1`) VALUES (NULL, '"
							+ RealName
							+ "', '"
							+ IdentityId
							+ "', '"
							+ Password
							+ "', '"
							+ OrderList
							+ "', '"
							+ OrderList1
							+ "');"
			);

			this.re = pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;

	}

	//A-添加管理员√
	public boolean AdminInsert(String username, String pwd) {
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

	//P-插入订单√
	public boolean OrderInsert(int PassengerId, int Seat, int FlightId, String CreateDate, String Status,boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`order`" : "`order1`";
			this.pst = cn
					.prepareStatement("INSERT INTO "+tableName+"(`Id`, `PassengerId`, `Seat`, `FlightId`, `CreateDate`, `Status`) VALUES (NULL,"
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

	//A-添加航班√
	public boolean FlightInsert(String StartTime, String ArrivalTime,
			String StartCity, String ArrivalCity, String DepartureDate,
			float price, int CurrentPassengers, int SeatCapacity,
			String FlightStatus, String PassengerId, String FlightName,boolean isDomestic) {

		if (!Flight.IsFlight(StartTime, ArrivalTime, StartCity, ArrivalCity,
				DepartureDate, price, CurrentPassengers, SeatCapacity,
				FlightStatus, PassengerId, FlightName)) {//验证航班有效信息
			AllDialog.Dialog(frame, "航班信息无效(出发城市不得与到达城市相同，出发时间必须在到达时间之前)");
			System.err.println("航班信息无效(出发城市不得与到达城市相同，出发时间必须在到达时间之前)");
			return false;
		}
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`flight`" : "`flight1`";
			this.pst = cn
					.prepareStatement("INSERT INTO "+tableName+"(`Id`, `StartTime`, `ArrivalTime`, `StartCity`, `ArrivalCity`, `DepartureDate`, `Price`, `CurrentPassengers`, `SeatCapacity`, `FlightStatus`, `PassengerId`, `FlightName`) VALUES (NULL,'"
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
