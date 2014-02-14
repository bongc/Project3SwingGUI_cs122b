import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MainGUI {

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = DriverManager.getConnection("jdbc:mysql:///moviedb",
				"root", "132435");

		createGUI(con);
	}

	public static void createGUI(Connection con) {
		JFrame gui = new JFrame("User Management Interface");
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		JPanel dbContainer = new JPanel();
		dbContainer.setLayout(new BoxLayout(dbContainer, BoxLayout.Y_AXIS));
		JPanel userContainer = new JPanel();
		userContainer.setLayout(new BoxLayout(userContainer, BoxLayout.X_AXIS));

		JPanel users = new JPanel();
		users.setBorder(BorderFactory.createTitledBorder("Users"));
		JPanel uPrivs = new JPanel();
		uPrivs.setBorder(BorderFactory.createTitledBorder("User's Privileges"));
		JPanel dbs = new JPanel();
		dbs.setBorder(BorderFactory.createTitledBorder("Databases"));
		JPanel tabs = new JPanel();
		tabs.setBorder(BorderFactory.createTitledBorder("Tables"));
		JPanel cols = new JPanel();
		cols.setBorder(BorderFactory.createTitledBorder("Columns"));
		JPanel privs = new JPanel();
		privs.setBorder(BorderFactory.createTitledBorder("Privileges"));


		final JPanel button = new JPanel();

		PanelList userPL = new PanelList(gui, con);
		userPL.premadeList(QueryProcessor.getUserList(users, con), users);
		
		
		// lm.createList(QueryProcessor.getUserList(users, con), resources);
		//
		// lm.createList(QueryProcessor.getUserList(users, con), privileges);

		JButton updateButton = new JButton("Update");
		updateButton.setActionCommand("update");
		updateButton.setPreferredSize(new Dimension(80, 20));
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// update database;
				System.exit(0);
			}
		});
		
		userContainer.add(users);
		userContainer.add(uPrivs);
		
		dbContainer.add(dbs);
		dbContainer.add(tabs);
		dbContainer.add(cols);
		
		container.add(userContainer);
		container.add(dbContainer);
		container.add(privs);
		button.add(updateButton);

		createMenuBar(gui);
		gui.getContentPane().add(container);
		gui.getContentPane().add(button, BorderLayout.PAGE_END);
		gui.pack();
		gui.setVisible(true);
	}

	private static void createMenuBar(JFrame gui) {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(190, 190, 190));
		menuBar.setPreferredSize(new Dimension(400, 20));

		JMenu menu = new JMenu("File");

		JMenuItem item1 = new JMenuItem("Exit");
		item1.setToolTipText("Close Program");
		item1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		menu.add(item1);
		menuBar.add(menu);

		gui.setJMenuBar(menuBar);
	}

}