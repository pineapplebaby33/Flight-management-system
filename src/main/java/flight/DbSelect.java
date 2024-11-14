package flight;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

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


	}
}
