package chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class ClientFrame extends JFrame {

	private Client client;
	private JTextArea chatBox;
	private JTextField messageBox;
	private JButton sendMessage;
	private String nickname;

	public ClientFrame(String nickname) {
		this.nickname = nickname;
		
		/*try {
			client = new Client(nickname);
		}catch (Exception e) {
			e.printStackTrace();
			return;
		}*/
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(nickname + "'s chat");
		setSize(470, 300);
		setLocationRelativeTo(null);

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridBagLayout());

		messageBox = new JTextField(30);
		messageBox.addFocusListener(new MyFocusListener());
		messageBox.setText("Type a message...");
		messageBox.addActionListener(new MyActionListner());

		chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setLineWrap(true);
		chatBox.setBorder(new EmptyBorder(5, 5, 5, 5));

		sendMessage = new JButton("Send Message");
		sendMessage.addActionListener(new MyActionListner());

		this.add(new JScrollPane(chatBox), BorderLayout.CENTER);

		GridBagConstraints left = new GridBagConstraints();
		left.anchor = GridBagConstraints.LINE_START;
		left.fill = GridBagConstraints.HORIZONTAL;
		left.weightx = 512;
		left.weighty = 1;

		GridBagConstraints right = new GridBagConstraints();
		right.insets = new Insets(0, 10, 0, 0);
		right.anchor = GridBagConstraints.LINE_END;
		right.fill = GridBagConstraints.NONE;
		right.weightx = 1;
		right.weighty = 1;
		
		messageBox.setBorder(new EmptyBorder(0, 10, 0, 0));

		southPanel.add(messageBox, left);
		southPanel.add(sendMessage, right);

		this.add(BorderLayout.SOUTH, southPanel);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void open() {
		ClientFrame clientFrame = new ClientFrame("Nemanja");
		clientFrame.setVisible(true);
	}

	// Testing
	public static void main(String[] args) {
		open();
	}

	class MyFocusListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {
			if (messageBox.getText().equals("Type a message...")) {
				messageBox.setText("");
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (sendMessage.isFocusOwner()) {
				messageBox.setText("Type a message...");
			}
			if (messageBox.getText().equals("")) {
				messageBox.setText("Type a message...");
			}
		}
	}
	
	class MyActionListner implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (messageBox.getText().length() < 1 || messageBox.getText().equals("Type a message...")) {
				return;
			} else if (messageBox.getText().equals("/clear")) {
				chatBox.setText("Cleared all messages\n");
				Timer t = new Timer(1500, new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {
		            	chatBox.setText("");
		            }
		        });
		        t.setRepeats(false);
		        t.start();
				messageBox.setText("Type a message...");
			} else {
				//client.getOutSocket().println("<" + nickname + ">:  " + messageBox.getText() + "\n");
				chatBox.append("<" + nickname + ">:  " + messageBox.getText() + "\n");
				messageBox.setText("Type a message...");
			}
			if(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == messageBox) {
				messageBox.setText("");
			}
		}
		
		
	}
}
