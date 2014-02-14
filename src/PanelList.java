import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.sql.Connection;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

//Courtesy of Chris Bong
public class PanelList {

	public Connection con;
	DefaultListModel<String> l;
	public JList<String> jlist;
	JScrollPane listScroller = new JScrollPane();

	public PanelList(Connection con) {
		this.con = con;
		l = new DefaultListModel<String>();
	}

	public void premadeList(DefaultListModel<String> list, JPanel panel,
			MouseAdapter ma, int width, int height) {
		JList<String> jlist = new JList<String>(list);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jlist.setLayoutOrientation(JList.VERTICAL);
		if (ma != null) {
			jlist.addMouseListener(ma);
		}

		listScroller = new JScrollPane(jlist);
		listScroller.setPreferredSize(new Dimension(width, height));

		panel.add(listScroller);
	}

	public void clearList() {
	}

	public void addToList(String s) {
		l.addElement(s);
	}

	public void addToPanel(JPanel panel, MouseAdapter ma, int width, int height) {
		jlist = new JList<String>(l);
		jlist.setLayoutOrientation(JList.VERTICAL);
		if (ma != null) {
			jlist.addMouseListener(ma);
		}
		listScroller = new JScrollPane(jlist);
		listScroller.setPreferredSize(new Dimension(200, 120));
		panel.add(listScroller);
	}

	public JScrollPane getJList() {
		return listScroller;
	}

}
