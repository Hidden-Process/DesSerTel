package Servlet;

import Controlador.Matriz;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author pablo
 */
public class ComprobarTest extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //Obtener la sesión actual
        HttpSession objsesion = request.getSession(false);
        String usuario = (String)objsesion.getAttribute("username");
        if(usuario.equals("")){
            response.sendRedirect("index.html");
        }
        
        //Obtener la respuesta tipo Radio pulsada por el usuario
        String respuesta = request.getParameter("respuesta");
        if(respuesta == null){
            respuesta = "0";
        }
        int res = Integer.parseInt(respuesta);
        
       // Obtener nº de pregunta, puntuación y test.
        String posicion = request.getParameter("pregunta");
        int pos = Integer.parseInt(posicion);
        int puntos = Integer.parseInt(request.getParameter("puntuacion"));
        String test = request.getParameter("test");
        
        // Finalmente pintar la página de forma dinámica.
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            
            out.println("<head>");
            
            out.println("<meta charset=\"UTF-8\">");
            out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />");
            
            out.println("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">");
            
            out.println("<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">");
            out.println("<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>");
            out.println("<link href=\"https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap\" rel=\"stylesheet\"> ");
            
            out.println("<link rel=\"stylesheet\" href=\"css/style.css\">");
            out.println("<script type=\"text/javascript\" src=\"js/myscript.js\"></script>");
            
            out.println("<title>Comprobar Respuesta</title>");  
            
            out.println("</head>");
            
            out.println("<body>");
            
           // Comprobamos la respuesta para generar paginas diferentes según la situación
           
            if(res == Matriz.getRespuestaCorrecta(pos)){ 
                puntos++;
                out.println("<div class=\"tittle\"> <h1 class=\"correcto\">CORRECTO</h1></div>");
            } else if(res==0){
                out.println("<div class=\"tittle\"> <h1 class=\"nsnc\">No Contestada</h1></div>"); 
            } else {
                out.println("<div class=\"tittle\"> <h1 class=\"incorrecto\">INCORRECTO</h1></div>");  
            }
            
             out.println("<div class=\"box\">");
             out.println("<h2 class=\"third\">" + Matriz.getTitulo(pos) + "</h1>");
             out.println("<br>");
             out.println("<h2 class=\"third\"> Respuesta Correcta:  " +  Matriz.getRespuestaCompleta(pos, Matriz.getRespuestaCorrecta(pos)) + "</h1>");
             out.println("</div>");
             
             // Si aún quedan preguntas por responder continuamos con el test
             // En caso contrario, Si es la última pregunta finalizamos el test y mostramos la página de resultados.
             
            if(pos<4){
               out.println("<div class=\"buttons\">");
               out.println("<form action=\"ContinuarTest\" method=\"POST\">");
               out.println("<input type=\"submit\" name=\"siguiente\" value=\"Siguiente\" class='btn btn-outline-success'>");
               out.println("</div>");
            } else {
                out.println("<div class=\"buttons\">");
                out.println("<form action=\"FinalizarTest\" method=\"post\">");
                out.println("<input type=\"submit\" name=\"finalizar\" value=\"Finalizar\" class=\"btn btn-outline-danger\">");
                out.println("</div>");
             }
            
            // Pasamos a la siguiente pregunta 
            // Controlamos test, puntuación y nº de preguntas con estos hidden inputs.
            // Los incluimos dentro del formulario original para que se envie  la información que necesitamos.
            
            pos++;
            out.println("<input type=\"text\" name=\"pregunta\" value=\"" + pos + "\" hidden=\"true\">");
            out.println("<input type=\"text\" name=\"test\" value=\"" + test + "\" hidden=\"true\">");
            out.println("<input type=\"text\" name=\"puntuacion\" value=\"" + puntos + "\" hidden=\"true\">");

            out.println("</form>"); 

            // Footer
            out.println("<div class=\"autor\">");
            out.println("<hr>");
            out.println("Pablo Bellido (Universidad de Málaga) <br>");
            out.println("Copyright &copy; 2021 Todos los derechos reservados <br>");
            out.println("</div>");
            
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
