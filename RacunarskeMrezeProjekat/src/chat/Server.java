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

import observer.Listener;
import observer.Observer;

public class Server implements Runnable,Listener {

	private static Server serverInstance = null;
	private ArrayList<PrintWriter> users;
	private ArrayList<Socket> userSockets;
	private ArrayList<Observer> observers;
	private static ArrayList<String> userNames;
	private static ServerSocket server_socket;
	

	private Server() throws Exception {
		users = new ArrayList<>();
		userNames = new ArrayList<>();
		observers = new ArrayList<>();
		userSockets = new ArrayList<>();
		server_socket = new ServerSocket(2020);

		System.out.println("Otvoren port 2020");
		
		Thread thread = new Thread(this);
		thread.start();
	}

	public void addClient(Socket socket, String name) {
		try {
			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			users.add(out);
			userSockets.add(socket);
			userNames.add(name);
			broadcast(null, "Client : " + name + " has just joined the chat.");
			notify("Add: " + name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeClient(String nickname) {
		try {
			int i;
			for (i = 0 ; i < userNames.size() ; i++) {
				if (userNames.get(i).equals(nickname))
					break;
			}
			if (i == userNames.size()) {
				System.out.println("Debug, should not be here.");
				return;
			}
			broadcast(null, "Client : " + userNames.get(i) + " has left the chat.");
			users.remove(i);
			userSockets.remove(i);
			userNames.remove(i);
			notify("Delete: " + nickname);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void broadcast(Object sender, Object message) {
		int i = 0;
		if (!(message instanceof String)) return;
		if (((String) message).equals("EXITING_NOW")) {
			for (int j = 0 ; j < users.size() ; j++) {
				if ((Socket) sender == userSockets.get(j)) {
					removeClient(userNames.get(j));
					return;
				}
			}
			return;
		}
		for (PrintWriter user : users) {
			if (sender == null) {
				user.println((String) message);
				continue;
			}
			if ((Socket) sender == userSockets.get(i)) {
				String tmp = "<";
				if (!(message instanceof String))
					return;
				tmp += getUserNickname((String) message) + ">";
				String[] split = ((String) message).split(tmp);
				tmp = "<You>";
				tmp += split[1];
				user.println(tmp);
			} else {
				user.println((String) message);
			}
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

	public static boolean isStarted() {
		if (serverInstance == null)
			return false;
		return true;
	}

	public void stop() {
		try {
			if (server_socket != null)
				server_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getUserNickname(String nick) {
		String tmp = "";
		int i = 1;
		while (nick.charAt(i) != '>') {
			tmp += nick.charAt(i);
			i++;
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
			while(true) {
				Socket incoming = server_socket.accept();
	            Thread t = new Thread(new ServerThread(incoming,this));
	            t.start();
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

	@Override
	public void addListener(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void removeListener(Observer observer) {
		observers.remove(observer);
	}

	@Override
	public void notify(Object o) {
		for (Observer ob : observers) {
			ob.update(o);
		}
	}
}
