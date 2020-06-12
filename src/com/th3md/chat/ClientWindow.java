package com.th3md.chat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class ClientWindow extends JFrame {


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private DatagramSocket socket;
	private InetAddress address;
	
	private Thread send;
	
	private JTextField txtMessage;
	
	private JTextArea txtrChat;
	
	private Client client;
	
	public ClientWindow(String name,String ip, int port) {
		client = new Client(name, ip, port);
		setTitle("Chat");
		if(!client.openConnect(ip)) {
			System.err.println("Error! Connection Faild");
			console("Connection Faild!");
		}
		createWindow();
		console("Attempting a connection to " + ip + ":" + port + ", User: " + name);
		client.send(("/c/" + name).getBytes());
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
		
		JScrollPane scroll = new JScrollPane(txtrChat);
		GridBagConstraints gbc_txtrChat = new GridBagConstraints();
		gbc_txtrChat.insets = new Insets(0, 0, 5, 5);
		gbc_txtrChat.fill = GridBagConstraints.BOTH;
		gbc_txtrChat.gridx = 0;
		gbc_txtrChat.gridy = 0;
		gbc_txtrChat.gridwidth = 3;
		gbc_txtrChat.gridheight = 2;
		gbc_txtrChat.insets = new Insets(0,5,0,0);
		contentPane.add(scroll, gbc_txtrChat);
		
		txtMessage = new JTextField();
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(txtMessage.getText());
				}
			}
		});
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.insets = new Insets(0, 0, 0, 5);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 0;
		gbc_txtMessage.gridy = 2;
		gbc_txtMessage.gridwidth = 2;
		contentPane.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);
		
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send(txtMessage.getText());
			}
		});
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
	
	private void send(String msg) {
		if(msg.isEmpty()) return;
		msg = client.getName() + ": " + msg;
		console(msg);
		msg = "/m/" + msg;
		client.send(msg.getBytes());
		txtrChat.setCaretPosition(txtrChat.getDocument().getLength());
		txtMessage.setText("");
	}
	
	public void console(String message) {
		txtrChat.append(message + "\n\r");
	}
}
