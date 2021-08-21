package recursos;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class Asistente {
	
	// Comprobamos que los TID coincidan
	public static boolean checkTID(DatagramPacket packet, InetAddress address, int port) {
		boolean tid = false;
		if(packet.getAddress().equals(address) && packet.getPort() == port) tid = true;
		return tid;
	}
	
	// Comprobamos que los números de bloques sean consecutivos
	public static boolean checkOrder(short blockNum, short packetNumber) {
		boolean check = false;
		if(blockNum == packetNumber) check = true;
		return check;
	}

}
