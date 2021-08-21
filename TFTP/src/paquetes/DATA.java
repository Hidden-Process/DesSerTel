package paquetes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DATA {
	
	private short opcode;
	private short blockNum;
	private byte[] data;
	
	public DATA(short blockNum, byte[] data) {
		opcode = (short) 03;
		this.blockNum = blockNum;
		this.data = data;
	}
	
	// Decode DATA
	public DATA(byte[] msg) {
		
		ByteArrayInputStream byteStream = new ByteArrayInputStream(msg);
		DataInputStream in = new DataInputStream(byteStream);
		
		byte[] aux;
		
		try {
			this.opcode = in.readShort();
			this.blockNum = in.readShort();
			aux = new byte[msg.length-4];
			in.readFully(aux, 0, aux.length);
			this.data = aux;
			byteStream.close();
			in.close();
			
		} catch(IOException e) {
			System.err.println("Error en la decodificación del paquete DATA");
		}
	}
	
	public byte[] encodeDATA() {
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteStream);
		
		byte [] aux = null;
		
		try {
			out.writeShort(opcode);
			out.writeShort(blockNum);
			out.write(data);
			
			aux = byteStream.toByteArray();
			out.close();
			byteStream.close();
			
		} catch(IOException e) {
			System.err.println("Error en la codificación del paqute DATA");
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

	public byte[] getData() {
		return data;
	}

	public void setOpcode(short opcode) {
		this.opcode = opcode;
	}

	public void setBlockNum(short blockNum) {
		this.blockNum = blockNum;
	}

	public void setData(byte[] data) {
		this.data = data;
	}	
}
