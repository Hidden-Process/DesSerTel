package principal;

import paquetes.*;
import recursos.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Servidor {
	
    private static final int TIMEOUT = 1000;
    private static final int MAX_RETRY = 3;
    private static final int TAM_MAX = 516;
    private static final int SERVER_PORT = 6789;
    
    private static InetAddress addr;
    private static int port;
    private static SocketAddress fulladdr;

    public static void main(String[] args) {
    	
    	try {
    		
    		// Abrimnos el socket en el puerto especificado
    		DatagramSocket socket = new DatagramSocket(SERVER_PORT);
    		
    		System.out.println("Servidor iniciado en puerto " + SERVER_PORT );
    		
    		while(true) {
        		
        		try {
        			DatagramPacket receive;
        			
            		byte[] pkg;
            		
            		// Recibimos solicitud
            		receive = new DatagramPacket(new byte[TAM_MAX], TAM_MAX);
            		socket.receive(receive);
            		
            		pkg = receive.getData();
            		addr = receive.getAddress();
            		port = receive.getPort();
            		fulladdr = receive.getSocketAddress();
            		
            		// Recibimos RRQ 
            		if(pkg[0]==0 && pkg[1] == 1) {
            			RRQ rrq = new RRQ(pkg);
            			get(socket, receive, rrq);
            		}
            		
            		// Recibimos WRQ
            		if(pkg[0]==0 && pkg[1] == 2) {
            			WRQ wrq = new WRQ(pkg);
            			put(socket, receive, wrq);
            		}
            		
        			} catch(IOException e) {
        			System.err.println("Error al recibir la petición en el servidor");
        			e.printStackTrace();
        			}
    			}	
    		}  catch(SocketException e) {
    		System.err.println("Error al Inicializar el Socket en el Servidor");
    		e.printStackTrace();
    	}
    	
    }
    
    public static void get(DatagramSocket socket, DatagramPacket pkg, RRQ packet) {
    	
    	// Declaración de variables
    	int tries;
    	boolean answer;
    	byte[] data;
    	short blockNum = 1;
    	int pos;
    	DATA d;
    	DatagramPacket send;
    	String filename = packet.getFilename();
    	  	
    	try {
    		do {
        		
    			// Inicializacion de variables
        		tries = 0;
        		answer = false;
        		pos = (blockNum-1) * 512;
        		
        		// Leemos del fichero especificado y codificamos la informacion a enviar en un paquete DATA
        		
        		d = new DATA(blockNum,Fichero.readFile(filename, pos));
        		data = d.encodeDATA();
        		
        		do {
        			
        			// Preparamos el paquete, iniciamos el timer y lo enviamos
        			send = new DatagramPacket(data, data.length, addr, port);
        			socket.setSoTimeout(TIMEOUT);
        			socket.send(send);
        			
        			try {
        				
        				// Recibimos respuesta por parte del cliente
        				pkg = new DatagramPacket(new byte[TAM_MAX], TAM_MAX);
        				socket.receive(pkg);
        				answer = true;
        				
        				byte[] receive = pkg.getData();
        				
        				// Comprobamos el TID
        				
        				if(!pkg.getSocketAddress().equals(fulladdr)) {
        					data = new ERROR((short) 02, "Cliente desconocido").encodeERROR();
        					DatagramPacket request = new DatagramPacket(data,data.length,addr,port);
        					socket.send(request);
        					answer=false;
        					break;
        				}
        				
        				// Comprobamos que no hayamos recibido un error, ya que al enviar un DATA solo podemos
        				// recibir un ACK o un ERROR.
        				if(receive[0] == 0 && receive[1] == 5) {
        					ERROR err = new ERROR(receive);
							System.out.println(" ----> ERROR " + err.getErrorCode() + " " + err.getErrorMsg());
							answer = false;
							break;
        				}

        			} catch(SocketTimeoutException e) {
        				tries++;
        				System.out.println("Error al enviar el paquete, quedan " + (MAX_RETRY - tries) + " intentos");
        			}
        			
        		} while(!answer && tries < MAX_RETRY);
        		
        		// Reiniciamos el timer
        		socket.setSoTimeout(0);
        		
        		if(!answer) break;
        		
        		// Si todo fue correcto incrementamos el numero de bloque para leer el siguiente trozo de fichero.
        		if(pkg.getSocketAddress().equals(fulladdr))  blockNum++;
        		
        	} while(d.getData().length >= 512);
    		
    	} catch(IOException e) {
    		System.err.println("Error en el servidor al intentar responder la petición del cliente");
    		e.printStackTrace();
    	}
    }
    
    public static void put(DatagramSocket socket, DatagramPacket pkg, WRQ packet) {
    	
    	// Declaracion de variables
    	int tries;
    	DatagramPacket send;
    	byte[] data;
    	byte[] buffer = new byte[0];
    	boolean answer;
    	String filename = packet.getFilename();
    	
    	short blockNum = 0;
    	DATA d = null;
   
    	// Obtenemos el numero de paquete que tendremos que recibir del cliente gracias al tamaño del archivo 
		long num = Fichero.getFileSize(filename);
	  	long cont = 0;
	  	
    	try {
    		
    		do {
        		
    			// Inicializamos variables
        		tries = 0;
        		answer = false;
        		
        		// Codificamos el ACK que debemos enviar cuando recibamos los datos del cliente.
        		data = new ACK(blockNum).encodeACK();
        		
        		do {
        			// Inicializamos el timer
        			socket.setSoTimeout(TIMEOUT);
        			
        			try {
        				
        				
        				// Recibimos DATA y enviamos ACK
        				
        				send = new DatagramPacket(data, data.length,addr,port);
            			socket.send(send);
            			
            			
        				pkg = new DatagramPacket(new byte[TAM_MAX], TAM_MAX);
            			socket.receive(pkg);
            			
     
            			// Comprobamos que los paquetes estan en el orden correcto
            			if(!Asistente.checkOrder(blockNum, new ACK(pkg.getData()).getBlockNum())) {
							data = new ERROR((short)02 ,  "Se ha recibido un paquete fuera de orden").encodeERROR();
							DatagramPacket request = new DatagramPacket(data, data.length, addr, port);
							socket.send(request);
							answer = false;
							break;
            			}
            			
            			// Los bytes del fichero original
            			byte[] contenido = pkg.getData();
            			cont++;
            			
            			// Si es el ultimo paquete limpiamos el bloque recibido que será menor de 512
            			if(cont == num) {
            				contenido = Fichero.clearArray(contenido);
            			}
            			
            			// Comprobamos que el identificador de transferencia es el correcto
            			
            			if(pkg.getSocketAddress().equals(fulladdr)) {
            				d = new DATA(contenido);
            				
            				// Volcamos en buffer el contenido del fichero leido en el bloque recibido
            				buffer = Fichero.joinArrays(buffer, d.getData());
            				
            				// Como todo ha ido bien aumentamos el numero de bloque para leer el siguiente trozo de fichero.
            				blockNum++;
            				answer = true;
            			} else {
            				data = new ERROR((short)02, "Emisor del paquete desconocido").encodeERROR();
            				DatagramPacket request = new DatagramPacket(data, data.length, addr, port);
							socket.send(request);
							answer = false;
							break;
            			}
            			
            			// En caso de que lo recibido fuera un error en lugar de DATA
            			if(contenido[0]==0 && contenido[1]==5) {
            				ERROR err = new ERROR(contenido);
							System.out.println(" ----> ERROR " + err.getErrorCode() + " " + err.getErrorMsg());
            				answer = false;
            				break;
            			}
        			} catch(SocketTimeoutException e) {
        				tries++;
        				System.out.println("Error al enviar el paquete, quedan " + (MAX_RETRY - tries) + " intentos");
        			}
        			
        		} while(!answer && tries < MAX_RETRY);
        		
        		// Reiniciamos el timer llegados a este punto.
        		socket.setSoTimeout(0);
        		
        		if(!answer) break;
        		
        	} while(cont <= num);
    		
    		// Si todo ha  ido correcto creamos el fichero con la informacion volcada en buffer.
    		if(answer) Fichero.createFile(filename, buffer);
    		
    	} catch(IOException e) {
    		System.err.println("Error al gestionar el método put en el servidor");
    	}
    	
    }
}
