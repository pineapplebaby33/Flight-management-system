package flight;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DbDelete {
	private DbConnect db = null;
	private Connection cn = null;
	private boolean re = false;
	private PreparedStatement pst = null;

	//A-删除未发布航班√
	public boolean FlightDelete(int id,boolean isDomestic) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			// 根据 isDomestic 参数选择查询的表
			String tableName = isDomestic ? "`flight`" : "`flight1`";
			this.pst = cn.prepareStatement("DELETE FROM"+tableName+"  WHERE Id="+id+" and FlightStatus='UNPUBLISHED';");
			this.re = pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;
	}

	public boolean AdminDelete(int id) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn.prepareStatement("DELETE FROM `admin` WHERE Id=" + id
					+ ";");
			this.re = pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;
	}

	public boolean OrderDelete(int id) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn.prepareStatement("DELETE FROM `order` WHERE Id=" + id
					+ ";");
			this.re = pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;
	}

	public boolean PassengerDelete(int id) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();
		try {
			this.pst = cn.prepareStatement("DELETE FROM `passenger` WHERE Id="
					+ id + ";");
			this.re = pst.executeUpdate() == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.re;

	}

	//取消航班时，删除记录（只支持当前的套餐）
	public boolean deletePackageOrder(int PId, String packageStatus, int OId) {
		this.db = new DbConnect();
		this.cn = this.db.Get_Connection();

		boolean isDeleted = false;

		try {
			// 构建 SQL 删除语句
			String sql = "DELETE FROM package WHERE PId = ? AND Package = ? AND OId = ?";
			this.pst = cn.prepareStatement(sql);

			// 设置查询参数
			this.pst.setInt(1, PId);
			this.pst.setString(2, packageStatus);
			this.pst.setInt(3, OId);

			// 执行删除操作
			int affectedRows = pst.executeUpdate();

			// 如果删除成功，受影响的行数大于0
			isDeleted = affectedRows > 0;

			// 关闭连接
			this.cn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 返回删除结果
		return isDeleted;
	}



	public static void main(String[] args) {
		/*
		 * Admin/Passenger/Flight/Order��ɾ������Example DbDelete d=new DbDelete();
		 * 
		 * if(d.AdminDelete(3)) { System.out.println("ɾ���ɹ�"); } else {
		 * System.out.println("ɾ��ʧ��"); } if(d.PassengerDelete(3)) {
		 * System.out.println("ɾ���ɹ�"); } else { System.out.println("ɾ��ʧ��"); }
		 * if(d.FlightDelete(3)) { System.out.println("ɾ���ɹ�"); } else {
		 * System.out.println("ɾ��ʧ��"); } if(d.OrderDelete(3)){
		 * System.out.println("ɾ���ɹ�"); } else { System.out.println("ɾ��ʧ��"); }
		 */
	}
}
