package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;
import ar.edu.utn.frba.dds.repositorios.RepositorioUsuarios;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;

public class SesionesController {

    public void mostrarHome(Context ctx) {
        Contribuyente usuario = ctx.sessionAttribute("usuario_logueado");
        Map<String, Object> model = new HashMap<>();
        if(usuario != null) {
            model.put("nombre", usuario.getNombre());
        }
        ctx.render("home.hbs", model);
    }

    public void mostrarLogin(Context ctx) {
        Map<String, Object> model = new HashMap<>();
        if("true".equals(ctx.queryParam("error"))) {
            model.put("error", "Usuario o contraseña incorrectos");
        }
        ctx.render("login.hbs", model);
    }

    public void iniciarSesion(Context ctx) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            Contribuyente usuario = RepositorioUsuarios.getInstancia().buscarPorEmail(email);

            if (usuario != null && usuario.getPassword().equals(password)) {
                ctx.sessionAttribute("usuario_logueado", usuario);
                ctx.redirect("/home");
            } else {
                ctx.redirect("/login?error=true");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.redirect("/login?error=true");
        }
    }

    public void cerrarSesion(Context ctx) {
        ctx.sessionAttribute("usuario_logueado", null);
        ctx.redirect("/login");
    }

    /*
    public void crear(Context ctx){
        try{
            var usuario = RepositorioUsuarios.getInstancia().buscarUsuario(
                ctx.formParam("nombre"),
                ctx.formParam("contrasenia")
            );

        }catch(Exception e){
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("error", "usuario o contrasenia incorrectos");
            //ctx.render("login.hbs", modelo); esto no conviene mejor redirijir pq me va a repetir el post del form cosa q no quiero pq produce el efecto duplicado
            ctx.redirect("/loguin?error=true");
        }//hay otra opcion mas...
    }
    */

}
