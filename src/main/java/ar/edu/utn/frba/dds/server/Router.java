package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.SesionesController;
import ar.edu.utn.frba.dds.controllers.UsuariosController;
import io.javalin.Javalin;

import java.util.Map;

public class Router  {
  UsuariosController usuariosController = new UsuariosController();
  //SesionesController sesionesController = new SesionesController(); todo:si lo pongo rompe todo

  public void configure(Javalin app) {
    app.get("/", ctx -> ctx.redirect("/home"));
    app.get("/home", ctx -> {
      ctx.render("home.hbs", Map.of("nombre", "Tobias"));
    });

    app.get("/loguin", ctx -> ctx.render("loguin.hbs"));
    app.post("/loguin", ctx -> usuariosController.crearUsuario(ctx));
    app.get("/loguin/registro", ctx -> ctx.render("registro.hbs"));
    //app.post("/usuarios", ctx -> usuariosController.crearUsuario(ctx)); todo: implementar con repo y todo
  }
}