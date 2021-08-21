package Servlet;

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
public class FinalizarTest extends HttpServlet {

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
        
        // Obtenemos la sesión para mostrar el nombre de usuario junto a los
        // resultados para darle un punto de personalización extra.
        HttpSession objsesion = request.getSession(false);
        String usuario = (String)objsesion.getAttribute("username");
        if(usuario.equals("")){
            response.sendRedirect("index.html");
        }
       
        // Obtenemos la puntuación para calcular los parametros que necesitamos
        // Aciertos, fallos y porcentajes.
        int puntos = Integer.parseInt(request.getParameter("puntuacion"));
        int porcentaje = (puntos*100)/5;
        int fallos = 5-puntos;
        
        
        // Generamos dinamicamente la página
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
            
            out.println("<title>Puntuación</title>");    
            
            out.println("</head>");
            
            out.println("<body onload=\"showGif()\">");
            
             // Seccion Navbar superior para cancelar el test o cerrar la sesion.
            
            out.println("<nav class=\"navbar navbar-custom justify-content-end\">");
            out.println("<form class=\"form-inline\">");
            out.println("<form id=\"cancelartest\" method=\"POST\" action=\"test.jsp\"></form>");
            out.println("<button class=\"btn btn-outline-success mr-3\" type=\"button\" form=\"cancelartest\" onclick='window.location.href=\"test.jsp\";'> Seleccionar Test </button>");
            out.println("<form id=\"cierresesion\" method=\"POST\" action=\"CerrarSesion\"></form>");
            out.println("<button class=\"btn btn-outline-danger mr-3\" type=\"button\" form=\"cierresesion\" onclick='window.location.href=\"index.html\";'\"> Cerrar Sesión </button>");
            out.println("</form>");
            out.println("</nav>");
        
            out.println("<div class=\"tittle\">");
            out.println("<h1 class=\"main\"> Resultados obtenidos </h1>");
            out.println("</div>");
            
            out.println("<div class=\"box\">");

            if(porcentaje<=33){
                out.println("<h2 class=\"third\"> Suspenso " + usuario + ", no se te aconseja presentarte al examen </h2>");
            } else if(porcentaje<=66){
                out.println("<h2 class=\"third\"> Nota baja " + usuario + ", necesitas estudiar más </h2>");
            } else {
                out.println("<h2 class=\"third\"> Felicidades " + usuario + ", estás preparado para el examen </h2>");
            }
            
            out.println("<h2 class=\"third\">Tu porcentaje de aciertos es del: " + porcentaje + "%</h2>");        
            out.println("<h2 class=\"third\">Has acertado: " + puntos + " preguntas</h2>");
            out.println("<h2 class=\"third\">Has fallado: " + fallos + " preguntas</h2>");
            
            
            out.println("<input id=\"puntos\" type=\"text\" name=\"puntos\" value=\"" + puntos + "\" hidden=\"true\"></input>");
            out.println("<img id=\"gif\">");
      
            
            out.println("</div>");
            
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
