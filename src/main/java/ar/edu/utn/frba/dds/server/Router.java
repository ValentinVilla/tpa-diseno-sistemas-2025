package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.ColeccionesController;
import ar.edu.utn.frba.dds.controllers.SesionesController;
import ar.edu.utn.frba.dds.controllers.UsuariosController;
import io.javalin.Javalin;

import java.util.Map;

public class Router  {
  UsuariosController usuariosController = new UsuariosController();
  ColeccionesController coleccionesController = new ColeccionesController();
  //SesionesController sesionesController = new SesionesController(); todo:si lo pongo rompe todo

  public void configure(Javalin app) {
    app.get("/", ctx -> ctx.redirect("/home"));
    app.get("/home", ctx -> {
      ctx.render("home.hbs", Map.of("nombre", "Tobias"));
    });

    app.get("/login", ctx -> ctx.render("login.hbs", Map.of("nombre", "Tobias")));
    app.get("/logueo", ctx -> ctx.render("login.hbs"));
    //app.post("/login", ctx -> ctx.render("helloworld"));
    app.get("/register", ctx -> ctx.render("register.hbs"));
    app.post("/register", ctx -> usuariosController.crearUsuario(ctx));
    //app.post("/usuarios", ctx -> usuariosController.crearUsuario(ctx)); todo: implementar con repo y todo
    app.get("/colecciones", ctx -> ctx.render("colecciones.hbs", coleccionesController.mostrarColecciones()));
  }
}