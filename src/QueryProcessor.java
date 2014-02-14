import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class QueryProcessor {
	public static DefaultListModel<String> getUserList(JPanel gui,
			Connection con) {
		DefaultListModel<String> users = new DefaultListModel<String>();

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

	public static DefaultListModel<String> getDatabases(JPanel gui,
			Connection con) {
		DefaultListModel<String> dbs = new DefaultListModel<String>();
		String query = "SHOW DATABASES;";
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				dbs.addElement(rs.getString("Database"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbs;
	}

	public static DefaultListModel<String> getTables(Connection con, String db) {
		DefaultListModel<String> tabs = new DefaultListModel<String>();
		String query = "SHOW TABLES FROM " + db;
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				tabs.addElement(rs.getString("Tables_in_" + db));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tabs;
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
				users.addElement(rs.getString("Grants for " + user
						+ "@localhost"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return users;
	}

	public static boolean runRevokeFromGrant(String grant, Connection con) {
		String query = grant.replace("GRANT", "REVOKE").replace(" TO ",
				" FROM ");
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

	public static DefaultListModel<String> getColumns(Connection con,
			String table, String db) {
		DefaultListModel<String> tabs = new DefaultListModel<String>();
		String query = "SHOW COLUMNS FROM " + db + "." + table;
		try {
			Statement statement = con.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				tabs.addElement(rs.getString("Field"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tabs;
	}

	public static void givePrivilege(String user, String database,
			String table, String column, String privilege, Connection con) {
		String query;
		if (user == null) {
			JOptionPane.showMessageDialog(null, "Please Select a User.");
			return;
		}
		if (privilege == null) {
			JOptionPane.showMessageDialog(null, "Please Select a Privilege.");
			return;
		}
		if (database == null) {
			JOptionPane.showMessageDialog(null, "Please Select a Database.");
			return;
		}

		if (database != null && table == null) {
			query = "GRANT " + privilege + " ON " + database + ".* TO " + "'"
					+ user + "'@'localhost';";
		} else if (database != null && table != null && column == null) {
			query = "GRANT " + privilege + " ON " + database + "." + table
					+ " TO " + "'" + user + "'@'localhost';";
		} else {
			query = "GRANT (" + privilege + ") " + column + " ON " + database
					+ "." + table + " TO " + "'" + user + "'@'localhost';";
		}
		if (query != null) {
			try {
				Statement stmt = con.createStatement();
				stmt.executeQuery(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		JOptionPane.showMessageDialog(null, "Privileges Updated!");
		return;
	}
}
