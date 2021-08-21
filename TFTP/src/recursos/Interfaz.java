package recursos;

import principal.Cliente;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Interfaz {
	
	private static final int SERVER_PORT = 6789;
	
	public static void main(String[] args) {
		
		InetAddress host = null;
		int port = SERVER_PORT;
		
		Scanner sc = new Scanner(System.in);
		
		String[] command;
		String filename;
		
		// Modo Ascii por defecto si no es modificado 
		String mode = "ascii";
		
		try {
			while(true) {
				System.out.print("tftp> ");
				command = sc.nextLine().split(" ");
				
				if(command[0].equals("help")) {
					showHelp();
				}
				
				else if(command[0].equals("connect")) {
					try {
						host = InetAddress.getByName(command[1]);
					}catch (UnknownHostException e) {
						System.err.println("Error en la direccion IP del servidor");
					}
				}
				
				else if(command[0].equals("mode")) {
					if((!command[1].equals("ascii")) && (!command[1].equals("binary"))){
						System.out.println("Error el modo debe ser ascii o binary");
					} else {
						mode = command[1];
					}
				}
				
				else if(command[0].equals("put")) {
					if(host == null) {
						System.out.println("Primero debes conectarte al servidor usando el comando connect");
					} else {
					filename = command[1];
					Cliente.put(host, port, filename, mode);
					}
				}
				
				else if(command[0].equals("get")) {
					if(host == null) {
						System.out.println("Primero debes conectarte al servidor usando el comando connect");
					} else {
					filename = command[1];
					Cliente.get(host, port, filename, mode);
					}
				}
				
				else if(command[0].equals("quit")) {
					sc.close();
					break;
				}
				
				else {
					System.out.println("Error, comando no reconocido, a continuación se mostrará la ayuda");
					showHelp();
				} 
			}
		} catch(Exception e) {
			System.out.println("Se ha producido algún error al introducir un comando");
			System.out.println("Vuelve a ejecutar la aplicación y usa el comando help para ver las opciones disponibles");
		}
			
	}
	
	
	public static void showHelp() {
		System.out.print("\n");
		System.out.println("Bienvenido al cliente CLI de la la aplicación TFTP \n");
		System.out.println("Los comandos disponibles son los siguientes: \n");
		System.out.println("help  --> Te muestra los comandos disponibles");
		System.out.println("connect <host> --> Registra la dirección del servidor");
		System.out.println("mode <ascii|binary> --> Especifica el modo de transmisión para los ficheros");
	    System.out.println("put <filename> --> Subir el fichero especificado al servidor, importante haber especificado un host"
	    		+ "valido antes usando el método connect.");
	    System.out.println("get <filename> --> Descargar el fichero especificado del servidor, importante haber especificado un host"
	    		+ "valido antes usando el método connect.");
	    System.out.println("quit --> Te desconecta del servidor, finalizando las transmisiones y cerrando la interfaz de usuario");
	    System.out.print("\n");
	}

}
