//用于连接 MySQL 数据库
package flight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnect {
	private static final String url = "jdbc:mysql://localhost:3306/flight?useUnicode=true&characterEncoding=utf-8";
	private static final String name = "com.mysql.cj.jdbc.Driver";//JDBC 驱动类名，用于加载 MySQL 驱动
	private static final String user = "shan";
	private static final String password = "shanshan";

	private Connection conn = null;//用于保存数据库连接对象

	//构造函数，用来创建数据库连接
	public DbConnect() {
		try {
			Class.forName(name);//加载 MySQL JDBC 驱动
			this.conn = DriverManager.getConnection(url, user, password);//获取数据库连接
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//方法，返回已创建的连接
	public Connection Get_Connection() {
		if (conn != null) {
			return this.conn;
		} else {
			System.err.println("Null Connection!");
			return this.conn;
		}

	}

	//方法，关闭连接
	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
