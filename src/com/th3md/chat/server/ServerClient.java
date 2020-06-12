package com.th3md.chat.server;

import java.net.InetAddress;

public class ServerClient {
	public String name;
	public InetAddress ip;
	public int port;
	public int ID = 0;
	public int attempt =0;
	
	public ServerClient(String name, InetAddress ip, int port, final int ID) {
		this.name = name;
		this.ID = ID;
		this.ip =ip;
		this.port = port;
	}
	
	public int getID() {
		return ID;
	}
}
