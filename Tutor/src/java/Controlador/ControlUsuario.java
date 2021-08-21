package Controlador;

import java.util.Scanner;

/**
 *
 * @author pablo
 */
public class ControlUsuario {
  
/* 
 * Método que utilizaremos para comprobar si un usuario esta auntenticado en nuestra
    aplicación web, es decir si estaba previamente registrado.
    Buscamos en el contenido del fichero, cuya ruta nos entra por cabecera,
    si el usuario y contraseña dados están en dicho fichero.
    El formato que usamos es (usuario) (espacio) (contraseña) y asi montamos
    el scanner correspondiente
*/
    public boolean autenticacion(String username, String pass, String path) throws Exception{
     
        boolean flag = false;
        
        String file = ControlFichero.lecturaFich(path);
        
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\r\n");
        
        while(sc.hasNext() && flag == false){
           String line = sc.nextLine();
           
           String[] words = line.split(" ");

           String u = words[0];
           String p = words[1];

           if(username.equalsIgnoreCase(u) && (pass.equalsIgnoreCase(p))){
               flag = true;
           }
        }
        
        sc.close();
     
        return flag;
    }
    
/* 
 * Método que utilizaremos para registrar usuarios en nuestro fichero, en caso de 
    que no estuvieran previamente en él.

*/
    public boolean registrar(String username, String pass, String path) throws Exception{
        if(!autenticacion(username, pass, path)){
           ControlFichero.escribirFich(username, pass, path);
           return true;
        }
        return false;
    }  
}
