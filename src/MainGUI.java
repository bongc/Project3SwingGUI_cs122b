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
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
	private static JFrame input;
	private static JPanel users;

	private static PanelList userPL;
	private static JTextArea username;
	private static JTextArea password;
	private static JFrame gui;

	private static JPanel procDbs;
	private static JPanel procs;
	private static JButton updateProcsButton;
	private static String selectedProc;

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = DriverManager.getConnection("jdbc:mysql:///moviedb", "root",
				"132435");

		createGUI();
	}

	public static void createGUI() {
		gui = new JFrame("User Management Interface");
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gui.setPreferredSize(new Dimension(1200, 700));

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		JPanel dbContainer = new JPanel();
		dbContainer.setLayout(new BoxLayout(dbContainer, BoxLayout.Y_AXIS));
		JPanel userContainer = new JPanel();
		userContainer.setLayout(new BoxLayout(userContainer, BoxLayout.X_AXIS));
		JPanel userPrivContainer = new JPanel();

		userPrivContainer.setLayout(new BoxLayout(userPrivContainer,
				BoxLayout.Y_AXIS));
		userPrivContainer.setBorder(BorderFactory
				.createTitledBorder("User's Privileges"));

		JPanel procContainer = new JPanel();
		procContainer.setLayout(new BoxLayout(procContainer, BoxLayout.Y_AXIS));

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
		procDbs = new JPanel();
		procDbs.setBorder(BorderFactory.createTitledBorder("Databases"));
		procs = new JPanel();
		procs.setBorder(BorderFactory.createTitledBorder("Procedures"));
		procs.setLayout(new BoxLayout(procs, BoxLayout.Y_AXIS));

		JPanel button = new JPanel();

		userPL = new PanelList(con);
		PanelList dbPL = new PanelList(con);
		tablePL = new PanelList(con);
		columnPL = new PanelList(con);
		privilegePL = new PanelList(con);

		userSelect = new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<String> list = (JList<String>) evt.getSource();

				int index = list.getSelectedIndex();
				user = list.getModel().getElementAt(index);
				if (index != -1) {
					uPrivs.removeAll();
					uPrivsPanel.premadeList(
							QueryProcessor.getUserPrivList("testuser", con),
							uPrivs, null, 350, 300);
					uPrivs.revalidate();
					uPrivs.repaint();
				}

				updateUserPrivList(list);
			}
		};

		tableRender = new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<String> list = (JList<String>) evt.getSource();
				int index = list.getSelectedIndex();
				database = list.getModel().getElementAt(index);
				tabs.removeAll();
				tablePL.premadeList(QueryProcessor.getTables(con, database),
						tabs, columnRender, 200, 120);
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
				columnPL.premadeList(
						QueryProcessor.getColumns(con, table, database), cols,
						columnSelect, 200, 120);
				cols.revalidate();
				cols.repaint();
			}
		};
		columnSelect = new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<String> list = (JList<String>) evt.getSource();
				int index = list.getSelectedIndex();
				column = list.getModel().getElementAt(index);

				privs.removeAll();
				PanelList pan = new PanelList(con);
				pan.addToList("SELECT");
				pan.addToPanel(privs, privilegeSelect, 200, 120);
				privs.revalidate();
				privs.repaint();

			}
		};
		privilegeSelect = new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<String> list = (JList<String>) evt.getSource();
				int index = list.getSelectedIndex();
				privilege = list.getModel().getElementAt(index);
			}
		};

		final MouseAdapter procListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList<String> list = (JList<String>) evt.getSource();
				int index = list.getSelectedIndex();
				selectedProc = list.getModel().getElementAt(index);
			}
		};

		MouseAdapter showProcListener = new MouseAdapter() {
			private String procDatabase;

			public void mouseClicked(MouseEvent evt) {
				JList<String> list = (JList<String>) evt.getSource();
				int index = list.getSelectedIndex();
				procDatabase = list.getModel().getElementAt(index);
				procs.removeAll();
				tablePL.premadeList(QueryProcessor.getProcs(con, procDatabase),
						procs, procListener, 200, 240);
				procs.add(updateProcsButton);
				procs.revalidate();
				procs.repaint();
			}
		};

		JButton revokeButton = new JButton("revoke");
		revokeButton.setPreferredSize(new Dimension(80, 20));
		revokeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// remove grant
				JList<String> jl = (JList<String>) ((JScrollPane) uPrivs
						.getComponent(0)).getViewport().getComponents()[0];
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
		uPrivsPanel.premadeList(QueryProcessor.getUserPrivList("", con),
				uPrivs, null, 350, 500);

		userPrivContainer.add(uPrivs);
		userPrivContainer.add(revokeButton);

		userPL.premadeList(QueryProcessor.getUserList(users, con), users,
				userSelect, 200, 500);
		dbPL.premadeList(QueryProcessor.getDatabases(dbs, con), dbs,
				tableRender, 200, 120);
		tablePL.premadeList(new DefaultListModel<String>(), tabs, null, 200,
				120);
		columnPL.premadeList(new DefaultListModel<String>(), cols, null, 200,
				120);
		// lol too last to create new PL
		columnPL.premadeList(QueryProcessor.getDatabases(dbs, con), procDbs,
				showProcListener, 200, 240);
		columnPL.premadeList(new DefaultListModel<String>(), procs,
				procListener, 200, 240);
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
				QueryProcessor.givePrivilege(user, database, table, column,
						privilege, con);
				// update user's privilege list...
				updateUserPrivList(null);
			}

		});
		updateProcsButton = new JButton("Grant Procedure Access");
		updateProcsButton.setPreferredSize(new Dimension(160, 20));
		updateProcsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// update database;
				QueryProcessor.giveProcedurePrivilege(user, selectedProc, con);
				// update user's privilege list...
				updateUserPrivList(null);
			}

		});

		JButton clearButton = new JButton("Clear");
		clearButton.setPreferredSize(new Dimension(80, 20));
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// update database;
				tabs.removeAll();
				cols.removeAll();
				privs.removeAll();
				privs.add(privilegePL.getJList());
				tablePL.premadeList(new DefaultListModel<String>(), tabs, null,
						200, 120);
				columnPL.premadeList(new DefaultListModel<String>(), cols,
						null, 200, 120);
				table = null;
				column = null;
				privilege = null;
				tabs.revalidate();
				cols.revalidate();
				privs.revalidate();
				tabs.repaint();
				cols.repaint();
				privs.repaint();
			}
		});

		userContainer.add(users);

		userPrivContainer.add(uPrivs);

		dbContainer.add(dbs);
		dbContainer.add(tabs);
		dbContainer.add(cols);
		dbContainer.add(privs);

		procContainer.add(procDbs);
		procs.add(updateProcsButton);
		procContainer.add(procs);

		container.add(userContainer);
		container.add(userPrivContainer);
		container.add(dbContainer);

		button.add(clearButton);
		container.add(procContainer);
		button.add(updateButton);

		createMenuBar();
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
				jl = (JList<String>) ((JScrollPane) users.getComponent(0))
						.getViewport().getComponents()[0];
			} catch (Exception e) {
				return;
			}
		}
		int index = jl.getSelectedIndex();
		uPrivs.removeAll();
		if (index != -1) {
			user = jl.getModel().getElementAt(index);
			uPrivsPanel.premadeList(QueryProcessor.getUserPrivList(user, con),
					uPrivs, null, 350, 300);
			uPrivs.revalidate();
			uPrivs.repaint();
		}
	}

	private static void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(190, 190, 190));
		menuBar.setPreferredSize(new Dimension(400, 20));

		JMenu menu = new JMenu("File");

		JMenuItem item1 = new JMenuItem("Add User");
		JMenuItem item2 = new JMenuItem("Exit");

		input = new JFrame("Create User");
		username = new JTextArea(1, 16);
		username.setPreferredSize(new Dimension(300, 30));

		password = new JTextArea(1, 16);
		password.setPreferredSize(new Dimension(300, 30));

		JPanel userFields = new JPanel();
		JPanel userPanel = new JPanel();
		userPanel.add(username);
		JPanel passPanel = new JPanel();
		passPanel.add(password);
		JButton create = new JButton("Create");
		create.setPreferredSize(new Dimension(80, 20));
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				QueryProcessor.createUser(username.getText().trim(), password
						.getText().trim(), con);
				username.setText("");
				password.setText("");
				input.dispose();
				gui.dispose();
				refresh();

			}
		});
		userPanel.setBorder(BorderFactory.createTitledBorder("Username: "));
		passPanel.setBorder(BorderFactory.createTitledBorder("Password: "));

		userFields.add(userPanel);
		userFields.add(passPanel);
		userFields.add(create);

		userFields.setLayout(new BoxLayout(userFields, BoxLayout.X_AXIS));

		input.add(userFields);
		input.pack();

		item1.setToolTipText("Create a new user.");
		item1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				input.setVisible(true);
			}
		});

		item2.setToolTipText("Close Program");
		item2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});

		menu.add(item1);
		menu.add(item2);
		menuBar.add(menu);

		gui.setJMenuBar(menuBar);
	}

	public static void refresh() {
		createGUI();
	}
}