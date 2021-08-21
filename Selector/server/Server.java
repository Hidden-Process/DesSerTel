package server;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.nio.ByteBuffer;

public class Server {
	
	private static final int BUFFER_SIZE = 1024;
    private static final int DEFAULT_PORT = 6543;
    
    private Set<SocketChannel> clients;
    private ServerSocketChannel server;   
    private Selector selector;
    
    public static void main(String[] args) {
		new Server().run();
	}
    
    public Server() {
    	try {
    		
    		// Abrimos el selector 
    		selector = Selector.open();
    		
    		// Inicializamos el conjunto donde se almacenaran los usuarios en linea.
    		clients = new HashSet<>();
    		
    		// Abrir canal pasivo en el selector y establecer lectura no bloqueante
    		server = ServerSocketChannel.open();
    		server.socket().bind(new java.net.InetSocketAddress(DEFAULT_PORT));
    		server.configureBlocking(false);
    		server.register(selector, SelectionKey.OP_ACCEPT);
    		
    	} catch(IOException e) {
    		System.out.println("SERVER LOG: Error Inicializing the server");
    		e.printStackTrace();
    		System.exit(-1);
    	} 
    	
    	System.out.println("SERVER LOG: Server is now running ...");
    }
    
    public void run() {
    	while(true) { // Espera activa
    		try {
    			selector.select();  // Llamada bloqueante
    			
    			// Iteración sobre el conjunto de canales activos 
    			Set<SelectionKey> keys = selector.selectedKeys();
    			for(Iterator<SelectionKey> i = keys.iterator();i.hasNext();) {
    				SelectionKey key = i.next();
    				i.remove();
    				
    				// Despachar petición de conexión 
    				if(key.isAcceptable()) {
    					accept();
    				}
    				
    				// Despachar petición de lectura 
    				if(key.isReadable()) {
    					read(key);
    				}
    			}
    		} catch(IOException e) {
    			System.out.println("SERVER LOG: The server has encounter a problem");
    			e.printStackTrace();
    		}
    		
    	}
    }

	private void accept() {
		try {
			// Se registra nuevo canal de lectura y se añade al usuario al conjunto de usuarios conectados.
			SocketChannel client = server.accept();
			client.configureBlocking(false);
			client.register(selector, SelectionKey.OP_READ,ByteBuffer.allocate(BUFFER_SIZE));
			clients.add(client);
			System.out.println("New User Joined the Chat Room");
			System.out.println("Users in the Room at the moment: " + clients.size());
		} catch(IOException e) {
			System.out.println("SERVER LOG: Error Accepting New Client");
		}
		
	}
	
	private void read(SelectionKey key) {
		SocketChannel client = (SocketChannel) key.channel();
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		try {
			// ¿Hay algo que leer en el buffer?
			buffer.clear();
			int bytesread = client.read(buffer);
			
			// Si se cierra la conexión de forma ordenada
			if(bytesread == -1) {
				clients.remove(client);
				key.cancel();
				client.close();
				System.out.println("User Left the Room Chat");
				System.out.println("Users in the Room at the moment: " + clients.size());
				
			} else { // Leemos el buffer y pasamos a escribir.
				buffer.flip();
				byte[] receive = new byte[buffer.remaining()];
				buffer.get(receive);
				String msg = new String(receive);
				write(msg);
			}
		} catch(IOException e) { // Si se  cierra la conexión de forma abrupta
			System.out.println("SERVER LOG: " + e.getMessage());
			clients.remove(client);
			System.out.println("User Left the Room Chat");
			System.out.println("Users in the Room at the moment: " + clients.size());
			try {
				client.close();
				client.socket().close();
			} catch(Exception e1) {
				System.out.println("SERVER LOG: Error closing the socket");
			}
	
		}
		
	}
	
	
	private void write(String msg){
		
		// Iteramos sobre los usuarios online y escribimos.
		for(Iterator<SocketChannel> i = clients.iterator();i.hasNext();) {
			
			SocketChannel client = i.next();
			ByteBuffer message = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
			
			try {
				if(client.isConnected()) {
					client.write(message);
				} else {
					i.remove();
				}
			} catch(IOException e) {
				System.out.println("SERVER LOG: Error Sending a Message");
			}

		}
	}

}
