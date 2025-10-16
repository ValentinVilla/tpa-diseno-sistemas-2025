package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.repositorios.RepositorioEstadisticas;
import io.javalin.Javalin;


import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Main {

  public static void main(String[] args) {

//    RepositorioEstadisticas repo = RepositorioEstadisticas.getInstancia();
//
//    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//
//    scheduler.scheduleAtFixedRate(() -> {
//      try {
//        System.out.println("Refrescando vistas materializadas...");
//        repo.refrescarTodas();
//        System.out.println("Refresco completado.");
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//    }, 0, 1, TimeUnit.MINUTES);
//
//    System.out.println("Scheduler iniciado. Presiona Ctrl+C para detener.");


    /*Javalin app = Javalin.create().start(7000);
    //app.get( "/", ctx -> ctx.result("Servidor corriendo"));
    *//*app.get("/", ctx -> ctx.html("<h1>Servidor corriendo</h1>"
        + "<p>Esta es nuestra primera respuesta HTML</p>"));*//*
    app.get("/", ctx -> {
      String nombre = ctx.queryParam("nombre");
      if (nombre == null) {
        nombre = "invitado";
      }
      ctx.html("<h1>Hola, " + nombre + "!</h1>");
    });

    //si tiro en el browser http://localhost:7000/?nombre=tobias la pagina tira Hola Tobias
    //si no le paso un numbre tira Hola invitado
    */

    /*Javalin app = Javalin.create(config -> {
      config.fileRenderer(new JavalinHandlebars());
    }).start(7000);

    app.get("/", ctx -> {
      String nombre = ctx.queryParam("nombre");
      if (nombre == null) {
        nombre = "invitado";
      }
      ctx.render("logueo.hbs", Map.of("nombre", nombre));
    });*/


  }
}

