package Controlador;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author pablo
 */
public class ControlFichero {
    
 /* 
 * Leemos el fichero cuya ruta nos entra por cabecera desde el contexto de servlet.
    Devolvemos el contenido del fichero en un string que ha sido procesado linea a linea.
    Usaremos este método tanto para leer los usuarios registrados en su respectivo 
    fichero como para leer las preguntas de los distintos ficheros de los test 
    para introducirlos en la matriz posteriormente.
 */
    
    public static String lecturaFich(String name) throws FileNotFoundException, IOException{
        String cadena;
        
        FileReader fr = new FileReader(name);
        BufferedReader bf = new BufferedReader(fr);
        StringBuilder sb = new StringBuilder();
        
        while((cadena = bf.readLine()) != null){
            sb.append(cadena + "\r\n");
        }
        fr.close();
        bf.close();
        return sb.toString();
    }
    
 /* 
 * Método que usaremos para escribir en el fichero de usuarios registrados los 
    usuarios que se vayan registrando. Le entran por cabecera el nombre de usuario
    y contraseña, además de la ruta del fichero obtenida por el contexto del servlet.
 */
    
    public static void escribirFich(String user, String pass, String name){
        
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(name,true))) {
            pw.println(user + " " + pass);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } 
    }
}

