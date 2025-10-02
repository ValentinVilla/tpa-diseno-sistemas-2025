package ar.edu.utn.frba.dds.server;

import io.javalin.Javalin;

import java.util.Map;

public class Router  {
  public void configure(Javalin app) {
    app.get("/", ctx -> ctx.result("Hola mundo desde Javalin"));
    app.get("/saludo", ctx -> {
      ctx.render("saludo.hbs", Map.of("nombre", "Tobias"));
    });
  }
}