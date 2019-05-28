package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import observer.Listener;
import observer.Observer;

public class Client implements Runnable,Listener{

	private ArrayList<Observer> observers = new ArrayList<>();
	private Socket socket;
	private String nickname;
	private BufferedReader inSocket;
	private PrintWriter outSocket;

	public Client(String nickname) throws Exception {
		String ip = getIPv4Address();
		System.out.println(ip);
		socket = new Socket(ip, 2020);
		this.nickname = nickname;
		inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		outSocket = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		
		outSocket.println(nickname);

		Thread thread = new Thread(this);
		thread.start();
		
	}

	public Socket getSocket() {
		return socket;
	}

	public String getNickname() {
		return nickname;
	}

	public BufferedReader getInSocket() {
		return inSocket;
	}

	public PrintWriter getOutSocket() {
		return outSocket;
	}

	public static void main(String[] args) {
		try {
			new Client("cao");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getIPv4Address() throws Exception{
		String ip = "";
		ip = InetAddress.getLocalHost().toString();
		if (ip.contains("/")) {
			String[] split = ip.split("/");
			ip = split[split.length - 1];
		}
		return ip;
	}

	@Override
	public void run() {
		try {
			while (true) {
				String msg = inSocket.readLine();
				System.out.println("Server kaze: " + msg);
				notify(msg);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
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
