package com.th3md.chat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{

	private List<ServerClient> clients = new ArrayList<ServerClient>();
	private int port;
	private DatagramSocket socket;
	private boolean running = false;
	private Thread run,client,receive,send;
	
	public Server(int port) {
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
		run = new Thread(this,"Server");
		run.start();
	}

	@Override
	public void run() {
		running = true;
		System.out.println("Server Started on port " + port);
		clients();
		receive();
	}
	
	private void clients() {
		client = new Thread("Client") {
			public void run() {
				while(running) {
					try {
						Thread.sleep(100L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		client.start();
	}
	
	private void receive() {
		receive = new Thread("Receive") {
			public void run() {
				while(running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
					process(packet);
					try {
						Thread.sleep(100L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		receive.start();
	}
	
	private void process(DatagramPacket rPacket) {
		String rPacketString = new String(rPacket.getData());
		if(rPacketString.startsWith("/c/")) {
			//UUID id = UUID.randomUUID();
			int id = Random.getRandom();
			System.out.println("ID :" + id);
			clients.add(
					new ServerClient(
							rPacketString.substring(3,rPacketString.length()),
							rPacket.getAddress(),
							rPacket.getPort(),
							id
					)
			);
			System.out.println(rPacketString.substring(3,rPacketString.length()));
			send(("/c/"+id+"/e/").getBytes(), rPacket.getAddress(), rPacket.getPort());
		}else if(rPacketString.startsWith("/m/")){
			//System.out.println(rPacketString); // just for debug
			distribute(rPacketString);
		}else{
			System.out.println(rPacketString);
		}
	}
	
	private void distribute(String message) {
		for(int i=0;i<clients.size();i++) {
			ServerClient client = clients.get(i);
			send(message.getBytes(), client.ip,client.port);
		}
	}
	
	private void send(final byte[] data, final InetAddress ip, final int port) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
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
	
}
