package flight;

import frame.AllDialog;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class DbInsert {
	private JFrame frame;
	private DbConnect db = null;
	private Connection cn = null;
	private boolean re = false;
	private PreparedStatement pst = null;

	public boolean AdminInsert(Admin admin) {
		return new DbInsert().AdminInsert(admin.getUsername(), admin.getPwd());
	}

	/*
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

	 */

	/*
	public boolean OrderInsert(Order o,boolean isDomestic) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		return new DbInsert().OrderInsert(o.getPassengerId().getId(),
				o.getSeat(), o.getFlightId().getId(),
				o.getCreateDate().format(formatter),
				o.getStatus(),isDomestic);
	}

	 */

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
	public boolean OrderInsert(int PassengerId, String Seat, int FlightId, String CreateDate, String Status,boolean isDomestic) {
		System.out.println("插入订单");
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`order`" : "`order1`";
			this.pst = cn.prepareStatement(
					"INSERT INTO " + tableName + " (`Id`, `PassengerId`, `Seat`, `FlightId`, `CreateDate`, `Status`) VALUES (NULL, ?, ?, ?, ?, ?);"
			);

			// 使用 PreparedStatement 设置参数
			this.pst.setInt(1, PassengerId);         // PassengerId 是整数
			this.pst.setString(2, Seat);              // Seat 是字符串，如 "6D"
			this.pst.setInt(3, FlightId);             // FlightId 是整数
			this.pst.setString(4, CreateDate);        // CreateDate 是字符串，格式应符合数据库要求
			this.pst.setString(5, Status);            // Status 是字符串
			//this.pst.executeUpdate();

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

	//A-添加航班(自动生成)√
	public boolean FlightInsert(int ID,String StartTime, String ArrivalTime,
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
			this.pst = cn.prepareStatement("INSERT INTO " + tableName
					+ "(`Id`, `StartTime`, `ArrivalTime`, `StartCity`, `ArrivalCity`, `DepartureDate`, `Price`, `CurrentPassengers`, `SeatCapacity`, `FlightStatus`, `PassengerId`, `FlightName`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

// 设置参数
			this.pst.setInt(1, ID);
			this.pst.setString(2, StartTime); // 确保 StartTime 是正确的格式
			this.pst.setString(3, ArrivalTime); // 确保 ArrivalTime 是正确的格式
			this.pst.setString(4, StartCity);
			this.pst.setString(5, ArrivalCity);
			this.pst.setString(6, DepartureDate); // 确保 DepartureDate 是正确的格式
			this.pst.setDouble(7, price);
			this.pst.setInt(8, CurrentPassengers);
			this.pst.setInt(9, SeatCapacity);
			this.pst.setString(10, FlightStatus);
			this.pst.setString(11, PassengerId);
			this.pst.setString(12, FlightName);

// 执行插入
			//this.pst.executeUpdate();
			this.re = pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;

	}

	//
	public boolean insertTransitData(Map<Integer, List<String>> transferFlightsMap,int pid) {
		System.out.println("插入中转数据");
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		boolean isSuccessful = true;

		try {
			// 准备 SQL 语句，使用新的字段名称
			String sql = "INSERT INTO transit (`FlightId`, `StartFlightName`, `StartCity`, " +
					"`TransitCity`, `ArrivalCity`, `ArrivalFlightName`,`Pid`) VALUES (?, ?, ?, ?, ?, ?,?)";
			this.pst = cn.prepareStatement(sql);

			// 遍历 transferFlightsMap
			for (Map.Entry<Integer, List<String>> entry : transferFlightsMap.entrySet()) {
				int flightId = entry.getKey();
				List<String> routeList = entry.getValue();

				// 确保 routeList 包含至少5个元素
				if (routeList.size() < 5) {
					System.err.println("航班ID: " + flightId + " 的路线数据不足，无法插入");
					isSuccessful = false;
					continue;
				}

				// 设置参数
				this.pst.setInt(1, flightId);                 // FlightId
				this.pst.setString(2, routeList.get(0));       // StartFlightName
				this.pst.setString(3, routeList.get(1));       // StartCity
				this.pst.setString(4, routeList.get(2));       // TransitCity
				this.pst.setString(5, routeList.get(3));       // ArrivalCity
				this.pst.setString(6, routeList.get(4));       // ArrivalFlightName
				this.pst.setInt(7, pid);
				// 执行插入
				if (pst.executeUpdate() != 1) {
					isSuccessful = false;
					System.err.println("插入中转数据失败，航班ID: " + flightId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isSuccessful = false;
		} finally {
			try {
				if (pst != null) pst.close();
				if (cn != null) cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isSuccessful;
	}

	// 插入订单方法
	public boolean FoodInsert(int OrderId, int Food) {
		this.db = new DbConnect(); // 假设 DbConnect 是您用于管理数据库连接的类
		this.cn = this.db.Get_Connection(); // 获取数据库连接
		try {
			// 使用参数化查询避免SQL注入
			this.pst = cn.prepareStatement(
					"INSERT INTO `food`(`OrderId`, `Food`) VALUES (?, ?)"
			);

			// 设置参数
			this.pst.setInt(1, OrderId);
			this.pst.setInt(2, Food);

			// 执行插入操作
			this.re = this.pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源，防止资源泄露
			try {
				if (pst != null) pst.close();
				if (cn != null) cn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
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
