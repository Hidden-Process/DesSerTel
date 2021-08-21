package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import javax.swing.UIManager;

public class Model {
	
	private static final int BUFFER_SIZE = 1024;
	private static final int DEFAULT_PORT = 6543;
	
	public static void main(String[] args) {
		
		SocketChannel socket = null;
		InetSocketAddress addr;
		View view;
		
		try {
			if(args.length == 0) {
				addr = new InetSocketAddress("localhost" , DEFAULT_PORT);
			}else {
				addr = new InetSocketAddress(args[0], DEFAULT_PORT);
			}
			socket = SocketChannel.open(addr);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(socket != null) {
			view = new View(socket);
			
			while(true) {
				ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
				
				try {
					buffer.clear();
					socket.read(buffer);
					view.setText(new String(buffer.array()) + "\r\n");
				} catch(IOException e1) {
					e1.printStackTrace();
				}
				
				buffer.compact();
				buffer.clear();
				
			}
		} else {
			System.out.println("APP LOG: Error at socket creation");
		}
		
	}

}
