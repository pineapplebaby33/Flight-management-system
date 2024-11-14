package flight;

import frame.Research;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DbUpdate {
	private DbConnect db = null;
	private Connection cn = null;
	private boolean re = false;
	private PreparedStatement pst = null;


	public boolean PassengerUpdate(int id, String RealName, String IdentityId,
			String Password, String OrderList) {

		Password = Encode.MD5(Password);
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn
					.prepareStatement("UPDATE `passenger` SET `RealName`='"
							+ RealName + "',`IdentityId`='" + IdentityId
							+ "',`Password`='" + Password + "',`OrderList`='"
							+ OrderList + "' WHERE Id=" + id + ";");
			this.re = pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;

	}


	//A-修改管理员个人信息√
	public boolean AdminUpdate(int id, String username, String pwd) {
		pwd = Encode.MD5(pwd);
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn.prepareStatement("update admin SET Username='"
					+ username + "',Password='" + pwd + "' WHERE Id=" + id
					+ ";");
			this.re = pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;

	}

	//P-更新flight表√
	public boolean FlightUpdate(int id, String StartTime, String ArrivalTime,
			String StartCity, String ArrivalCity, String DepartureDate,
			float price, int CurrentPassengers, int SeatCapacity,
			String FlightStatus, String PassengerId, String FlightName,boolean isDomestic) {
		if (!Flight.IsFlight(StartTime, ArrivalTime, StartCity, ArrivalCity,
				DepartureDate, price, CurrentPassengers, SeatCapacity,
				FlightStatus, PassengerId, FlightName)) {
			System.err.println("Flight数据不规范，无法更新");
			return false;
		}
		DbSelect select = new DbSelect();
		Flight f = select.FlightSelect(id,isDomestic);
		if (Flight.IsUpdateFlight(id, StartTime, ArrivalTime, StartCity,
				ArrivalCity, DepartureDate, price, CurrentPassengers,
				SeatCapacity, f.getFlightStatus(), PassengerId, FlightName) == 1) {
			return false;//航班已经终止
		}
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		if (Flight.IsUpdateFlight(id, StartTime, ArrivalTime, StartCity,
				ArrivalCity, DepartureDate, price, CurrentPassengers,
				SeatCapacity, f.getFlightStatus(), PassengerId, FlightName) == 3) {
			try {
				// 根据 isDomestic 参数选择查询的表
				String tableName = isDomestic ? "`flight`" : "`flight1`";

				String _sql = "UPDATE " + tableName + " SET Price=" + price
						+ ",CurrentPassengers=" + CurrentPassengers
						+ ",SeatCapacity=" + SeatCapacity + ", PassengerId='"
						+ PassengerId + "',FlightStatus='" + FlightStatus
						+ "' WHERE Id=" + id + ";";
				this.pst = cn.prepareStatement(_sql);

				this.re = pst.executeUpdate() == 1;

			} catch (Exception e) {
				e.printStackTrace();
			}
			return this.re;
		}
		if (Flight.IsUpdateFlight(id, StartTime, ArrivalTime, StartCity,
				ArrivalCity, DepartureDate, price, CurrentPassengers,
				SeatCapacity, f.getFlightStatus(), PassengerId, FlightName) == 2) {
			try {
				// 根据 isDomestic 参数选择查询的表
				String tableName = isDomestic ? "`flight`" : "`flight1`";
				this.pst = cn.prepareStatement("UPDATE" + tableName + " SET StartTime='"
						+ StartTime + "',ArrivalTime='" + ArrivalTime
						+ "',StartCity='" + StartCity + "',ArrivalCity='"
						+ ArrivalCity + "',DepartureDate='" + DepartureDate
						+ "',Price=" + price + ",CurrentPassengers="
						+ CurrentPassengers + ",SeatCapacity=" + SeatCapacity
						+ ",FlightStatus='" + FlightStatus + "',PassengerId='"
						+ PassengerId + "',FlightName='" + FlightName
						+ "' WHERE Id=" + id + ";");
				this.re = pst.executeUpdate() == 1;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.re;

	}

	//P-更新Order表删除订单时使用√
	public boolean OrderUpdate(int id, int PassengerId, String Seat, int FlightId,
			String CreateDate, String Status,boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`order`" : "`order1`";
			// 使用占位符 ? 避免直接拼接参数
			this.pst = cn.prepareStatement("UPDATE " + tableName + " SET `PassengerId` = ?, `Seat` = ?, `FlightId` = ?, `CreateDate` = ?, `Status` = ? WHERE Id = ?");

			// 使用 PreparedStatement 设置参数
			this.pst.setInt(1, PassengerId);          // PassengerId 是整数
			this.pst.setString(2, Seat);               // Seat 是字符串，比如 "20C"
			this.pst.setInt(3, FlightId);              // FlightId 是整数
			this.pst.setString(4, CreateDate);         // CreateDate 是字符串或日期格式
			this.pst.setString(5, Status);             // Status 是字符串
			this.pst.setInt(6, id);                    // id 是整数，WHERE 条件中的标识符

			// 执行更新操作
			this.pst.executeUpdate();
			this.re = pst.executeUpdate() == 1;


// 使用 PreparedStatement 设置参数
			this.pst.setInt(1, PassengerId);         // PassengerId 是整数
			this.pst.setString(2, Seat);              // Seat 是字符串，如 "6D"
			this.pst.setInt(3, FlightId);             // FlightId 是整数
			this.pst.setString(4, CreateDate);        // CreateDate 是字符串，格式应符合数据库要求
			this.pst.setString(5, Status);            // Status 是字符串
			this.pst.executeUpdate();

			this.re = pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;

	}


	//P-更新Passenger表订单列+√
	public boolean UpdateOrderList(int pid, String OrderList, boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 的值选择要更新的列名
			String columnName = isDomestic ? "OrderList" : "OrderList1";
			// 使用占位符防止 SQL 注入
			this.pst = cn.prepareStatement("UPDATE `passenger` SET `" + columnName + "` = ? WHERE Id = ?");
			this.pst.setString(1, OrderList);
			this.pst.setInt(2, pid);

			this.re = pst.executeUpdate() == 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;
	}


	public static void main(String[] args) {

	}
}
