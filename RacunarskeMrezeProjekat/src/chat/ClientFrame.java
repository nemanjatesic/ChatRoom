package chat;

import javax.swing.JFrame;

public class ClientFrame extends JFrame{
	
	private Client client;

	public ClientFrame(String nickname, Server server) {
		client = new Client();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(nickname + "'s chat");
		setSize(380, 450);
		setLocationRelativeTo(null);
	}
	
	public static void open() {
        ClientFrame clientFrame = new ClientFrame("Nemanja",null);
        clientFrame.setVisible(true);
    }
	
	// Testing
	public static void main(String[] args) {
		open();
	}
}
