import java.awt.Dimension;
import java.sql.Connection;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class PanelList {

	public JFrame gui;
	public Connection con;
	DefaultListModel<String> l;
	public JList<String> jlist;

	public PanelList(JFrame gui, Connection con) {
		this.gui = gui;
		this.con = con;
		l = new DefaultListModel<String>();
	}

	public void premadeList(DefaultListModel<String> list, JPanel panel) {
		JList<String> jlist = new JList<String>(list);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jlist.setLayoutOrientation(JList.VERTICAL_WRAP);

		JScrollPane listScroller = new JScrollPane(jlist);
		listScroller.setPreferredSize(new Dimension(300, 150));

		panel.add(listScroller);
	}

	public void addToList(String s) {
		l.addElement(s);
	}

	public void createJList(Boolean singleSelect) {
		jlist = new JList<String>(l);

		if (singleSelect) {
			jlist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		}
		jlist.setLayoutOrientation(JList.VERTICAL_WRAP);
	}

	public void addToPanel(JPanel panel) {

		JScrollPane listScroller = new JScrollPane(jlist);
		listScroller.setPreferredSize(new Dimension(300, 150));
		panel.add(listScroller);
	}
	
	public JList<String> getJList()
	{
		return jlist;
	}

}
