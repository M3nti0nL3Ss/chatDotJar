package com.th3md.chat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server implements Runnable{

	private List<ServerClient> clients = new ArrayList<ServerClient>();
	private List<Integer> responses = new ArrayList<Integer>();
	private int port;
	private DatagramSocket socket;
	private boolean running = false;
	private Thread run,client,receive,send;
	private final int MAX_ATTEMPS = 5;
	private boolean raw = false;
	
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
		Scanner scanner = new Scanner(System.in);
		while(running) {
			String text = scanner.nextLine();
			if(!text.startsWith("/") && !text.isEmpty()) {
				distribute("/m/Server: " + text + "/e/");
				continue;
			}
			text = text.substring(1);
			if(text.equals("raw")) {
				raw = !raw;
				System.out.println("Raw mode set to " + raw);
			}
			else if(text.equals("clients")) {
				System.out.println("Clients:\n===========");
				for(ServerClient c : clients) {
					System.out.println(c.name + "(" + c.getID() + ") "+ c.ip + ":"+ c.port);
				}
				System.out.println("===========");
			}
			else if(text.startsWith("kick")) {
				String name = text.split(" ")[1];
				int id;
				try {
					id = Integer.parseInt(name);
				}catch(NumberFormatException e) {
					id = -1;
				}
				if(id>=0) {
					boolean check = false;
					for(ServerClient c: clients) {
						if(c.getID() == id) {
							disconnect(id,true);
							check = true;
							break;
						}
					}
					if(!check) System.out.println("Client " + id + " Doesn't exist. Check ID number!");
				}else {
					boolean check = false;
					for(ServerClient c: clients) {
						if(c.name.equals(name)) {
							disconnect(c.getID(),true);
							check = true;
							break;
						}
					}
					if(!check) System.out.println("Client " + name + " Doesn't exist. Check Name!");
				}
			}
			else if(text.equals("quit")) quit();
			else if(text.equals("help")) help();
			else {
				System.out.println("Unknown command.");
				help();
			}
		}
		scanner.close();
	}
	
	private void clients() {
		client = new Thread("Client") {
			@Override
			public void run() {
				while(running) {
					distribute("/i/server");
					sendWhosOnline();
					try {
						Thread.sleep(2000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(int i=0;i<clients.size();i++) {
						ServerClient client = clients.get(i);
						if(!responses.contains(client.getID())) {
							if(client.attempt >= MAX_ATTEMPS) {
								disconnect(client.getID(), false);
							}else {
								client.attempt++;
							}
						}else {
							responses.remove(new Integer(client.getID()));
							client.attempt =0;
						}
					}
				}
			}
		};
		client.start();
	}
	
	private void help() {
		System.out.println("List of commands:");
		System.out.println("=================");
		System.out.println("/raw - enables/disables raw mode.");
		System.out.println("/clients - shows all connected clients.");
		System.out.println("/kick [users ID or username] - kicks a user.");
		System.out.println("/help - shows this help message.");
		System.out.println("/quit - shuts down the server.");
		System.out.println("=================");
	}
	
	private void sendWhosOnline() {
		if(clients.size() <= 0) return;
		String users = "/u/";
		for(ServerClient c: clients) {
			users += c.name + "/n/";
		}
		users = users.substring(0,users.length()-2)+"e/";
		distribute(users);
	}
	
	private void receive() {
		receive = new Thread("Receive") {
			@Override
			public void run() {
				while(running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);
					try {
						socket.receive(packet);
					} catch (SocketException e) {
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
			String name = rPacketString.split("/c/|/e/")[1];
			System.out.println(name + "(" + id + ") Connected");
			clients.add(
					new ServerClient(
							name,
							rPacket.getAddress(),
							rPacket.getPort(),
							id
					)
			);
			send(("/c/"+id+"/e/").getBytes(), rPacket.getAddress(), rPacket.getPort());
		}else if(rPacketString.startsWith("/m/")){
			//System.out.println(rPacketString); // just for debug
			distribute(rPacketString);
		}else if(rPacketString.startsWith("/d/")){
			int id = Integer.parseInt(rPacketString.split("/d/|/e/")[1]);
			disconnect(id,true);
		}else if(rPacketString.startsWith("/i/")){
			responses.add(Integer.parseInt(rPacketString.split("/i/|/e/")[1]));
		}else{
			System.out.println(rPacketString);
		}
	}
	
	private void quit() {
		for(ServerClient c: clients) {
			disconnect(c.getID(), true);
		}
		running = false;
		socket.close();
	}
	
	private void disconnect(int id, boolean status) {
		ServerClient client=null;
		for(int i = 0;i<clients.size();i++) 
			if(clients.get(i).ID == id) {
				client = clients.get(i);
				clients.remove(i);
				break;
			}
		if(client == null) return;
		String message = "Client "+ client.name + "("+ client.getID() + ") @ "
				+ client.ip.toString()
				+ ":"
				+ client.port
				+ ((status)?" Disconnected.":" Time out.");
		System.out.println(message);
	}
	
	private void distribute(String message) {
		if(message.startsWith("/m/")) {
			String text = message.substring(3).split("/e/")[0];
			System.out.println(text);
		}
		if(raw) System.out.println(message);
		for(int i=0;i<clients.size();i++) {
			ServerClient client = clients.get(i);
			send(message.getBytes(), client.ip,client.port);
		}
	}
	
	private void send(final byte[] data, final InetAddress ip, final int port) {
		send = new Thread("Send") {
			@Override
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
