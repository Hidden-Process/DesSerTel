package paquetes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ACK {
	
	private short opcode;
	private short blockNum;
	
	public ACK(short blockNum) {
		opcode = (short) 04;
		this.blockNum = blockNum;
	}
	
	// Decode ACK
	public ACK(byte[] msg) {
		
		ByteArrayInputStream byteStream = new ByteArrayInputStream(msg);
		DataInputStream in = new DataInputStream(byteStream);
		
		try {
			this.opcode = in.readShort();
			this.blockNum = in.readShort();
			in.close();
			byteStream.close();
		}catch(IOException e) {
			System.err.println("Error decodificando paquete ACK");
		}
	}
	
	public byte[] encodeACK() {
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		
	    byte[] aux = null;
	     
	     try {
	    	 out.writeShort(opcode);
	    	 out.writeShort(blockNum);
	    	 aux = byteStream.toByteArray();
	    	 out.close();
	    	 byteStream.close();
	     } catch(IOException e) {
	    	 System.err.println("Error en la codificación del paquete ACK");
	     }
	     
	     return aux;
	}
	
	// Getters y Setters

	public short getOpcode() {
		return opcode;
	}

	public short getBlockNum() {
		return blockNum;
	}

	public void setOpcode(short opcode) {
		this.opcode = opcode;
	}

	public void setBlockNum(short blockNum) {
		this.blockNum = blockNum;
	}
}
