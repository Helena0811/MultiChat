/*
 * 1. �����ͺ��̽� ���� ������ DB������ �ʿ��� Ŭ������ �ߺ��ؼ� �������� �ʾƵ� ��!
 * 2. �ν��Ͻ��� ������ �� ���� �ΰ� ���(SingleTon)
 * -> ���ø����̼� ���� �� �����Ǵ� Connection ��ü�� �ϳ��� ����
 * */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	
	static private DBManager instance;
	
	Connection con;
	
	private String driver="oracle.jdbc.driver.OracleDriver";
	private String url="jdbc:oracle:thin:@localhost:1521:XE";
	private String user="batman";
	private String password="1234";
	
	private DBManager(){
		/*
		 * 1. driver �ε�
		 * 2. ����
		 * 3. ������ ����
		 * 4. ���� ����
		 * */
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(url, user, password);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static public DBManager getInstance() {
		if(instance==null){
			instance=new DBManager();
		}
		return instance;
	}

	public Connection getConnection() {
		return con;
	}

	public void disConnect(Connection con){
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
