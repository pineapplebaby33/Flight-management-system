package flight;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbSelect {
	private DbConnect db = null;
	private Connection cn = null;
	private ResultSet ret = null;//接口，表示从数据库查询生成的结果集（类似于数据表）
	private PreparedStatement pst = null;//接口，用于表示预编译的 SQL 语句对象

	//A-查询并返回Admin√
	public Admin AdminSelect(String username) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn.prepareStatement("select * from admin where binary Username='" + username + "';",
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			this.ret = pst.executeQuery();//执行查询并将结果存储在 ret 中
		} catch (Exception e) {
			e.printStackTrace();
		}
		//处理查询结果
		try {
			ret.last();
			if (ret.getRow() > 0) {
				ret.beforeFirst();

				while (ret.next()) {
					Admin ad = new Admin(ret.getInt(1), ret.getString(2),
							ret.getString(3));//ID,username,password
					this.ret.close();
					this.cn.close();
					return ad;
				}

			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	//A-查询并返回Admin by id 查询管理员姓名时使用
	public Admin AdminSelect(int id) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn.prepareStatement("select * from admin where Id=" + id
					+ ";");
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		int _id = 1;
		String _s1 = "", _s2 = "";
		try {
			while (ret.next()) {
				_id = ret.getInt(1);
				_s1 = ret.getString(2);
				_s2 = ret.getString(3);
			}
			this.ret.close();
			this.cn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Admin p = new Admin(_id, _s1, _s2);
		db.close();
		return p;

	}

	public Admin[] AdminSelect() {

		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn.prepareStatement("select * from admin;");
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ret.last();
			if (ret.getRow() > 0) {
				Admin[] ad = new Admin[ret.getRow()];
				ret.beforeFirst();
				int _i = 0;
				while (ret.next()) {
					Admin x = new Admin(ret.getInt(1), ret.getString(2),
							ret.getString(3));
					ad[_i] = x;
					_i++;
				}
				this.ret.close();
				this.cn.close();
				return ad;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	//查询并返回Passenger√
	public Passenger PassengerSelect(String RealName, String pwd) {
		pwd = Encode.MD5(pwd);
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn
					.prepareStatement("select * from passenger where RealName='" + RealName + "' and Password='" + pwd + "';"
							,ResultSet.TYPE_SCROLL_INSENSITIVE
							,ResultSet.CONCUR_READ_ONLY);
			this.ret = pst.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ret.last();
			if (ret.getRow() > 0) {
				ret.beforeFirst();

				while (ret.next()) {
					Passenger ad = new Passenger(ret.getInt(1),
							ret.getString(2), ret.getString(3),
							ret.getString(4), ret.getString(5),
							ret.getString(6));//Id,RealName,IdentityId,Password,OrderList,OrderList1
					this.ret.close();
					this.cn.close();
					return ad;
				}

			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public Passenger[] PassengerSelect() {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn.prepareStatement("select * from passenger;");
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ret.last();
			if (ret.getRow() > 0) {
				Passenger[] ad = new Passenger[ret.getRow()];
				ret.beforeFirst();
				int _i = 0;
				while (ret.next()) {
					Passenger x = new Passenger(ret.getInt(1),
							ret.getString(2),
							ret.getString(3),
							ret.getString(4),
							ret.getString(5),
							ret.getString(6));
					ad[_i] = x;
					_i++;
				}
				this.ret.close();
				this.cn.close();
				return ad;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	//P-查询并返回Passenger by id√
	//1.查看订单信息
	//2.查询航班信息
	public Passenger PassengerSelect(int id) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn.prepareStatement("select * from passenger where Id="
					+ id + ";");
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		int _id = 1;
		String _s1 = "", _s2 = "", _s3 = "", _s4 = "", _s5 = "";
		try {
			while (ret.next()) {
				_id = ret.getInt(1);
				_s1 = ret.getString(2);
				_s2 = ret.getString(3);
				_s3 = ret.getString(4);
				_s4 = ret.getString(5);
				_s5 = ret.getString(5);
			}
			this.ret.close();
			this.cn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Passenger p = new Passenger(_id, _s1, _s2, _s3, _s4, _s5);
		db.close();
		return p;
	}

	//查询密码
	public String getPasswordByPid(int pid) {
		String password = null;
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();

		try {
			// SQL 查询语句
			String sql = "SELECT Password FROM passenger WHERE Id = ?";
			this.pst = cn.prepareStatement(sql);

			// 设置查询参数
			this.pst.setInt(1, pid);

			// 执行查询
			this.ret = pst.executeQuery();

			// 获取查询结果
			if (ret.next()) {
				password = ret.getString("Password");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			try {
				if (ret != null) ret.close();
				if (pst != null) pst.close();
				if (cn != null) cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return password;
	}


	//P-查询并返回Order(指定订单)√
	public Order OrderSelect(int pid, int fid,boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`order`" : "`order1`";
			String _sql = "select * from "+ tableName +" where PassengerId=" + pid
					+ " and FlightId=" + fid + ";";
			this.pst = cn.prepareStatement(_sql,ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ret.last();
			if (ret.getRow() > 0) {
				ret.beforeFirst();
				int _id = 1;
				int _i1 = 1,  _i3 = 1;
				String _s1 = "", _s2 = "",_i2 = "";
				while (ret.next()) {
					_id = ret.getInt(1);
					_i1 = ret.getInt(2);
					_i2 = ret.getString(3);
					_i3 = ret.getInt(4);
					_s1 = ret.getString(5);
					_s2 = ret.getString(6);
				}
				this.ret.close();
				this.cn.close();
				Order p = new Order(_id, _i1, _i2, _i3, _s1, _s2,isDomestic);
				db.close();
				return p;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//P-查询并返回Order订单(成功支付的)√
	public Order OrderSelect(int pid, int fid, String mode,boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`order`" : "`order1`";
			String _sql = "select * from " + tableName +" where PassengerId=" + pid + " and FlightId=" + fid + " and Status='PAID';";
			this.pst = cn.prepareStatement(_sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ret.last();
			if (ret.getRow() > 0) {
				ret.beforeFirst();
				int _id = 1;
				int _i1 = 1,  _i3 = 1;
				String _s1 = "", _s2 = "",_i2 = "";
				while (ret.next()) {
					_id = ret.getInt(1);
					_i1 = ret.getInt(2);
					_i2 = ret.getString(3);
					_i3 = ret.getInt(4);
					_s1 = ret.getString(5);
					_s2 = ret.getString(6);
				}
				this.ret.close();
				this.cn.close();
				Order p = new Order(_id, _i1, _i2, _i3, _s1, _s2,isDomestic);
				db.close();
				return p;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//A-查询并返回Order订单 ordershow√
	public Order[] OrderSelect(boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`order`" : "`order1`";
			this.pst = cn.prepareStatement("select * from "+tableName+";",ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ret.last();
			if (ret.getRow() > 0) {
				Order[] ad = new Order[ret.getRow()];
				ret.beforeFirst();
				int _i = 0;
				while (ret.next()) {
					Order x = new Order(ret.getInt(1), ret.getInt(2),
							ret.getString(3), ret.getInt(4), ret.getString(5),
							ret.getString(6),isDomestic);
					ad[_i] = x;
					_i++;
				}
				this.ret.close();
				this.cn.close();
				return ad;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	//P-查询并返回Order by id 取消订单时使用√
	public Order OrderSelect(int id,boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`order`" : "`order1`";
			this.pst = cn.prepareStatement("select * from " +tableName+ " where Id=" + id + ";");
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		int _id = 1;
		int _i1 = 1, _i3 = 1;
		String _s1 = "", _s2 = "", _i2 = "";
		try {
			while (ret.next()) {
				_id = ret.getInt(1);
				_i1 = ret.getInt(2);
				_i2 = ret.getString(3);
				_i3 = ret.getInt(4);
				_s1 = ret.getString(5);
				_s2 = ret.getString(6);
			}
			this.ret.close();
			this.cn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Order p = new Order(_id, _i1, _i2, _i3, _s1, _s2,isDomestic);
		db.close();
		return p;

	}

	//P-查询并返回Order[] 乘客界面查询订单时使用√
	public Order[] OrderSelect(int pid,boolean isDomestic,String s) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`order`" : "`order1`";
			this.pst = cn.prepareStatement("select * from " + tableName + " where PassengerId=" + pid + " and Status='PAID';",
											ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			this.ret = pst.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			ret.last();
			if (ret.getRow() > 0) {
				Order[] ad = new Order[ret.getRow()];
				ret.beforeFirst();
				int _i = 0;
				while (ret.next()) {
					Order x = new Order(ret.getInt(1), ret.getInt(2),
							ret.getString(3), ret.getInt(4), ret.getString(5),
							ret.getString(6),isDomestic);
					ad[_i] = x;
					_i++;
				}
				this.ret.close();
				this.cn.close();
				return ad;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	//A-管理员准确寻找√(有时间)
	public Flight[] FlightSelect(String StartTime, String StartCity, String ArrivalCity,boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`flight`" : "`flight1`";
			String _sql = "select * from "+tableName+"where";
			if (StartTime != null && StartTime.length() > 0) {
				_sql += " StartTime like '" + StartTime + "%'";
				_sql += " and ";
			}
			if (StartCity != null && StartCity.length() > 0) {
				_sql += " StartCity = '" + StartCity + "'";
				_sql += " and ";
			}
			if (ArrivalCity != null && ArrivalCity.length() > 0) {
				_sql += " ArrivalCity = '" + ArrivalCity + "'";
				_sql += " and ";
			}

			_sql = _sql.substring(0, _sql.length() - 5);
			_sql += ";";
			this.pst = cn.prepareStatement(_sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ret.last();
			if (ret.getRow() > 0) {
				Flight[] ad = new Flight[ret.getRow()];
				ret.beforeFirst();
				int _i = 0;
				while (ret.next()) {
					Flight x = new Flight(ret.getInt(1), ret.getString(2),
							ret.getString(3), ret.getString(4),
							ret.getString(5), ret.getString(6),
							ret.getFloat(7), ret.getInt(8), ret.getInt(9),
							ret.getString(10), ret.getString(11),
							ret.getString(12));
					ad[_i] = x;
					_i++;
				}
				this.ret.close();
				this.cn.close();
				return ad;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	//A-管理员准确寻找√(无时间)
	public Flight[] FlightSelect(String StartCity, String ArrivalCity,boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`flight`" : "`flight1`";
			String _sql = "select * from "+tableName+"where";
			if (StartCity != null && StartCity.length() > 0) {
				_sql += " StartCity = '" + StartCity + "'";
				_sql += " and ";
			}
			if (ArrivalCity != null && ArrivalCity.length() > 0) {
				_sql += " ArrivalCity = '" + ArrivalCity + "'";
				_sql += " and ";
			}

			_sql = _sql.substring(0, _sql.length() - 5);
			_sql += ";";
			this.pst = cn.prepareStatement(_sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ret.last();
			if (ret.getRow() > 0) {
				Flight[] ad = new Flight[ret.getRow()];
				ret.beforeFirst();
				int _i = 0;
				while (ret.next()) {
					Flight x = new Flight(ret.getInt(1), ret.getString(2),
							ret.getString(3), ret.getString(4),
							ret.getString(5), ret.getString(6),
							ret.getFloat(7), ret.getInt(8), ret.getInt(9),
							ret.getString(10), ret.getString(11),
							ret.getString(12));
					ad[_i] = x;
					_i++;
				}
				this.ret.close();
				this.cn.close();
				return ad;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	//没有调用
	public Flight[] FlightSelect(String FlightName,boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`flight`" : "`flight1`";
			String _sql = "select * from "+ tableName + " where FlightName like '%"
					+ FlightName + "%';";
			this.pst = cn.prepareStatement(_sql);
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ret.last();
			if (ret.getRow() > 0) {
				Flight[] ad = new Flight[ret.getRow()];
				ret.beforeFirst();
				/*
				 * ע�⣺getRow()���������ǻ�����������ǻ��ret��ǰָ��λ ������Ҫ��ret�����������󣬻�ȡrow����������
				 */
				int _i = 0;
				while (ret.next()) {
					Flight x = new Flight(ret.getInt(1), ret.getString(2),
							ret.getString(3), ret.getString(4),
							ret.getString(5), ret.getString(6),
							ret.getFloat(7), ret.getInt(8), ret.getInt(9),
							ret.getString(10), ret.getString(11),
							ret.getString(12));
					ad[_i] = x;
					_i++;
				}
				this.ret.close();
				this.cn.close();
				return ad;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	//A-查询并返回Flight[] 更新管理员航班列表时使用，无航班状态限制√
	public Flight[] FlightSelect(boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`flight`" : "`flight1`";
			this.pst = cn.prepareStatement("select * from "+ tableName +";",
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ret.last();
			if (ret.getRow() > 0) {
				Flight[] ad = new Flight[ret.getRow()];
				ret.beforeFirst();
				int _i = 0;
				while (ret.next()) {
					Flight x = new Flight(ret.getInt(1), ret.getString(2),
							ret.getString(3), ret.getString(4),
							ret.getString(5), ret.getString(6),
							ret.getFloat(7), ret.getInt(8), ret.getInt(9),
							ret.getString(10), ret.getString(11),
							ret.getString(12));
					ad[_i] = x;
					_i++;
				}
				this.ret.close();
				this.cn.close();
				return ad;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	//P-查询并返回Flight[] 更新乘客航班列表时使用，有航班状态限制√
	public Flight[] FlightSelectForPass(boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`flight`" : "`flight1`";
			this.pst = cn.prepareStatement(
					"SELECT * FROM " + tableName + " WHERE FlightStatus='AVAILABLE' OR FlightStatus='FULL';",
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY
			);
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ret.last();
			if (ret.getRow() > 0) {
				Flight[] ad = new Flight[ret.getRow()];
				ret.beforeFirst();
				//注意：getRow() 返回的是总行数，不能直接使用，需要在 ret 游标移动到最后一行后再使用
				int _i = 0;
				while (ret.next()) {
					Flight x = new Flight(ret.getInt(1), ret.getString(2),
							ret.getString(3), ret.getString(4),
							ret.getString(5), ret.getString(6),
							ret.getFloat(7), ret.getInt(8), ret.getInt(9),
							ret.getString(10), ret.getString(11),
							ret.getString(12));
					ad[_i] = x;
					_i++;
				}
				this.ret.close();
				this.cn.close();
				return ad;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	//A-P-查询并返回Flight by id√
	public Flight FlightSelect(int id,boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`flight`" : "`flight1`";
			this.pst = cn.prepareStatement("SELECT * FROM" + tableName + " WHERE Id=" + id + ";");
			this.ret = pst.executeQuery();

		} catch (Exception e) {
			e.printStackTrace();
		}
		int _id = 1;
		String _s1 = "", _s2 = "", _s3 = "", _s4 = "", _s5 = "";
		float _p = 1f;
		int _i1 = 1, _i2 = 1;
		String _s6 = "", _s7 = "", _s8 = "";
		try {
			while (ret.next()) {
				_id = ret.getInt(1);
				_s1 = ret.getString(2);
				_s2 = ret.getString(3);
				_s3 = ret.getString(4);
				_s4 = ret.getString(5);
				_s5 = ret.getString(6);
				_p = ret.getFloat(7);
				_i1 = ret.getInt(8);
				_i2 = ret.getInt(9);
				_s6 = ret.getString(10);
				_s7 = ret.getString(11);
				_s8 = ret.getString(12);
			}
			this.ret.close();
			this.cn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(_id+" "+_s1+" "+_s2+" "+_s3+" "+_s4+" "+_s5+" "+_p+" "+_i1+" "+_i2+" "+_s6+" "+_s7);
		Flight f = new Flight(_id, _s1, _s2, _s3, _s4, _s5, _p, _i1, _i2, _s6,
				_s7, _s8);

		return f;
	}

	//P-查询并返回Flight[] 改签时使用，有航班状态限制√
	public Flight[] FlightSelectForTransit(String startCity, String arrivalCity, boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		List<Flight> flights = new ArrayList<>(); // 使用列表来暂存查询结果

		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`flight`" : "`flight1`";

			// SQL 查询语句，按出发城市和目的城市过滤
			String sql = "SELECT * FROM " + tableName + " WHERE StartCity = ? AND ArrivalCity = ? AND (FlightStatus='AVAILABLE' OR FlightStatus='FULL')";
			this.pst = cn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			// 设置查询参数
			this.pst.setString(1, startCity);
			this.pst.setString(2, arrivalCity);

			// 执行查询
			this.ret = pst.executeQuery();

			// 遍历查询结果并创建 Flight 对象
			while (ret.next()) {
				Flight x = new Flight(
						ret.getInt("Id"),
						ret.getString("StartTime"),
						ret.getString("ArrivalTime"),
						ret.getString("StartCity"),
						ret.getString("ArrivalCity"),
						ret.getString("DepartureDate"),
						ret.getFloat("Price"),
						ret.getInt("CurrentPassengers"),
						ret.getInt("SeatCapacity"),
						ret.getString("FlightStatus"),
						ret.getString("PassengerId"),
						ret.getString("FlightName")
				);
				flights.add(x); // 将 Flight 对象添加到列表
			}

			// 将 List 转换为 Flight[]
			if (!flights.isEmpty()) {
				return flights.toArray(new Flight[0]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ret != null) ret.close();
				if (pst != null) pst.close();
				if (cn != null) cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null; // 如果没有符合条件的记录，则返回 null
	}

	//p-查询并返回Flight by pid，生成航班推荐时使用
	public Flight[] FlightSelectByPassengerId(int PassengerId, boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`flight`" : "`flight1`";
			String _sql = "SELECT * FROM " + tableName + " WHERE PassengerId LIKE ? OR PassengerId LIKE ?";

			// 准备 SQL 语句
			this.pst = cn.prepareStatement(_sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			this.pst.setString(1, "%;" + PassengerId + ";%"); // 匹配中间包含的情况
			this.pst.setString(2, PassengerId + ";%");       // 匹配以 PassengerId 开头的情况
			this.ret = pst.executeQuery();

			// 处理查询结果
			ret.last(); // 移动到结果集最后一行，获取行数
			if (ret.getRow() > 0) {
				Flight[] flights = new Flight[ret.getRow()];
				ret.beforeFirst(); // 回到结果集的起始位置
				int index = 0;

				// 遍历结果集
				while (ret.next()) {
					// 创建 Flight 对象并存储到数组
					Flight flight = new Flight(
							ret.getInt(1),    // Flight ID
							ret.getString(2), // StartTime
							ret.getString(3), // StartCity
							ret.getString(4), // ArrivalCity
							ret.getString(5), // FlightName
							ret.getString(6), // PassengerId
							ret.getFloat(7),  // Price
							ret.getInt(8),    // AvailableSeats
							ret.getInt(9),    // TotalSeats
							ret.getString(10),// AircraftType
							ret.getString(11),// Airline
							ret.getString(12) // OtherInfo
					);
					flights[index] = flight;
					index++;
				}

				// 关闭连接
				this.ret.close();
				this.cn.close();
				return flights; // 返回结果
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (this.ret != null) this.ret.close();
				if (this.cn != null) this.cn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null; // 如果没有结果或发生异常，返回 null
	}


	// 根据 flightname 查询起始城市和目的城市
	// 根据 flightname 查询起始城市和目的城市
	public List<String> getCitiesByFlightName(String flightName) {
		List<String> cities = new ArrayList<>(); // 用于存储起始城市和目的城市
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();

		try {
			// SQL 查询语句
			String sql = "SELECT StartCity, ArrivalCity FROM flight WHERE FlightName = ?";
			this.pst = cn.prepareStatement(sql);

			// 设置查询参数
			this.pst.setString(1, flightName);

			// 执行查询
			this.ret = pst.executeQuery();

			// 获取查询结果
			if (ret.next()) {
				cities.add(ret.getString("StartCity"));   // 添加起始城市到列表
				cities.add(ret.getString("ArrivalCity")); // 添加目的城市到列表
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			try {
				if (ret != null) ret.close();
				if (pst != null) pst.close();
				if (cn != null) cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cities;
	}



	//A-查询预定信息√
	public BookingInfo[] BookingInfoSelect(Boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`order`" : "`order1`";
			this.pst = cn.prepareStatement("select * from "+tableName+";",ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			this.ret = pst.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ret.last();
			if (ret.getRow() > 0) {
				BookingInfo[] ad = new BookingInfo[ret.getRow()];
				ret.beforeFirst();
				int _i = 0;
				while (ret.next()) {
					Order x = new Order(ret.getInt(1), ret.getInt(2),
							ret.getString(3), ret.getInt(4), ret.getString(5),
							ret.getString(6),isDomestic);
					BookingInfo b = new BookingInfo(x.getFlightId(),
													x.getPassengerId(),
													x.getPassengerId().getRealName(),
													x.getPassengerId().getIdentityId(),
													x.getId(),
													x.getSeat(),
													x.getCreateDate(),
							x.getStatus()
					);
					ad[_i] = b;
					_i++;
				}
				this.ret.close();
				this.cn.close();

				return ad;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	//获取最新的ID,自动更新航班√
	public int NewGetId(boolean isDomestic){
			this.db = new DbConnect();
			this.cn = this.db.Get_Connection();
			int lastId = -1; // 默认值，表示未找到
			try {
				// 根据 isDomestic 参数选择查询的表
				String tableName = isDomestic ? "`flight`" : "`flight1`";
				this.pst = cn.prepareStatement("SELECT id FROM " + tableName + " ORDER BY id DESC LIMIT 1;");
				this.ret = pst.executeQuery();

				if (ret.next()) { // 获取第一条记录，即 id 最大值
					lastId = ret.getInt("id");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (this.ret != null) this.ret.close();
					if (this.pst != null) this.pst.close();
					if (this.cn != null) this.cn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return lastId;


	}

	// 查询指定日期是否有航班
	public boolean hasFlightOnDate(String date,boolean isDomestic) {
		boolean hasFlight = false;
		PreparedStatement pst = null;
		ResultSet rs = null;
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`flight`" : "`flight1`";
			// 定义 SQL 查询语句
			String sql = "SELECT COUNT(*) FROM "+tableName+" WHERE DATE(StartTime) = ?";
			pst = cn.prepareStatement(sql);
			pst.setString(1, date);  // 设置查询参数为指定日期
			rs = pst.executeQuery();
			if (rs.next()) {
				// 获取查询结果，如果有航班则返回 true
				hasFlight = rs.getInt(1) > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pst != null) pst.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hasFlight;
	}

	//查询中转数据
	public String queryTransit(int pid, String flightName) {
		System.out.println("查询 transit 表...");
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		String result = "";

		try {
			// SQL 查询语句
			String sql = "SELECT * FROM transit WHERE Pid = ? AND (StartFlightName = ? OR ArrivalFlightName = ?)";
			this.pst = cn.prepareStatement(sql);

			// 设置参数
			this.pst.setInt(1, pid);
			this.pst.setString(2, flightName);
			this.pst.setString(3, flightName);

			// 执行查询
			this.ret = pst.executeQuery();

			// 处理查询结果
			if (ret.next()) {
				// 拼接结果字符串
				result = "起飞航班名: " + ret.getString("StartFlightName") +
						", 起飞城市: " + ret.getString("StartCity") +
						", 中转城市: " + ret.getString("TransitCity") +
						", 到达城市: " + ret.getString("ArrivalCity") +
						", 中转航班名: " + ret.getString("ArrivalFlightName");
			} else {
				result = "未找到匹配的记录";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "查询出错";
		} finally {
			try {
				if (ret != null) ret.close();
				if (pst != null) pst.close();
				if (cn != null) cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	//查询OrderID
	public Integer queryOrderId(int pid, int fid) {
		System.out.println("查询订单表...");
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		Integer orderId = null;

		try {
			// SQL 查询语句
			String sql = "SELECT Id FROM `order` WHERE PassengerId = ? AND FlightId = ? AND Status = 'PAID'";
			this.pst = cn.prepareStatement(sql);

			// 设置查询参数
			this.pst.setInt(1, pid);
			this.pst.setInt(2, fid);

			// 执行查询
			this.ret = pst.executeQuery();

			// 如果查询结果存在，则获取订单 ID
			if (ret.next()) {
				orderId = ret.getInt("Id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ret != null) ret.close();
				if (pst != null) pst.close();
				if (cn != null) cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return orderId;
	}

	//查找另一个航班名
	public String findOtherFlightName(int pid, String flightName) {
		System.out.println("查询 transit 表...");
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		String otherFlightName = null;

		try {
			// SQL 查询语句，查找 pid 匹配的记录，其中 StartFlightName 或 ArrivalFlightName 与 flightName 相等
			String sql = "SELECT StartFlightName, ArrivalFlightName FROM transit WHERE Pid = ? AND (StartFlightName = ? OR ArrivalFlightName = ?)";
			this.pst = cn.prepareStatement(sql);

			// 设置查询参数
			this.pst.setInt(1, pid);
			this.pst.setString(2, flightName);
			this.pst.setString(3, flightName);

			// 执行查询
			this.ret = pst.executeQuery();

			// 处理查询结果
			if (ret.next()) {
				String startFlightName = ret.getString("StartFlightName");
				String arrivalFlightName = ret.getString("ArrivalFlightName");

				// 如果 StartFlightName 等于传入的 flightName，则返回 ArrivalFlightName；反之亦然
				if (startFlightName.equals(flightName)) {
					otherFlightName = arrivalFlightName;
				} else if (arrivalFlightName.equals(flightName)) {
					otherFlightName = startFlightName;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ret != null) ret.close();
				if (pst != null) pst.close();
				if (cn != null) cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return otherFlightName;
	}

	//根据航班名查找fid
	public Integer findFlightIdByName(String flightName) {
		System.out.println("查询 flight 表...");
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		Integer flightId = null;

		try {
			// SQL 查询语句
			String sql = "SELECT Id FROM flight WHERE FlightName = ?";
			this.pst = cn.prepareStatement(sql);

			// 设置查询参数
			this.pst.setString(1, flightName);

			// 执行查询
			this.ret = pst.executeQuery();

			// 如果查询结果存在，则获取航班 ID
			if (ret.next()) {
				flightId = ret.getInt("Id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ret != null) ret.close();
				if (pst != null) pst.close();
				if (cn != null) cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flightId;
	}

	//返回起始地址
	public List<String> findCitiesByFlightNameAndPid(String flightName, int pid) {
		System.out.println("查询 transit 表...");
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		List<String> cities = new ArrayList<>();

		try {
			// SQL 查询语句
			String sql = "SELECT StartCity, ArrivalCity FROM transit WHERE Pid = ? AND (StartFlightName = ? OR ArrivalFlightName = ?)";
			this.pst = cn.prepareStatement(sql);

			// 设置查询参数
			this.pst.setInt(1, pid);
			this.pst.setString(2, flightName);
			this.pst.setString(3, flightName);

			// 执行查询
			this.ret = pst.executeQuery();

			// 处理查询结果
			if (ret.next()) {
				// 将 StartCity 和 ArrivalCity 添加到列表
				cities.add(ret.getString("StartCity"));   // 0 是起始地址
				cities.add(ret.getString("ArrivalCity")); // 1 是目的地址
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ret != null) ret.close();
				if (pst != null) pst.close();
				if (cn != null) cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cities;
	}

	// 根据 OrderId 查询 Food
	// 根据 OrderId 查询 Food
	public int QueryFoodByOrderId(int OrderId) {
		this.db = new DbConnect(); // 假设 DbConnect 是用于管理数据库连接的类
		this.cn = this.db.Get_Connection(); // 获取数据库连接
		int Food = -1; // 初始化为 -1，表示未找到

		try {
			// 使用参数化查询，防止SQL注入
			this.pst = cn.prepareStatement("SELECT `Food` FROM `food` WHERE `OrderId` = ?");

			// 设置参数
			this.pst.setInt(1, OrderId);

			// 执行查询
			this.ret = this.pst.executeQuery();

			// 获取查询结果
			if (ret.next()) {
				Food = ret.getInt("Food");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源，防止资源泄露
			try {
				if (ret != null) ret.close();
				if (pst != null) pst.close();
				if (cn != null) cn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return Food;
	}

	//根据pid查询package表，返回套餐状态
	public String queryPackageStatus(int pid) {
		System.out.println("查询 Package 表...");
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		String packageStatus = "无"; // 默认返回值为 "无"

		try {
			// SQL 查询语句，获取指定 PId 的当前套餐和次数
			String sql = "SELECT Package, COUNT(*) AS Count " +
					"FROM package " +
					"WHERE PId = ? " +
					"GROUP BY Package";
			this.pst = cn.prepareStatement(sql);

			// 设置查询参数
			this.pst.setInt(1, pid);

			// 执行查询
			this.ret = pst.executeQuery();

			// 存储每种套餐的计数
			Map<String, Integer> packageCounts = new HashMap<>();

			while (ret.next()) {
				String packageName = ret.getString("Package");
				int count = ret.getInt("Count");
				packageCounts.put(packageName, count);
			}

			// 判断逻辑
			if (packageCounts.containsKey("国外随心飞")) {
				int abroadCount = packageCounts.get("国外随心飞");
				if (abroadCount < 11) {
					packageStatus = "国外随心飞"; // 当前套餐未满
				}
			} else if (packageCounts.containsKey("国内随心飞")) {
				int domesticCount = packageCounts.get("国内随心飞");
				if (domesticCount < 11) {
					packageStatus = "国内随心飞"; // 当前套餐未满
				}
			} else if (packageCounts.containsKey("学生寒暑假")) {
				int studentCount = packageCounts.get("学生寒暑假");
				if (studentCount < 5) {
					packageStatus = "学生寒暑假"; // 当前套餐未满
				}
			} else {
				packageStatus = "无"; // 没有任何套餐记录
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ret != null) ret.close();
				if (pst != null) pst.close();
				if (cn != null) cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return packageStatus;
	}

	// 根据 pid 查询 package 表，返回所有订阅的套餐状态(名称+是否已满)
	public List<Map<String, Object>> queryAllPackageStatus(int pid) {
		System.out.println("查询 Package 表...");
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();

		// 用于存储所有订阅的套餐状态
		List<Map<String, Object>> packageStatusList = new ArrayList<>();

		try {
			// SQL 查询语句，获取指定 PId 的当前套餐和次数
			String sql = "SELECT Package, COUNT(*) AS Count " +
					"FROM package " +
					"WHERE PId = ? " +
					"GROUP BY Package";
			this.pst = cn.prepareStatement(sql);

			// 设置查询参数
			this.pst.setInt(1, pid);

			// 执行查询
			this.ret = pst.executeQuery();

			// 遍历查询结果，存储套餐信息
			while (ret.next()) {
				Map<String, Object> packageInfo = new HashMap<>();
				String packageName = ret.getString("Package");
				int count = ret.getInt("Count");

				// 存储套餐名称和计数
				packageInfo.put("Package", packageName);
				packageInfo.put("Count", count);

				// 判断是否达到最大限制
				boolean isFull = false;
				if (packageName.equals("国外随心飞") && count >= 11) {
					isFull = true;
				} else if (packageName.equals("国内随心飞") && count >= 11) {
					isFull = true;
				} else if (packageName.equals("学生寒暑假") && count >= 5) {
					isFull = true;
				}

				// 存储是否达到最大限制的状态
				packageInfo.put("IsFull", isFull);

				// 添加到结果列表
				packageStatusList.add(packageInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ret != null) ret.close();
				if (pst != null) pst.close();
				if (cn != null) cn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return packageStatusList;
	}

	// 根据 pid,packageStatus,OId 查询 package 表，返回特定套餐订单
	public PackageOrder PackageOrderSelect(int PId, String packageStatus, int OId) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();

		// 用于存储查询结果
		int id = 0;
		int passengerId = 0;
		String packageName = "";
		float price = 0;
		int orderId = 0;

		try {
			// 构建 SQL 查询语句
			String sql = "SELECT * FROM package WHERE PId = ? AND Package = ? AND OId = ?";
			this.pst = cn.prepareStatement(sql);

			// 设置查询参数
			this.pst.setInt(1, PId);
			this.pst.setString(2, packageStatus);
			this.pst.setInt(3, OId);

			// 执行查询
			this.ret = pst.executeQuery();

			// 解析查询结果
			if (ret.next()) {
				id = ret.getInt("id");
				passengerId = ret.getInt("PId");
				packageName = ret.getString("Package");
				price = ret.getFloat("Price");
				orderId = ret.getInt("OId");
			}

			// 关闭结果集和连接
			this.ret.close();
			this.cn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 构造并返回 PackageOrder 对象
		return new PackageOrder(id, passengerId, packageName, price, orderId);
	}






	public static void main(String[] args) {
		// System.out.println(new DbSelect().OrderSelect(3, 1));

// System.out.println(new DbSelect().PassengerSelect("a", "b"));

		/*
		 * 查询 Example
		 *
		 * DbSelect sa = new DbSelect();
		 * TestObject.print(sa.OrderSelect(1, 1));
		 * TestObject.print(sa.OrderSelect(2, 1));
		 * Flight[] x = sa.FlightSelect("", "", "某某", "某某");
		 * for (int i = 0; i < x.length; i++) {
		 *     TestObject.print(x[i]);
		 * }
		 */

		/*
		 * Select All Example
		 *
		 * DbSelect sa = new DbSelect();
		 * Passenger[] p = sa.PassengerSelect();
		 * Admin[] a = sa.AdminSelect();
		 * Flight[] f = sa.FlightSelect();
		 * for (int i = 0; i < f.length; i++) {
		 *     TestObject.print(f[i]);
		 * }
		 * System.out.println(sa.ret + "\n" + sa.cn);
		 */

		/*
		 * 乘客查询 Debug
		 * Passenger p = sa.PassengerSelect(1);
		 * TestObject.print(p);
		 */

		/*
		 * Admin 查询 Debug
		 * Admin a = sa.AdminSelect(1);
		 * TestObject.print(a);
		 */

		/*
		 * 航班查询 Debug
		 *
		 * Flight f = sa.FlightSelect(1);
		 * TestObject.print(f);
		 */

		DbSelect sa = new DbSelect();
		String ps = sa.queryPackageStatus(33);
		System.out.println(ps);

	}
}
