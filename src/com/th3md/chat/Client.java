package com.th3md.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client{
	
	
	private boolean running = false;
	private String name, ip;
	private int port;
	
	private int ID = -1;
	
	public String getName() {return name;}
	public String getIp() {return ip;}
	public int getPort() {return port;}
	public int getID() {return ID;}
	public void setID(int ID) {this.ID = ID;}
	
	private DatagramSocket socket;
	private InetAddress address;
	
	private Thread send;
	
	public Client(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
	}

	public String receive() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String message = new String(packet.getData());
		return message;
	}
	
	public void send(final byte[] data) {
		send = new Thread("SendChat") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
	
	public boolean openConnect(String ip) {
		try {
			socket = new DatagramSocket();
			address = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	

}
