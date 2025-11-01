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
        ctx.sessionAttribute("returnTo", ctx.path());
        ctx.render("home.hbs", model);
    }

    public void mostrarLogin(Context ctx) {
        if(ctx.sessionAttribute("usuario_logueado") != null) {
            ctx.redirect("/home");
            return;
        }
        
        Map<String, Object> model = new HashMap<>();
        if("true".equals(ctx.queryParam("error"))) {
            model.put("error", "Usuario o contraseña incorrectos");
        }
    }

    public void iniciarSesion(Context ctx) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            Contribuyente usuario = RepositorioUsuarios.getInstancia().buscarPorEmail(email);

            if (usuario != null && usuario.getPassword().equals(password)) {
                ctx.sessionAttribute("usuario_logueado", usuario);
                // revisa si hay una URL de retorno guardada
                String returnTo = ctx.sessionAttribute("returnTo");
                if (returnTo != null && !returnTo.isEmpty()) {
                    //Si la hay,la borra de la sesión (para no reusarla xd)
                    ctx.sessionAttribute("returnTo", null);
                    ctx.redirect(returnTo);
                } else {
                    // 4. Si no la hay,te manda al home (comportamiento por defecto)
                    ctx.redirect("/home");
                }
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
        ctx.redirect("/");
    }

}
