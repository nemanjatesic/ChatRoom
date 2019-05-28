package chat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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
			if (name != null) userNames.add(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void broadcast(Object sender, Object message) {
		int i = 0;
		for (PrintWriter user : users) {
			if ((Socket)sender == userSockets.get(i)) {
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

	public static boolean isStarted() {
		if (serverInstance == null) return false;
		return true;
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

	@Override
	public void run() {
		try {
			Socket socket = new Socket(toString(), 2020);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true) {
				System.out.println("usao sam ovde");
				String message = "";
				message = in.readLine();
				if (message.equals("EXITING_NOW"))
					break;
				broadcast(socket, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "";
		}
		if (ip.contains("/")) {
			String[] split = ip.split("/");
			ip = split[split.length - 1];
		}
		return ip;
	}
}
