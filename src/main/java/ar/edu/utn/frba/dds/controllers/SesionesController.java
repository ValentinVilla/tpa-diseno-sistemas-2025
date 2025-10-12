package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;
import ar.edu.utn.frba.dds.repositorios.RepositorioUsuarios;

public class SesionesController {
    RepositorioUsuarios repositorioUsuarios;

    public void mostrar(Context ctx) {
        Map<String, Object> modelo = new HashMap<>();
        if("true".equals(ctx.queryParam("error"))) {
            modelo.put("error", "usuario o contrasenia incorrectos");
        }
        ctx.render("login.hbs", modelo);
    }


    public void crear(Context ctx){
        try{
            var usuario = repositorioUsuarios.getInstancia().buscarUsuario(
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

    public SesionesController(){
        this.repositorioUsuarios = RepositorioUsuarios.getInstancia();
    }

}
