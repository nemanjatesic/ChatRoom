package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {

	private Socket socket;
	private Server server;
	
	public ServerThread(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
	}

	@Override
	public void run() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//String name = in.readLine();
			server.addClient(socket, "a");
			while (true) {
				System.out.println("usao sam ovde");
				String message = "";
				message = in.readLine();
				if (message.equals("EXITING_NOW"))
					break;
				server.broadcast(socket, message);
			}
			in.close();
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
