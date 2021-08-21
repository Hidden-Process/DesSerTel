package paquetes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ERROR {
	
	private short opcode;
	private short errorCode;
	private String errorMsg;
	
	public ERROR(short errorCode, String errorMsg) {
		opcode = (short) 05;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	// Decode ERROR
	public ERROR(byte[] msg) {
		
		ByteArrayInputStream byteStream = new ByteArrayInputStream(msg);
		DataInputStream in = new DataInputStream(byteStream);
		
		byte [] aux;
		
		try {
			
			this.opcode = in.readShort();
			
			this.errorCode = in.readShort();
			
			aux = new byte[msg.length-5];
			in.readFully(aux, 0, aux.length);
			this.errorMsg = new String(aux);
			
			in.close();
			byteStream.close();
			
		} catch(IOException e) {
			System.err.println("Error decodificando el paquete ERROR");
		}
	}
	
	public byte[] encodeERROR() {
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		
		byte[] aux = null;
		
		try {
			out.writeShort(this.opcode);
			out.writeShort(this.errorCode);
			out.writeBytes(this.errorMsg);
			out.write(0);
			
			aux = byteStream.toByteArray();
			out.close();
			byteStream.close();
			
		} catch(IOException e) {
			System.err.println("Error en la codificación del paquete ERROR");
		}
		
		return aux;
	}

	// Getters y Setters
	public short getOpcode() {
		return opcode;
	}

	public short getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setOpcode(short opcode) {
		this.opcode = opcode;
	}

	public void setErrorCode(short errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
