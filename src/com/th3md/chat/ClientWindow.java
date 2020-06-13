package com.th3md.chat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ClientWindow extends JFrame implements Runnable{


	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private DatagramSocket socket;
	private InetAddress address;
	
	private Thread send;
	
	private JTextField txtMessage;
	
	private JTextArea txtrChat;
	
	private Client client;
	
	private int ID = -1;
	
	private boolean running = false;
	private Thread listen, run;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmOnlineUsers;
	private JMenuItem mntmExit;
	
	private OnlineUsers users;
	
	
	public ClientWindow(String name,String ip, int port) {
		client = new Client(name, ip, port);
		setTitle("Chat");
		if(!client.openConnect(ip)) {
			System.err.println("Error! Connection Faild");
			console("Connection Faild!");
		}
		createWindow();
		console("Attempting a connection to " + ip + ":" + port + ", User: " + name);
		client.send(("/c/" + name + "/e/").getBytes());
		
		users = new OnlineUsers();
		running = true;
		run = new Thread(this,"ClientWindow");
		run.start();
	}
	
	
	
	private void createWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(880,550);
		setLocationRelativeTo(null);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmOnlineUsers = new JMenuItem("Online Users");
		mntmOnlineUsers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				users.setVisible(true);
			}
		});
		mnFile.add(mntmOnlineUsers);
		
		mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{28,815,30, 7};
		gbl_contentPane.rowHeights = new int[]{35,475, 40};
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
		gbc_txtrChat.weightx = 1;
		gbc_txtrChat.weighty = 1;
		gbc_txtrChat.insets = new Insets(0,5,0,0);
		contentPane.add(scroll, gbc_txtrChat);
		
		txtMessage = new JTextField();
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(txtMessage.getText(),true);
				}
			}
		});
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.insets = new Insets(0, 0, 0, 5);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 0;
		gbc_txtMessage.gridy = 2;
		gbc_txtMessage.gridwidth = 2;
		gbc_txtMessage.weightx = 1;
		gbc_txtMessage.weighty = 0;
		contentPane.add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);
		
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				send(txtMessage.getText(),true);
			}
		});
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.insets = new Insets(0, 0, 0, 5);
		gbc_btnSend.gridx = 2;
		gbc_btnSend.gridy = 2;
		gbc_btnSend.weightx = 0;
		gbc_txtrChat.weighty = 0;
		contentPane.add(btnSend, gbc_btnSend);
		
		txtMessage.requestFocusInWindow();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				send("/d/" + client.getID() + "/e/",false);
				running = false;
				client.close();
			}
		});
		
		try {
			setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void send(String msg,boolean text) {
		if(msg.isEmpty()) return;
		//console(msg);
		if(text) {
			msg = client.getName() + ": " + msg;
			msg = "/m/" + msg + "/e/";
			txtMessage.setText("");
		}		
		client.send(msg.getBytes());
		txtrChat.setCaretPosition(txtrChat.getDocument().getLength());
	}
	
	
	private void console(String message) {
		txtrChat.append(message + "\n\r");
	}
	
	private void listen() {
		listen = new Thread("Listen") {
			@Override
			public void run() {
				while(running) {
					String message = client.receive();
					if(message.startsWith("/c/")) {
						client.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
						console("Connected, ID : "+client.getID());
					}else if(message.startsWith("/m/")) {
						String text = message.substring(3).split("/e/")[0];
						console(text);
					}else if(message.startsWith("/i/")) {
						send("/i/"+ client.getID() +"/e/",false);
					}else if(message.startsWith("/u/")) {
						String[] newUsers = message.split("/u/|/n/|/e/");
						users.update(Arrays.copyOfRange(newUsers, 1, newUsers.length - 1));
					}
					try {
						Thread.sleep(100L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		listen.start();
	}
	
	
	@Override
	public void run() {
		listen();
	}
}
