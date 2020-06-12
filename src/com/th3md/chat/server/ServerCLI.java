package com.th3md.chat.server;

public class ServerCLI {
	
	private int port;
	private Server server;
	
	public ServerCLI(int port) {
		this.port = port;
		server = new Server(port);
	}
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Usage: java -jar ServerCLI.jar [port]");
			return;
		}
		new ServerCLI(Integer.parseInt(args[0]));
	}
}
