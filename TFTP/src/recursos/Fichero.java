package recursos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Fichero {

	private static final int MAX_TAM = 512;
	
	// Leemos el fichero en trozos de 512 bytes
	 public static byte[] readFile(String filename, int offset) {
		 
		 File f =  new File(filename.trim());
		 FileInputStream fis;
		 byte[] fich = new byte[MAX_TAM];
		 
		 try {
			fis = new FileInputStream(f);
			fis.skip(offset);
			int s =  fis.read(fich, 0, MAX_TAM);
			if(s!= -1) fich = Arrays.copyOfRange(fich, 0, s);
			else fich = new byte[0];
			fis.close();
			
		} catch (IOException e) {
			System.err.println("Error al acceder al contenido del fichero");
			e.printStackTrace();
		}
		 
		 return fich;
	 }
	 
	 // Escribimos el contenido de los paquetes leidos en un fichero
	 public static void createFile(String filename, byte[] content) {	 
		
		File f = new File("copia_" + filename.trim());
		 try(FileOutputStream fos = new FileOutputStream(f)){
			 fos.write(content);
			 fos.close(); 
		 } catch(IOException e) {
			 System.err.println("Error al crear el fichero");
			 e.printStackTrace();
		 }
	 }

	 // Unimos en un único array el contenido de 2 arrays.
	 public static byte[] joinArrays(byte[] a1, byte[] a2) {
		 
		byte[] array = Arrays.copyOf(a1, a1.length + a2.length);
		System.arraycopy(a2, 0, array, a1.length, a2.length);
		return array;
		}
	 
	 // Limpiamos un array de bytes, equivalente al método trim de los Strings
	public static byte[] clearArray(byte[] pkg) {
		
		int i = pkg.length - 1;
		while(i>=0 && pkg[i] == 0) {
			i--;
		}
			return Arrays.copyOf(pkg, i+1);
		}
	
	// Obtenemos el número de paquetes que habra que enviar o recibir gracias al tamaño del archivo viendo si es multiplo de 512
	public static long getFileSize(String filename) {
		
	  	long num;
	  	File f =  new File(filename.trim());
	  	if(f.length() % 512 == 0) {
   		 num = (f.length() / 512) + 1;
	  	} else {
   		 num = f.length() / 512;
	  	}
	  	return num;
	}	
}


