package principal;

import paquetes.*;
import recursos.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;


public class Cliente {
	
	private static final int MAX_TAM = 516;
    private static final int MAX_RETRY = 3;
    private static final int TIMEOUT= 1000;

	
	public static void get(InetAddress addr, int port, String file, String mode) {
		
		// Declaracion de variables
		int tries;
		short numBlock = 1;
		boolean answer;
		DATA data = null;
		byte[] buffer = new byte[0];
		
		DatagramSocket socket;
		DatagramPacket send;
		DatagramPacket receive;
		
		
		// Obtener tamaño del fichero para saber cuantos bloques tenemos que recibir
		long num = Fichero.getFileSize(file);
	  	long cont = 0;
		
		try {
			// Abrimos socket
			socket = new DatagramSocket();
			
			// Codificamos el paquete de lectura
			byte [] pkg = new RRQ(file,mode).encodeRRQ();
			
			// Iniciamos comunicacion con el servidor enviando solicitud de lectura
			send = new DatagramPacket(pkg, pkg.length, addr, port);
			socket.send(send);
			System.out.println(" ----> RRQ " + file + " " + mode);
			
			do {
				
				// Inicializacion de variable
				tries = 0;
				answer = false;
				
				do {
					// Inicializacion del timer.
					socket.setSoTimeout(TIMEOUT);
					
					try {
						
						// Recibimos información del servidor
						receive = new DatagramPacket(new byte[MAX_TAM], MAX_TAM);
						socket.receive(receive);
						
						// Comprobamos el TID (Identificador de transferencia)	
						if(!Asistente.checkTID(receive,addr,port)) {
							pkg = new ERROR((short)05, "El paquete proviene de un servidor desconocido").encodeERROR();
							send = new DatagramPacket(pkg, pkg.length,addr,port);
							socket.send(send);
							answer = false;
							break;
						}
						
						// Recibimos un paquete de información del archivo que queremos descargar del servidor.
						// Aumentamos el contador, un paquete menos que recibir.
						byte[] contenido = receive.getData();
						cont++;
						
						// Si es el ultimo paquete limpiamos el contenido ya que no sera de 512 bytes por ser el ultimo.
						if(cont-1 == num) {
							contenido = Fichero.clearArray(contenido);
						}
						
						// Una respuesta aun GET solo puede ser DATA o ERROR
						
						// DATA
						if(contenido[0] == 0 && contenido[1] == 3) {
							DATA d = new DATA(contenido);
							
							// Comprobamos que el paquete recibido viene en el orden esperado
							if(Asistente.checkOrder(numBlock, d.getBlockNum())) {
								System.out.println((" <---- DATA " + (d.getBlockNum()) + " " + (d.getData().length) + " bytes"));
								data = new DATA(receive.getData());
								
								// Metemos el paquete recibido en buffer donde se iran almacenando cada paquete que vaya llegando. 
								buffer = Fichero.joinArrays(buffer, data.getData());
							} else {
								pkg = new ERROR((short)04 ,  "Se ha recibido un paquete fuera de orden").encodeERROR();
								send = new DatagramPacket(pkg, pkg.length,addr, port);
								socket.send(send);
								answer = false;
								break;
							}
						}
						
						// ERROR
						if(contenido[0] == 0 && contenido[1] == 5) {
							ERROR err = new ERROR(contenido);
							System.out.println(" <---- ERROR " + err.getErrorCode() + " " + err.getErrorMsg());
							answer = false;
							break;
						}
						
						// Hemos recibido el paquete correspondiente y mandamos el ACK al servidor.
						answer = true;

						pkg = new ACK(numBlock).encodeACK();
						send = new DatagramPacket(pkg, pkg.length,addr, port);
						socket.send(send);
						System.out.println(" ----> ACK " + numBlock);

					} catch(SocketTimeoutException e) {
						tries++;
						System.out.println("Error al enviar el paquete, quedan " + (MAX_RETRY - tries) + " intentos");
					}
					
				} while(!answer && tries < MAX_RETRY);
				
				// Si todo fue bien aumentamos el número de bloque y reiniciamos el timer.
				if(answer) {
					numBlock++;
					socket.setSoTimeout(0);
				} else break;
				
			} while(cont <= num);
			
			// Una vez que hemos recibido todos los bytes del fichero que pedimos lo escribimos en un nuevo fichero y si la
			// la información era textual la mostramos por pantalla.
			if(answer) {
				Fichero.createFile(file, buffer);
				if(mode.equals("ascii")) {
					System.out.println(new String(buffer));
				}
			}
			
			// La transmisión ha terminado, cerramos el socket.
			socket.close();
		} catch(IOException e) {
			System.err.println("Error al codificar un RRQ en el método get del cliente.");
			e.printStackTrace();
		}
	}
	
public static void put(InetAddress addr, int port, String file, String mode) {
		
		// Declaracion de variables necesarias
		int tries, pos;
		short numBlock = 0;
		boolean answer;
		DATA data;
		
		try {
			
			// Codificamos el paquete que queremos mandar.
			byte[] pkg = new WRQ(file, mode).encodeWRQ();
			
			// Declaramos los socket y paquetes que usaremos
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket send = null;
			DatagramPacket receive = null;
			
			// Enviamos el WRQ para iniciar la comunicacion con el servidor
			send = new DatagramPacket(pkg, pkg.length,addr,port);
			socket.send(send);
			System.out.println(" ----> WRQ " + file + " " + mode);
			
			do {
				
				//  Inicializacion de variables
				tries = 0;
				answer = false;
				pos = numBlock*512;
				
				// Leemos un trozo de fichero y lo codificamos dentro de un paquete DATA
				byte[] buffer = Fichero.readFile(file, pos);
				data = new DATA(numBlock ,buffer);
				pkg = data.encodeDATA();
				
				do {
					// Iniciamos el timer
					socket.setSoTimeout(TIMEOUT);
					
					try {
						
						// Recibimos contenido del servidor
						receive = new DatagramPacket(new byte[MAX_TAM], MAX_TAM);
						socket.receive(receive);
						
						// Comprobamos el TID
						if(!Asistente.checkTID(receive,addr,port)) {
							pkg = new ERROR((short)05, "El paquete proviene de un servidor desconocido").encodeERROR();
							send = new DatagramPacket(pkg, pkg.length,addr,port);
							socket.send(send);
							answer = false;
							break;
						}

						// La respuesta a un put solo puede ser ACK o ERROR
						
						byte[] contenido = receive.getData();
						
						// ACK
						if(contenido[0] == 0 && contenido[1] == 4) {
							ACK ack = new ACK(contenido);
							
							// Comprobamos que el orden de los paquetes sea correcto
							if(Asistente.checkOrder(numBlock, ack.getBlockNum())) {
								System.out.println(" <---- ACK " + ack.getBlockNum());
							} else {
								pkg = new ERROR((short)04 ,  "Se ha recibido un paquete fuera de orden").encodeERROR();
								send = new DatagramPacket(pkg, pkg.length,addr, port);
								socket.send(send);
								answer = false;
								break;
							}
						}
						
						// ERROR
						if(contenido[0] == 0 && contenido[1] == 5) {
							ERROR err = new ERROR(contenido);
							System.out.println(" <---- ERROR " + err.getErrorCode() + " " + err.getErrorMsg());
							answer = false;
							break;
						}
						
						// Una vez que todas las comprobaciones han sido superadas continuamos con la ejecucion
						// Mandamos el DATA correspondiente
						
						answer = true;
						send = new DatagramPacket(pkg, pkg.length,addr,port);
						socket.send(send);
						System.out.println(" ----> DATA " + (numBlock) + " " + (pkg.length-4) + " bytes");
						
					} catch(SocketTimeoutException e) {
						tries++;
						System.out.println("Error al enviar el paquete, quedan " + (MAX_RETRY - tries) + " intentos");
					}	
				} while(!answer && tries < MAX_RETRY);
				
				if(!answer) break;
				
				// Si todo fue correcto aumentamos el nº de bloque para leer el siguiente trozo de fichero y reiniciamos el timer.
				
				if(answer && Asistente.checkTID(receive,addr,port)) {
					numBlock++;
					socket.setSoTimeout(0);
				}
			} while(data.getData().length >= 512);
			
			// Se acabo la transmisión, podemos cerrar el socket.
			socket.close();
					
		} catch(IOException e) {
			System.err.println("Error al codificar un WRQ en el método put del cliente.");
			e.printStackTrace();
		}
	}
}
	
