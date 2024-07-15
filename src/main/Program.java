package main;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import views.FrameMain;
import views.LoginForm;

public class Program {
	public static Connection conn;
	public static String connstr;

	public static ResultSet myReader;
	public static String servername = "";
	public static String servernameLeft = "";

	public static String username = "";
	public static String mlogin = "";
	public static String password = "";

	public static String database = "";
	public static String remotelogin = "";
	public static String remotepassword = "";

	public static String mloginDN = "";
	public static String passwordDN = "";
	public static String mGroup = "";
	public static String mHoten = "";

	public static int mChinhanh = 0;
	public static List<String> macn;
	public static Map<String, String> servers;
	public static LoginForm login;

	public static FrameMain frmMain;
	public static Statement statement = null;

	public static int Connect() {
		if (Program.conn != null) {
			try {
				Program.conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Program.connstr = "jdbc:sqlserver://" + Program.servername + ";databaseName=" + Program.database
					+ ";encrypt=true;trustServerCertificate=true";
			try {
				Program.conn = DriverManager.getConnection(Program.connstr, Program.mlogin, Program.password);
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null,
						"Lỗi kết nối cơ sở dữ liệu.\nBạn xem lại user name và password.\n " + e.getMessage(), "Error",
						JOptionPane.WARNING_MESSAGE);
				return 0;
			}

			System.out.println(Program.connstr);
			return 1;
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "", e.getMessage(), JOptionPane.WARNING_MESSAGE);
			return 0;
		}
	}

	public static void readInfoDBFile() {
        String filePath = "static/infoDB.txt"; 
        try {
            InputStream inputStream = Program.class.getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new IllegalArgumentException("file not found! " + filePath);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            Program.servername = reader.readLine();
            Program.remotelogin = reader.readLine();
            Program.remotepassword = reader.readLine();
            Program.database = reader.readLine();
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static ResultSet ExecSqlDataReader(String strLenh, Object... objects) {
		ResultSet myReader = null;
		try {
			PreparedStatement p = Program.conn.prepareStatement(strLenh);
			for (int i = 1; i <= objects.length; i++) {
				p.setObject(i, objects[i - 1]);
			}
			myReader = p.executeQuery();
			return myReader;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static void ExecSqlDML(String sql, Object... objects) throws SQLException {

		PreparedStatement p = Program.conn.prepareStatement(sql);
		for (int i = 1; i <= objects.length; i++) {
			p.setObject(i, objects[i - 1]);
		}
		p.executeUpdate();

	}

	public static int ExecSqlNoQuery(String sql, Object... objects) throws SQLException {
		CallableStatement c = Program.conn.prepareCall(sql);

		c.registerOutParameter(1, java.sql.Types.INTEGER);
		for (int i = 2; i <= objects.length + 1; i++) {
			c.setObject(i, objects[i - 2]);
		}

		c.execute();
		int res = c.getInt(1);
		c.close();
		return res;
	}

	public static int ExecSqlNonQuery(String strlenh) {
		try {
			statement = conn.createStatement();
			statement.executeUpdate(strlenh);
			statement.close();
			return 0;
		} catch (SQLException ex) {
			if (ex.getMessage().contains("Error converting data type varchar to int"))
				JOptionPane.showMessageDialog(null,
						"Bạn format Cell lại cột \"Ngày Thi\" qua kiểu Number hoặc mở File Excel.");
			else
				JOptionPane.showMessageDialog(null, ex.getMessage());
			return ex.getErrorCode();
		}
	}

	private static List<String> getMaCn() {
		String sql = "SELECT MACN FROM ChiNhanh";
		List<String> macn = new ArrayList<String>();
		Program.myReader = Program.ExecSqlDataReader(sql);
		try {
			while (Program.myReader.next()) {
				macn.add(Program.myReader.getString(1).trim());
			}
			return macn;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, String> getServer() {
		Program.mlogin = Program.remotelogin;
		Program.password = Program.remotepassword;

		Program.Connect();
		Map<String, String> server = new LinkedHashMap<String, String>();
		try {
			String sql = "SELECT * FROM [dbo].[V_DS_PHANMANH] ";
			PreparedStatement statement = Program.conn.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				server.put(rs.getString("TENCN"), rs.getString("TENSERVER"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Program.macn = Program.getMaCn();
			try {
				Program.conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return server;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					login = new LoginForm();
					login.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}