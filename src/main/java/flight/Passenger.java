//乘客信息，取消航班预定，返回预定列表
package flight;

import frame.Research;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Passenger {
	private int id = 0;
	private String RealName = "";
	private String IdentityId = "";
	private String Password = "";
	private int[] OrderList;
	private int[] OrderList1;

	public Passenger(int id, String RealName, String IdentityId, String Password, String OrderList,String OrderList1) {
		this.id = id;
		this.RealName = RealName;
		this.IdentityId = IdentityId;
		this.Password = Password;
		this.OrderList = this.GetOrderList(OrderList);
		this.OrderList1 = this.GetOrderList(OrderList1);
	}


	/*
	public Order[] SelectOrders(String pwd) {
		if (Passenger.CheckPwd(this.getRealName(), pwd)) {
			return new DbSelect().PassengerOrders(this.getId());
		} else {
			System.err.println("�˿��������");
			return null;
		}
	}


	 */
	//检查密码√
	public static boolean CheckPwd(String RealName, String pwd) {
		DbSelect _s = new DbSelect();
		Passenger _a = _s.PassengerSelect(RealName, pwd);
		if (_a != null) {
			return true;
		}
		return false;

	}


	//预定航班√
	public static boolean ReserveFlight(int pid, int fid, String pwd,Boolean  isDomestic) {
		DbSelect select = new DbSelect();
		Flight f = select.FlightSelect(fid,isDomestic);
		//检查航班状态
		if (f.getFlightStatus().equals("AVAILABLE")) {
			Passenger p = select.PassengerSelect(pid);
			// 验证密码
			if (Passenger.CheckPwd(p.getRealName(), pwd)) {
				// 检查乘客是否已预定该航班
				if (select.OrderSelect(pid, fid,"",isDomestic) == null) {
					DbInsert insert = new DbInsert();
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
					String CreateDate = df.format(new Date());
					// 插入订单
					boolean re = insert.OrderInsert(p.getId(), p.getId(),
							f.getId(), CreateDate, "PAID",isDomestic);
					// 更新航班和乘客信息
					re = re && Flight.ReserveFlight(pid, fid,isDomestic)
							&& p.UpdateOrderList(fid,isDomestic);
					if (re) {
						return true;
					}
				} else {
					System.err.println("该乘客已预订该航班，无法重复预订");
				}
			} else {
				System.err.println("密码错误");
				return false;
			}
		} else {
			System.err.println("航班状态异常，无法预订");
			return false;
		}
		return false;
	}

	/*
	public static Order ReserveFlight(int pid, int fid, String pwd, int mode) {

		DbSelect select = new DbSelect();
		Flight f = select.FlightSelect(fid);

		if (f.getFlightStatus().equals("AVAILABLE")) {
			Passenger p = select.PassengerSelect(pid);

			if (Passenger.CheckPwd(p.getRealName(), pwd)) {
				if (select.OrderSelect(pid, fid) == null) {

					DbInsert insert = new DbInsert();
					// �������ڸ�ʽ
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd-HH-mm-ss");
					String CreateDate = df.format(new Date());

					boolean re = insert.OrderInsert(p.getId(), p.getId(),
							f.getId(), CreateDate, "PAID");
					// ����Flight��Passenger�б��CurrentPassengers������Passenger��OrderList
					re = re && Flight.ReserveFlight(pid, fid)
							&& p.UpdateOrderList(fid);
					if (re) {
						return select.OrderSelect(pid, fid);
					}
				} else {
					System.err.println("�����Ѵ��ڣ��޷��ظ���Ʊ");
				}
			} else {
				System.err.println("�˿��������");
				return null;
			}
		} else {
			System.err.println("����״̬�쳣������Ԥ��");
			return null;
		}
		return null;
	}
*/

	//订单表_添加P.OrderList
	public boolean UpdateOrderList(int fid,boolean isDomestic) {
		int[] _o = isDomestic ? this.getOrderList():this.getOrderList1();
		String OrderList = "";
		if (_o != null) {
			for (int i = 0; i < _o.length; i++) {
				OrderList += _o[i] + ";";
			}
		}
		OrderList += fid + ";";
		return new DbUpdate().UpdateOrderList(this.getId(), OrderList,isDomestic);
	}

	//订单表_移除p.OrderList
	public boolean UpdateOrderList2(int fid,boolean isDomestic) {
		int[] _o = isDomestic ? this.getOrderList():this.getOrderList1();
		String OrderList = "";
		if (_o != null) {
			for (int i = 0; i < _o.length; i++) {
				if (_o[i] != fid) {
					OrderList += _o[i] + ";";
				}
			}
		}
		return new DbUpdate().UpdateOrderList(this.getId(), OrderList,isDomestic);
	}


	//取消航班√
	public static boolean UnsubscribeFlight(int pid, int fid, String pwd,boolean isDomestic) {
		DbSelect select = new DbSelect();
		Flight f = select.FlightSelect(fid,isDomestic);//获取航班信息
		if (!f.getFlightStatus().equals("TERMINATE")) {//检查状态，如果航班终止，无法取消订单
			Passenger p = select.PassengerSelect(pid);
			if (Passenger.CheckPwd(p.getRealName(), pwd)) {
				if (select.OrderSelect(pid, fid, isDomestic) != null) {
					boolean re = f.UnreserveFlight(pid, fid,isDomestic)
							&& p.UpdateOrderList2(fid,isDomestic);
					if (re) {
						return true;
					}
				} else {
					System.err.println("订单不存在，无法取消");
				}
			} else {
				System.err.println("密码不正确");
				return false;
			}
		} else {
			System.err.println("航班已终止，无法取消");
			return false;
		}
		return false;

	}
	
	public int getId() {
		return id;
	}

	public String getRealName() {
		return RealName;
	}

	public String getIdentityId() {
		return IdentityId;
	}

	public String getPassword() {
		return Password;
	}

	public int[] getOrderList() {
		return OrderList;
	}

	public int[] getOrderList1() {
		return OrderList1;
	}

	public Passenger GetPassengerById(int id) {
		DbSelect select = new DbSelect();
		return select.PassengerSelect(id);
	}

	public String GetOrderString(int[] oi) {
		String s = "";
		for (int i = 0; i < oi.length; i++) {
			s += Integer.toString(oi[i]) + ";";
		}
		return s;

	}

	public int[] GetOrderList(String _o) {
		if (_o.length() > 0) {
			String[] _s;
			_s = _o.split(";");
			int[] _t = new int[_s.length];
			for (int i = 0; i < _s.length; i++) {
				_t[i] = Integer.parseInt(_s[i]);
			}

			return _t;
		}
		return null;
	}

	public static void main(String[] args) {
		// System.out.println(Passenger.UnsubscribeFlight(3, 2, "balabala"));

	}
}
