<%-- 
    Document   : Tests
    Created on : 14-jul-2021, 16:38:08
    Author     : pablo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- 
Obtenemos la sesion
--%>
<%

    HttpSession objsesion = request.getSession(false);
    String usuario = (String)objsesion.getAttribute("username");
    if(usuario.equals("")){
        response.sendRedirect("index.html");
    }
    
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        
        <title>Tests</title>
        
         <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">

        <!-- custom css file -->
        <link rel="stylesheet" href="css/style.css">
        
        <!-- Google Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet"> 
        
        <!-- custom js file -->
        <script type="text/javascript" src="js/myscript.js"></script>
        
        <!-- Boostrap JS files enabling dropdown menu -->
        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    </head>
    
    <body>
        
         <!-- Navbar Section -->
        <div class="text-right">
            <div class="dropdown" style="right: 0; left: auto;">
            <button class="btn btn-primary dropdown-toggle" type="button" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> 
            <% out.println(usuario);%> </button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenu2">
            <form method="POST" action="CerrarSesion"> <button class="dropdown-item" type="button" onClick='window.location.href="index.html";'>Cerrar sesión</button></form> 
            </div>
            </div> 
        </div>
        
            
         <!-- Content Section -->  
        <div class="tittle">
            <h2 class="second">Test Disponibles</h2>
        </div>
        
        <div class="box">
     
            <!-- Tarjeta Izquierda --> 
        <div id="st-box">
          
            <div class="card text-white bg-success mb-3" style="max-width: 18rem;">
            <div class="card-header">Test 1</div>
            <div class="card-body">
            <h5 class="card-title">Fácil</h5>
            <p class="card-text">Este test es una toma contacto con la materia, consta de preguntas sencillas.</p>
            <form action="EmpezarTest" method="POST">
                <center><input type="submit" name="test" value="Test 1" class="btn btn-primary"></center>
            </form>
            </div>
            </div>
            
        </div>
        
            <!-- Tarjeta Central --> 
        <div id="nd-box">
            
            <div class="card text-white bg-warning mb-3" style="max-width: 18rem;">
            <div class="card-header">Test 2</div>
            <div class="card-body">
            <h5 class="card-title">Medio</h5>
            <p class="card-text">Este test contiene preguntas típicas de examen para saber lo que te encontrarás</p>
             <form action="EmpezarTest" method="POST">
                <center><input type="submit" name="test" value="Test 2" class="btn btn-primary"></center>
            </form>
            </div>
            </div> 
            
        </div>
        
            <!-- Tarjeta Derecha --> 
        <div id="rd-box">
           
            <div class="card text-white bg-danger mb-3" style="max-width: 18rem;">
            <div class="card-header">Test 3</div>
            <div class="card-body">
            <h5 class="card-title">Difícil</h5>
            <p class="card-text">Este test esta construido para ser un reto que te prepara para el examen</p>
             <form action="EmpezarTest" method="POST">
                <center><input type="submit" name="test" value="Test 3" class="btn btn-primary"></center>
            </form>
            </div>
            </div>
            
        </div>
          
    </div>  
         
        <!-- Footer Section -->
        <div class="autor">
        <hr>
        Pablo Bellido (Universidad de Málaga) <br>
        Copyright &copy; 2021 Todos los derechos reservados <br>
        </div>
        
    </body>
    
</html>
