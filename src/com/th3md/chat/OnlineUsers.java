package com.th3md.chat;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JList;
import java.awt.GridBagConstraints;

public class OnlineUsers extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JList<String> list;

	public OnlineUsers() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(200,320);
		setLocationRelativeTo(null);
		setTitle("Online Users");
		setType(Type.UTILITY);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		list = new JList<String>();
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 0;
		
		JScrollPane scroll = new JScrollPane(); 
		scroll.setViewportView(list);
		contentPane.add(scroll, gbc_list);
	}
	
	public void update(String[] users) {
		list.setListData(users);
	}

}
