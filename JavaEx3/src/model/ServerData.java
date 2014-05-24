package model;

public class ServerData {
	// Data Members
	private String ip;
	private int port;
	
	// ServerData constructor
	public ServerData(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

}
