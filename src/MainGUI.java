import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainGUI {

	private static JPanel tabs;
	private static JPanel privs;
	private static PanelList tablePL;
	private static JPanel cols;
	private static PanelList columnPL;
	private static MouseAdapter tableRender;
	private static MouseAdapter columnRender;
	private static MouseAdapter columnSelect;
	private static MouseAdapter userSelect;
	private static MouseAdapter privilegeSelect;
	private static String privilege;
	private static String user;
	private static String database;
	private static String table;
	private static String column;
	private static PanelList privilegePL;
	private static PanelList uPrivsPanel;
	private static JPanel uPrivs;
	private static Connection con;
	private static JPanel users;

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = DriverManager.getConnection("jdbc:mysql:///moviedb", "root", "1234");

		createGUI();
	}

	public static void createGUI() {
		JFrame gui = new JFrame("User Management Interface");
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setPreferredSize(new Dimension(1024, 700));

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		JPanel dbContainer = new JPanel();
		dbContainer.setLayout(new BoxLayout(dbContainer, BoxLayout.Y_AXIS));
		JPanel userContainer = new JPanel();
		userContainer.setLayout(new BoxLayout(userContainer, BoxLayout.X_AXIS));
		JPanel userPrivContainer = new JPanel();
		userPrivContainer.setLayout(new BoxLayout(userPrivContainer, BoxLayout.Y_AXIS));
		userPrivContainer.setBorder(BorderFactory.createTitledBorder("User's Privileges"));

		 users = new JPanel();
		users.setBorder(BorderFactory.createTitledBorder("Users"));
		uPrivs = new JPanel();
		JPanel dbs = new JPanel();
		dbs.setBorder(BorderFactory.createTitledBorder("Databases"));
		tabs = new JPanel();
		tabs.setBorder(BorderFactory.createTitledBorder("Tables"));
		cols = new JPanel();
		cols.setBorder(BorderFactory.createTitledBorder("Columns"));
		privs = new JPanel();
		privs.setBorder(BorderFactory.createTitledBorder("Privileges"));

		JPanel button = new JPanel();

		PanelList userPL = new PanelList(con);
		PanelList dbPL = new PanelList(con);
		tablePL = new PanelList(con);
		columnPL = new PanelList(con);
		privilegePL = new PanelList(con);

		userSelect = new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<String> list = (JList<String>) evt.getSource();
				updateUserPrivList(list);
			}
		};

		tableRender = new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<String> list = (JList<String>) evt.getSource();
				int index = list.getSelectedIndex();
				database = list.getModel().getElementAt(index);
				tabs.removeAll();
				tablePL.premadeList(QueryProcessor.getTables(con, database), tabs, columnRender, 200, 120);
				tabs.revalidate();
				tabs.repaint();
			}
		};

		columnRender = new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<String> list = (JList<String>) evt.getSource();
				int index = list.getSelectedIndex();
				table = list.getModel().getElementAt(index);
				cols.removeAll();
				columnPL.premadeList(QueryProcessor.getColumns(con, table, database), cols, columnSelect, 200, 120);
				cols.revalidate();
				cols.repaint();
			}
		};
		columnSelect = new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<String> list = (JList<String>) evt.getSource();
				int index = list.getSelectedIndex();
				column = list.getModel().getElementAt(index);

			}
		};
		privilegeSelect = new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<String> list = (JList<String>) evt.getSource();
				int index = list.getSelectedIndex();
				privilege = list.getModel().getElementAt(index);
			}
		};

		JButton revokeButton = new JButton("revoke");
		revokeButton.setPreferredSize(new Dimension(80, 20));
		revokeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// remove grant
				JList<String> jl = (JList<String>) ((JScrollPane) uPrivs.getComponent(0)).getViewport().getComponents()[0];
				int index = jl.getSelectedIndex();
				if (index != -1) {
					String grant = jl.getModel().getElementAt(index).toString();
					// run sql query
					QueryProcessor.runRevokeFromGrant(grant, con);
				}

				updateUserPrivList(null);
			}
		});

		uPrivsPanel = new PanelList(con);
		uPrivsPanel.premadeList(QueryProcessor.getUserPrivList("", con), uPrivs, null, 350, 500);

		userPrivContainer.add(uPrivs);
		userPrivContainer.add(revokeButton);

		userPL.premadeList(QueryProcessor.getUserList(users, con), users, userSelect, 200, 500);
		dbPL.premadeList(QueryProcessor.getDatabases(dbs, con), dbs, tableRender, 200, 120);
		tablePL.premadeList(new DefaultListModel<String>(), tabs, null, 200, 120);
		columnPL.premadeList(new DefaultListModel<String>(), cols, null, 200, 120);
		privilegePL.addToList("ALL");
		privilegePL.addToList("SELECT");
		privilegePL.addToList("INSERT");
		privilegePL.addToList("DELETE");
		privilegePL.addToPanel(privs, privilegeSelect, 200, 120);

		// lm.createList(QueryProcessor.getUserList(users, con), resources);
		//
		// lm.createList(QueryProcessor.getUserList(users, con), privileges);

		JButton updateButton = new JButton("Update");
		updateButton.setPreferredSize(new Dimension(80, 20));
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// update database;
				QueryProcessor.givePrivilege(user, database, table, column, privilege, con);
				// update user's privilege list...
				updateUserPrivList(null);
			}

		});

		userContainer.add(users);

		userPrivContainer.add(uPrivs);

		dbContainer.add(dbs);
		dbContainer.add(tabs);
		dbContainer.add(cols);
		dbContainer.add(privs);

		container.add(userContainer);
		container.add(userPrivContainer);
		container.add(dbContainer);
		button.add(updateButton);

		createMenuBar(gui);
		gui.getContentPane().add(container);
		gui.getContentPane().add(button, BorderLayout.PAGE_END);
		gui.pack();
		gui.setVisible(true);

	}

	private static void updateUserPrivList(JList<String> jlist) {
		JList<String> jl = null;
		if (jlist != null) {
			jl = jlist;
		} else {
			try {
				jl = (JList<String>) ((JScrollPane) users.getComponent(0)).getViewport().getComponents()[0];
			} catch (Exception e) {
				return;
			}
		}
		int index = jl.getSelectedIndex();
		uPrivs.removeAll();
		if (index != -1) {
			user = jl.getModel().getElementAt(index);
			uPrivsPanel.premadeList(QueryProcessor.getUserPrivList(user, con), uPrivs,
					null, 350, 300);
			uPrivs.revalidate();
			uPrivs.repaint();
		}
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