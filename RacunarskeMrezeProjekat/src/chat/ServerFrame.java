package chat;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import observer.Observer;

public class ServerFrame extends JFrame implements Observer{
	
	private Server server;
	private JTextArea chatBox;

	public ServerFrame() {
		try {
			server = Server.getInstance();
			server.addListener(this);
			
			setTitle("Server : " + server);
			setSize(470, 300);
			setLocationRelativeTo(null);
			
			chatBox = new JTextArea();
			chatBox.setEditable(false);
			chatBox.setLineWrap(true);
			chatBox.setBorder(new EmptyBorder(5, 5, 5, 5));
			chatBox.setText("Connected clients list : \n");
			DefaultCaret caret = (DefaultCaret)chatBox.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			
			this.add(new JScrollPane(chatBox), BorderLayout.CENTER);
			
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent windowEvent) {
					if (Server.isStarted())
						Server.getInstance().stop();
				}
			});
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void open() {
		ServerFrame serverFrame = new ServerFrame();
		serverFrame.setVisible(true);
	}
	
	public Server getServer() {
		return server;
	}

	@Override
	public void update(Object o) {
		if (!(o instanceof String)) {
			return;
		}
		String str = (String) o;
		if (str.startsWith("Delete: ")) {
			String split[] = str.split("Delete: ");
			String splitChat[] = chatBox.getText().split("\n");
			int i;
			chatBox.setText("");
			for (i = 0 ; i < splitChat.length ; i++) {
				if (!(splitChat[i].equals(split[1]))) {
					chatBox.append(splitChat[i] + "\n");
				}
			}
		}else if (str.startsWith("Add: ")) {
			String split[] = str.split("Add: ");
			chatBox.append(split[1] + "\n");
		}
	}
}
