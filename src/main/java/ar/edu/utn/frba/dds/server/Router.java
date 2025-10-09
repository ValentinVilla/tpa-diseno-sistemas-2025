package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.UsuariosController;
import io.javalin.Javalin;

import java.util.Map;

public class Router  {
  UsuariosController usuariosController = new UsuariosController();

  public void configure(Javalin app) {
    app.get("/", ctx -> ctx.redirect("/logueo"));
    app.get("/logueo", ctx -> {
      ctx.render("logueo.hbs", Map.of("nombre", "Tobias"));
    });

    app.get("/registrarse", ctx -> ctx.render("registro.hbs"));
    app.post("/usuarios", ctx -> usuariosController.crearUsuario(ctx));
    app.get("/usuario-creado", ctx -> ctx.render("userCreadoCorrecto.hbs"));

  }
}