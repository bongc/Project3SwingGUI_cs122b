import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

public class QueryProcessor {
	public static DefaultListModel<String> getUserList(JPanel gui, Connection con) {
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
}
