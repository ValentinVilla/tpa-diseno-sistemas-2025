package ar.edu.utn.frba.dds.server;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.Map;

public class Router {
    public void configure(Javalin app) {
        app.get("/", ctx -> ctx.result("Pagina principal"));
        app.get("/saludo", ctx -> {
            ctx.render("src/main/resources/templates/saludo.hbs", Map.of("nombre", "Tobias"));
        }); //Esto busca la plantilla saludo.hbs, reemplaza la variable nombre por Tobias y envía el HTML resultante al navegador.
        //UsuarioController controller = new UsuarioController();

        //app.get("/users/random", ctx -> ctx.json(controller.randomUser()));
        //app.get("/users/random", ctx -> ctx.json("hola"));

    }
}