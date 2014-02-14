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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainGUI {

	private static PanelList uPrivsPanel;
	private static JPanel uPrivs;
	private static Connection con;

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		con = DriverManager.getConnection("jdbc:mysql:///moviedb", "root", "");

		createGUI();
	}

	public static void createGUI() {
		JFrame gui = new JFrame("User Management Interface");
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		JPanel dbContainer = new JPanel();
		dbContainer.setLayout(new BoxLayout(dbContainer, BoxLayout.Y_AXIS));
		JPanel userContainer = new JPanel();
		userContainer.setLayout(new BoxLayout(userContainer, BoxLayout.X_AXIS));
		JPanel userPrivContainer = new JPanel();
		userPrivContainer.setLayout(new BoxLayout(userPrivContainer, BoxLayout.Y_AXIS));
		userPrivContainer.setBorder(BorderFactory.createTitledBorder("User's Privileges"));
		
		JPanel users = new JPanel();
		users.setBorder(BorderFactory.createTitledBorder("Users"));
		uPrivs = new JPanel();
		JPanel dbs = new JPanel();
		dbs.setBorder(BorderFactory.createTitledBorder("Databases"));
		JPanel tabs = new JPanel();
		tabs.setBorder(BorderFactory.createTitledBorder("Tables"));
		JPanel cols = new JPanel();
		cols.setBorder(BorderFactory.createTitledBorder("Columns"));
		JPanel privs = new JPanel();
		privs.setBorder(BorderFactory.createTitledBorder("Privileges"));

		final JPanel button = new JPanel();

		MouseAdapter ma = new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				int index = list.getSelectedIndex();
				if (index != -1) {
					System.out.println(list.getModel().getElementAt(index));
					uPrivs.removeAll();
					uPrivsPanel.premadeList(QueryProcessor.getUserPrivList("testuser", con), uPrivs, null, 550, 500);
					uPrivs.revalidate();
					uPrivs.repaint();
				}
			}
		};

		PanelList userPL = new PanelList(gui, con);
		userPL.premadeList(QueryProcessor.getUserList(users, con), users, ma, 200, 500);

		JButton revokeButton = new JButton("revoke");
		revokeButton.setPreferredSize(new Dimension(80, 20));
		revokeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JList jl = (JList) ((JScrollPane) uPrivs.getComponent(0)).getViewport().getComponents()[0];
				int index = jl.getSelectedIndex();
				if (index != -1) {
					String grant = jl.getModel().getElementAt(index).toString();
					// run sql query
					QueryProcessor.runRevokeFromGrant(grant, con);
				}

				uPrivs.removeAll();
				uPrivsPanel.premadeList(QueryProcessor.getUserPrivList("testuser", con), uPrivs, null, 550, 500);
				uPrivs.revalidate();
				uPrivs.repaint();
			}
		});
		
		uPrivsPanel = new PanelList(gui, con);
		uPrivsPanel.premadeList(QueryProcessor.getUserPrivList("", con), uPrivs, null, 550, 500);

		userPrivContainer.add(uPrivs);
		userPrivContainer.add(revokeButton);
		
		// lm.createList(QueryProcessor.getUserList(users, con), resources);
		//
		// lm.createList(QueryProcessor.getUserList(users, con), privileges);

		JButton updateButton = new JButton("Update");
		updateButton.setActionCommand("update");
		updateButton.setPreferredSize(new Dimension(80, 20));
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// update database;
			}
		});

		userContainer.add(users);

		dbContainer.add(dbs);
		dbContainer.add(tabs);
		dbContainer.add(cols);

		container.add(userContainer);
		container.add(userPrivContainer);
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