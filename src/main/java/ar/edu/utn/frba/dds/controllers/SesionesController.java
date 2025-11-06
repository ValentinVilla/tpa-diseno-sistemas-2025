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
      Contribuyente usuario = SesionHelper.authenticateByEmailAndPassword(email, password);

      if (usuario != null) {
        SesionHelper.guardarUsuario(ctx, usuario);
        ctx.sessionAttribute("login_error", null);
        ctx.sessionAttribute("openLogin", null);
        SesionHelper.redirigirPostLogin(ctx);
      } else {
        ctx.sessionAttribute("login_error", "Correo o contraseña incorrectos. Verificá tus datos e intentá nuevamente.");
        ctx.sessionAttribute("openLogin", true);

        String referer = ctx.header("Referer");
        ctx.redirect(referer != null ? referer : "/");
      }
    } catch (Exception e) {
      ctx.sessionAttribute("login_error", "Ha ocurrido un error al intentar iniciar sesión. Por favor intentá más tarde.");
      ctx.sessionAttribute("openLogin", true);
      String referer = ctx.header("Referer");
      ctx.redirect(referer != null ? referer : "/");
    }
  }

  public void cerrarSesion(Context ctx) {
    SesionHelper.guardarUsuario(ctx, null);
    ctx.redirect("/");
  }
}
