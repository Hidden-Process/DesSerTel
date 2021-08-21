package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.nio.channels.SocketChannel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class View {

	private String appName = "Chat Client";
	private JFrame newFrame = new JFrame(appName);
	private JButton sendMessage;
	private JTextField messageBox;
	private JTextArea chatBox;

	public View(SocketChannel socket) {

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel downPanel = new JPanel();
		downPanel.setLayout(new GridBagLayout());

		sendMessage = new JButton("Send Message");

		messageBox = new JTextField(100);
		messageBox.requestFocusInWindow();

		chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setFont(new Font("Serif", Font.PLAIN, 14));
		chatBox.setLineWrap(true);

		Controller controller = new Controller(messageBox, chatBox, socket);
		messageBox.addActionListener(controller);
		sendMessage.addActionListener(controller);

		mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

		GridBagConstraints left = new GridBagConstraints();
		left.anchor = GridBagConstraints.LINE_START;
		left.fill = GridBagConstraints.HORIZONTAL;
		left.weightx = 512.0D;
		left.weighty = 1.0D;

		GridBagConstraints right = new GridBagConstraints();
		right.insets = new Insets(0, 10, 0, 0);
		right.anchor = GridBagConstraints.LINE_END;
		right.fill = GridBagConstraints.NONE;
		right.weightx = 1.0D;
		right.weighty = 1.0D;

		downPanel.add(messageBox, left);
		downPanel.add(sendMessage, right);

		mainPanel.add(BorderLayout.SOUTH, downPanel);

		newFrame.add(mainPanel);
		newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newFrame.setSize(600, 400);
		newFrame.setVisible(true);

		centreWindow(newFrame);
	}

	public static void centreWindow(JFrame frame) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
	}

	public void setText(String msg) {
		chatBox.append(msg);
	}

}
