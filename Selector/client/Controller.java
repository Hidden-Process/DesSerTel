package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Controller implements ActionListener{
	
	private JTextArea chatBox;
	private JTextField messageBox;
	SocketChannel socket;
	
	public Controller(JTextField messageBox, JTextArea chatBox, SocketChannel socket) {
		super();
		this.chatBox = chatBox;
		this.messageBox = messageBox;
		this.socket = socket;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(messageBox.getText().length() <1) {
			// Message Box is empty ==> Do Nothing
		} else {
			try {
				String username = System.getProperty("user.name") + ": ";
				String mensaje = username + messageBox.getText();
				byte[] msg = mensaje.getBytes();
				ByteBuffer buff = ByteBuffer.wrap(msg);
				socket.write(buff);
				buff.clear();
			} catch(IOException excp) {
				chatBox.append("CHAT LOG: Error sending the message");
			}
			
			messageBox.setText("");
		}
		
		messageBox.requestFocusInWindow();
	}

}
