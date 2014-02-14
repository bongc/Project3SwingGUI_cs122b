import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.sql.Connection;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

//Courtesy of Chris Bong
public class PanelList {

	public JFrame gui;
	public Connection con;
	DefaultListModel l;
	public JList jlist;

	public PanelList(JFrame gui, Connection con) {
		this.gui = gui;
		this.con = con;
		l = new DefaultListModel();
	}

	public void premadeList(DefaultListModel list, JPanel panel, MouseAdapter ma, int width, int height) {
		JList jlist = new JList(list);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jlist.setLayoutOrientation(JList.VERTICAL_WRAP);
		if(ma != null){
			jlist.addMouseListener(ma);
		}

		JScrollPane listScroller = new JScrollPane(jlist);
		listScroller.setPreferredSize(new Dimension(width, height));

		panel.add(listScroller);
	}

	public void addToList(String s) {
		l.addElement(s);
	}

	public void createJList(Boolean singleSelect) {
		jlist = new JList(l);

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
	
	public JList getJList()
	{
		return jlist;
	}

}
