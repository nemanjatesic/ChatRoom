package chat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OpeningClientFrame extends JFrame {
	private JComboBox<Server> servers;
	private JTextField tfUsername;

	public OpeningClientFrame() {
		setSize(400, 370);
		servers = new JComboBox<>();
		tfUsername = new JTextField(15);
		JLabel lblUsername = new JLabel("Enter a username : ");
		JLabel lblServer = new JLabel("Choose server : ");
		JPanel panel = new JPanel(new GridBagLayout());
		JButton btnJoin = new JButton("Join");

		btnJoin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (servers.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(null, "Please select some server!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				String username = tfUsername.getText();
				if (username.equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter username!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!Server.IsNameViable(username)) {
					JOptionPane.showMessageDialog(null, "Username is already taken!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				new ClientFrame(username);
			}
		});

		GridBagConstraints c = getConstraints(0, 0);

		panel.add(lblUsername, c);
		c = getConstraints(1, 0);
		tfUsername.setPreferredSize(new Dimension(200, 35));
		panel.add(tfUsername, c);

		c = getConstraints(0, 1);
		panel.add(lblServer, c);
		c = getConstraints(1, 1);
		servers.setPreferredSize(new Dimension(170, 35));
		panel.add(servers, c);

		this.add(BorderLayout.CENTER, panel);
		this.add(BorderLayout.SOUTH, btnJoin);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Client");
		setLocationRelativeTo(null);
	}

	public static void open() {
		OpeningClientFrame openingClientFrame = new OpeningClientFrame();
		openingClientFrame.setVisible(true);
	}

	// Testing
	public static void main(String[] args) {
		open();
	}

	private GridBagConstraints getConstraints(int x, int y) {
		GridBagConstraints constrain = new GridBagConstraints();
		constrain.gridx = x;
		constrain.gridy = y;
		constrain.insets = new Insets(20, 20, 0, 0);
		constrain.anchor = GridBagConstraints.WEST;
		return constrain;
	}

}