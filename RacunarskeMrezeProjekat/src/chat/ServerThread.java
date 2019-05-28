package chat;

import java.io.BufferedReader;
import java.io.IOException;
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
		System.out.println("Nesto ce ispisati : " + server);
		/*try {
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
		}*/
		try {
			System.out.println("usao");
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String inp = null;

			inp = in.readLine();
			
			server.addClient(socket, inp);
			
			
			boolean isDone = true;

			System.out.println("TYPE : BYE");
			System.out.println();
			while (isDone && ((inp = in.readLine()) != null)) {

				System.out.println("Ovo " + inp);
				if (inp.trim().equals("EXITING_NOW")) {
					//System.out.println("THANKS FOR CONNECTING...Bye for now");
					isDone = false;
				}
				server.broadcast(socket, inp);
			}
		} catch (IOException e) {
			/*try {
				//server_socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}*/
			e.printStackTrace();
		}
	}

}
