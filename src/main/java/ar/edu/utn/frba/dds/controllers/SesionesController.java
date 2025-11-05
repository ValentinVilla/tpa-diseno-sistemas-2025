package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

import ar.edu.utn.frba.dds.repositorios.RepositorioUsuarios;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.helpers.SesionHelper;


public class SesionesController {

  public void mostrarHome(Context ctx) {
    Map<String, Object> model = SesionHelper.crearModeloBase(ctx);
    SesionHelper.guardarReturnTo(ctx, ctx.path());
    ctx.render("home.hbs", model);
  }

  public void mostrarLogin(Context ctx) {
    Contribuyente usuario = SesionHelper.obtenerUsuario(ctx);
    if (usuario != null) {
      ctx.redirect("/home");
      return;
    }

    Map<String, Object> model = new HashMap<>();
    if ("true".equals(ctx.queryParam("error"))) {
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
        SesionHelper.guardarUsuario(ctx, usuario);
        SesionHelper.redirigirPostLogin(ctx);
      } else {
        ctx.redirect("/login?error=true");
      }
    } catch (Exception e) {
      ctx.redirect("/login?error=true");
    }
  }

  public void cerrarSesion(Context ctx) {
    SesionHelper.guardarUsuario(ctx, null);
    ctx.redirect("/");
  }
}
