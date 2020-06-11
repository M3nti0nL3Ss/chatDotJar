package com.th3md.chat;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtName;
	private JLabel lblIpAdress;
	private JTextField txtIp;
	private JLabel lblPort;
	private JTextField txtI;
	

	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300,380);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.setHorizontalAlignment(SwingConstants.LEFT);
		txtName.setBounds(68, 65, 163, 19);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(130, 46, 40, 15);
		contentPane.add(lblName);
		
		lblIpAdress = new JLabel("IP Adress");
		lblIpAdress.setBounds(115, 117, 70, 15);
		contentPane.add(lblIpAdress);
		
		txtIp = new JTextField();
		txtIp.setBounds(68, 144, 163, 19);
		contentPane.add(txtIp);
		txtIp.setColumns(10);
		
		lblPort = new JLabel("Port");
		lblPort.setBounds(135, 195, 30, 15);
		contentPane.add(lblPort);
		
		txtI = new JTextField();
		txtI.setColumns(10);
		txtI.setBounds(68, 222, 163, 19);
		contentPane.add(txtI);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				login(txtName.getText(),txtIp.getText(),Integer.parseInt(txtI.getText()));
			}
		});
		btnLogin.setBounds(91, 302, 117, 25);
		contentPane.add(btnLogin);
	}
	
	private void login(String name,String ip, int port) {
		dispose();
		new Client(name,ip,port);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
