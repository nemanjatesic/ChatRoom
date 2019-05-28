package chat;

import javax.swing.JFrame;

public class ServerFrame extends JFrame{
	
	private Server server;

	public ServerFrame() {
		try {
			server = Server.getInstance();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void open() {
		ServerFrame serverFrame = new ServerFrame();
		serverFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		open();
	}
	
	public Server getServer() {
		return server;
	}
}
