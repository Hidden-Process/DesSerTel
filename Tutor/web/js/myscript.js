// Comprobamos que los campos del login estan rellenos.
function validacion_login(){
    var user = document.getElementById("username").value;
    var pass = document.getElementById("pass").value;
    
    if(user.length === 0 && pass.length === 0){
        document.getElementById("username").style="border: 2px solid red";
        document.getElementById("pass").style="border: 2px solid red";
        return false;
    }
    
    if(user.length === 0){
        document.getElementById("username").style="border: 2px solid red";
        return false;
    }
    
    if(pass.length === 0){
        document.getElementById("pass").style="border: 2px solid red";
        return false;
    }
    
    return true;
}

function validacion_registro(){
    var user = document.getElementById("username").value;
    var pass = document.getElementById("pass").value;
    var pass2 = document.getElementById("pass2").value;
    var email = document.getElementById("mail").value;
    var phone = document.getElementById("phone").value;
    
        // Comprobar que ningún campo queda vacío
      if(user.length === 0 || pass.length === 0 || pass2.length === 0  ||  mail.length === 0 || birth.length === 0 ||phone.length === 0){
        alert("Algunos campos no han sido rellenados");
        return false;
    }
    
     // Comprobar que las contraseñas coinciden
    if(pass !== pass2){
        alert("Las contraseñas deben coincidir");
        return false;
    }
    
    // Comprobar que la direccion de email dada es valida.
    // Fuente de la regex: https://www.w3resource.com/javascript/form/email-validation.php
    
    var mailformat = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
    if(!mailformat.test(email)){
       document.getElementById('email').style="border: 2px solid red";
       return false;
    }
    
    // Comprobar que el telefono dado es valido
    // Regex de 9 digitos.
    var phoneformat = /^\d{9}$/;
    if(!phoneformat.test(phone)){
       document.getElementById('telf').style="border: 2px solid red";
       return false;
    }
    
    
    return true;
    
}

// Obtenemos la fecha de la ultima modificación del documento
function fecha(){
    var ultima_modificacion = document.lastModified;
    document.getElementById("modificacion").innerHTML = ultima_modificacion;
}

// Mostramos el gif que será distinto respecto a la puntuacion obtenida en el test.
function showGif(){
    var puntos = document.getElementById('puntos').value;
    if(puntos>3){
      document.getElementById('gif').src = 'img/aprobado.gif';
    } else{
      document.getElementById('gif').src = 'img/suspenso.gif';
    }
}


