package com.th3md.chat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.JTextField;

public class Client extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private String name, ip;
	private int port;
	private JTextField txtMessage;
	
	private JTextArea txtrChat;

	public Client(String name,String ip, int port) {
		setTitle("Chat");
		this.name = name;
		this.ip = ip;
		this.port = port;
		createWindow();
	}
	
	private void createWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(880,550);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{28,815,30, 7};
		gbl_contentPane.rowHeights = new int[]{35,475, 40};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		txtrChat = new JTextArea();
		txtrChat.setEditable(false);
		GridBagConstraints gbc_txtrChat = new GridBagConstraints();
		gbc_txtrChat.insets = new Insets(0, 0, 5, 5);
		gbc_txtrChat.fill = GridBagConstraints.BOTH;
		gbc_txtrChat.gridx = 1;
		gbc_txtrChat.gridy = 1;
		gbc_txtrChat.gridwidth = 2;
		gbc_txtrChat.insets = new Insets(0,5,0,0);
		contentPane.add(txtrChat, gbc_txtrChat);
		
		txtMessage = new JTextField();
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.insets = new Insets(0, 0, 0, 5);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 1;
		gbc_txtMessage.gridy = 2;
		contentPane.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);
		
		
		JButton btnSend = new JButton("Send");
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		contentPane.add(btnSend, gbc_btnSend);
		
		txtMessage.requestFocusInWindow();
		
		
		try {
			setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void console(String message) {
		txtrChat.append(message + "\n\r");
	}

}
