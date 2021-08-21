package paquetes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RRQ {
	
	private short opcode;
	private String filename;
	private String mode;
	
	public RRQ(String filename, String mode) {
		opcode = (short) 01;
		this.filename = filename;
		this.mode = mode;
	}
	
	// Decode RRQ
	public RRQ(byte[] msg) {
		
		ByteArrayInputStream byteStream = new ByteArrayInputStream(msg);
		DataInputStream in = new DataInputStream(byteStream);
		
		byte[] aux;
		
		/* Usamos estas variables como separadores para identificar donde empieza
		 * y acaba cada uno de los campos variables, sabiendo cuantos bytes 
		 * necesitamos leer.
		 */
		
		int first = -1;
		int second = -1;
		
		int i = 2;
		while(msg[i] != (byte)0) {
			i++;
		}
		first = i;
		
		int j = first+1;
		while(msg[j] != (byte)0) {
			j++;
		}
		second = j;
		
		try {
			
			// Opcode
			this.opcode = in.readShort();
			
			// Filename
			aux = new byte[first];
			in.readFully(aux, 0, first-2);
			this.filename = new String (aux);
			
			in.skipBytes(1);
			
			// Mode
			aux = new byte[second - first -1];
			in.readFully(aux, 0, aux.length);
			this.mode = new String(aux);
			
			in.skipBytes(1);
			
			in.close();
			byteStream.close();
			
		} catch(IOException e) {
			System.err.println("Error en la decodificación del paquete RRQ");
		}	
	}
	
	public byte[] encodeRRQ() {
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		
		byte[] aux = null;
		
		try {
			out.writeShort(this.opcode);
			out.writeBytes(this.filename);
			out.write(0);
			out.writeBytes(this.mode);
			out.write(0);
			
			aux = byteStream.toByteArray();
			byteStream.close();
			out.close();
			
		} catch(IOException e) {
			System.err.println("Error en la codificación del paquete RRQ");
		}
		
		return aux;	
	}

	// Getters y Setters
	
	public short getOpcode() {
		return opcode;
	}

	public String getFilename() {
		return filename;
	}

	public String getMode() {
		return mode;
	}

	public void setOpcode(short opcode) {
		this.opcode = opcode;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}
