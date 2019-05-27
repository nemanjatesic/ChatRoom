package chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.Timer;

public class Server implements Runnable{

	private static Server serverInstance = null;
	private ArrayList<PrintWriter> users;
	private ArrayList<Socket> userSockets;
	private static ArrayList<String> userNames;
	private static ServerSocket server_socket;
	protected Thread nit;

	private Server() throws Exception {
		users = new ArrayList<>();
		userSockets = new ArrayList<>();
		server_socket = new ServerSocket(2020);

		System.out.println("Otvoren port 2020");
		
		nit = new Thread(this);
		nit.start();
		ClientFrame.open();
	}

	public void stop() {
		System.out.println("cao");
	}

	public void addClient(Socket socket, String name) {
		try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			users.add(out);
			userSockets.add(socket);
			userNames.add(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void broadcast(Object sender, Object message) {
		int i = 0;
		for (PrintWriter user : users) {
			if (sender == userSockets.get(i)) {
				String tmp = "<";
				if (!(message instanceof String))
					return;
				tmp += getUserNickname((String) message) + ">";
				String[] split = ((String) message).split(tmp);
				tmp = "<You>";
				tmp += split[1];
				user.println(tmp);
			} else
				user.println((String) message);
			i++;
		}
	}

	public static Server getInstance() {
		if (serverInstance == null) {
			try {
				serverInstance = new Server();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return serverInstance;
	}

	public static void main(String[] args) {
		System.out.println("ovde");
		try {
			new Server();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getUserNickname(String nick) {
		String tmp = "";
		int i = 1;
		while (nick.charAt(i) != '>') {
			tmp += nick.charAt(i);
		}
		return tmp;
	}
	
	public static boolean IsNameViable(String name) {
		for (String s : userNames) {
			if (s.equals(name))
				return false;
		}
		return true;
	}
	
	public static void newThread() throws Exception{
		System.out.println("poz");
		Socket socket = server_socket.accept();
		ServerThread server_thread = new ServerThread(socket, serverInstance);
		Thread thread = new Thread(server_thread);
		thread.start();
	}

	@Override
	public void run() {
		try {
			
			while(true) {
				System.out.println("cao");
				Socket socket = server_socket.accept();
				ServerThread server_thread = new ServerThread(socket, this);
				Thread thread = new Thread(server_thread);
				thread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
