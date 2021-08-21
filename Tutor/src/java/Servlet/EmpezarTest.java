package Servlet;

import Controlador.Matriz;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author pablo
 */
public class EmpezarTest extends HttpServlet {

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
        
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // Obtener sesión
        HttpSession objsesion = request.getSession(false);
        String usuario = (String)objsesion.getAttribute("username");
        if(usuario.equals("")){
            response.sendRedirect("index.html");
        }
        
        // Obtener Test seleccionado
        
        String testname;
        
        switch(request.getParameter("test")){
            case "Test 1" : testname = "test1.txt";
            break;
            case "Test 2" : testname = "test2.txt";
            break;
            default : testname = "test3.txt";
        }
        
        // Conseguimos el path donde está el fichero con las preguntas
        
        ServletContext application = getServletContext();
        String path = application.getRealPath("/files/" + testname);
        Matriz.getMatriz(path);
        
        // Pintamos la página
        
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
            
            out.println("<title>Realizar Test</title>");  
            
            out.println("</head>");
            
            out.println("<body>");

            // Seccion Navbar superior para cancelar el test o cerrar la sesion.
            
            out.println("<nav class=\"navbar navbar-custom justify-content-end\">");
            out.println("<form class=\"form-inline\">");
            out.println("<form id=\"cancelartest\" method=\"POST\" action=\"test.jsp\"></form>");
            out.println("<button class=\"btn btn-outline-danger mr-3\" type=\"button\" form=\"cancelartest\" onclick='window.location.href=\"test.jsp\";'> Cancelar </button>");
            out.println("<form id=\"cierresesion\" method=\"POST\" action=\"CerrarSesion\"></form>");
            out.println("<button class=\"btn btn-outline-danger mr-3\" type=\"button\" form=\"cierresesion\" onclick='window.location.href=\"index.html\";'\"> Cerrar Sesión </button>");
            out.println("</form>");
            out.println("</nav>");
            
            
            // Contenido
            out.println("<div class=\"tittle\"> <h1 class=\"second\">" +  Matriz.getTitulo(0) + "</h1></div>");
            
            out.println("<div class=\"box\">");
            
            out.println("<form method=\"POST\" action=\"ComprobarTest\">");
            
            // Mostramos las posible soluciones en una tabla para una mejor visualizacion
            out.println("<table>");
            out.println("<tr>");
            out.println("<th><input type=\"radio\" name=\"respuesta\" value=\"1\"></th>");
            out.println("<td>" + Matriz.getPrimeraOpcion(0) + "</td>");
            out.println("</tr>");
            out.println("</table>");
            
            out.println("<table>");
            out.println("<tr>");
            out.println("<th><input type=\"radio\" name=\"respuesta\" value=\"2\"></th>");
            out.println("<td>" + Matriz.getSegundaOpcion(0) + "</td>");
            out.println("</tr>");
            out.println("</table>");
            
            out.println("<table>");
            out.println("<tr>");
            out.println("<th><input type=\"radio\" name=\"respuesta\" value=\"3\"></th>");
            out.println("<td>" + Matriz.getTerceraOpcion(0)+ "</td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("<br>");
            
            out.println("<center><input type=\"submit\" name=\"submit\" value=\"Siguiente\" class=\"btn btn-success\"></center>");
            
            // Usaremos lo siguinete para llevar la cuenta de preguntas, test y puntuacion
            // Tiene que ir dentro del form original para que se envia dicha informacion correctamente.
            // Pondremos los input como hiddens ya que no es necesario que el usuario los vea.
            
            out.println("<input type=\"text\" name=\"pregunta\" value=\"0\" hidden=\"true\">");
            out.println("<input type=\"text\" name=\"test\" value='" + testname + "' hidden=\"true\">");
            out.println("<input type=\"text\" name=\"puntuacion\" value=\"0\" hidden=\"true\">");
            
            out.println("</form>");
            
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
