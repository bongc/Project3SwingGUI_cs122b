import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

public class QueryProcessor {
	public static DefaultListModel getUserList(JPanel gui, Connection con) {
		DefaultListModel users = new DefaultListModel();
		String query = "SELECT email FROM employees;";
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				users.addElement(rs.getString("email"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return users;
	}

	public static DefaultListModel getUserPrivList(String user, Connection con) {
		DefaultListModel users = new DefaultListModel();
		if (user.equals("")) {
			users.addElement("");
			return users;
		}
		String query = "SHOW GRANTS FOR '" + user + "'@'localhost'";
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				users.addElement(rs.getString("Grants for " + user + "@localhost"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return users;
	}

	public static boolean runRevokeFromGrant(String grant, Connection con) {
		String query = grant.replace("GRANT", "REVOKE").replace(" TO ", " FROM ");
		try {
			Statement statement = con.createStatement();
			int rs = statement.executeUpdate(query);
			return rs > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
