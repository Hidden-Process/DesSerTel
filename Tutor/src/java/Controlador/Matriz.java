package Controlador;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author pablo
 */
public class Matriz {
    static String [][] matriz = new String [5][5];
    
/*
*   Como pide el enunciado creamos una matriz con las preguntas a realizar
    además de las posibles respuestas y la respuesta correcta.
    Recibira la ruta donde se almacena el fichero con las preguntas por cabecera.
*/
    public static String [][] getMatriz(String testname){
        
/*
*   Montamos un Scanner para gestionar la lectura del fichero
    Además reutilizamos la clase ControlFichero para leer todo el fichero
    como un único String e ir separando las partes que nos interesan
*/
        Scanner sc;
        try{
             String file = ControlFichero.lecturaFich(testname);
             sc = new Scanner(file);
             sc.useDelimiter("\r\n");
             
             // Manejamos el movimiento en el interior de la matriz
             int fila = 0;
             int columna = 0;
             
             // Recogemos lo que necesitamos del fichero.
             while(sc.hasNext()){
                 String enunciado = sc.nextLine();
                 String correcto = sc.nextLine();
                 String primera = sc.nextLine();
                 String segunda = sc.nextLine();
                 String tercera = sc.nextLine();
                 
             // Lo almacenamos en la posición correcta de nuestra matriz.
             
             matriz[fila][columna] = enunciado;
             columna++;
             matriz[fila][columna] = correcto;
             columna++;
             matriz[fila][columna] = primera;
             columna++;
             matriz[fila][columna] = segunda;
             columna++;
             matriz[fila][columna] = tercera;
             columna = 0;
             fila++;
             }
             
             sc.close();
             
        }catch(IOException e){
            System.out.println("Error al abrir tratar el fichero " + e);
        }
        return matriz;
    }
    
    // Getters de la matriz
    
    public static String getTitulo(int fila){
        return matriz[fila][0];
    }
    
    public static int getRespuestaCorrecta(int fila){
        return Integer.parseInt(matriz[fila][1]);
    }
    
    public static String getPrimeraOpcion(int fila){
        return matriz[fila][2];
    }
    
    public static String getSegundaOpcion(int fila){
        return matriz[fila][3];
    }
    
    public static String getTerceraOpcion(int fila){
        return matriz[fila][4];
    }
    
    public static String getRespuestaCompleta(int pos,int res){
        return matriz[pos][res+1];
    }
}
