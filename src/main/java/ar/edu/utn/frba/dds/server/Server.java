package ar.edu.utn.frba.dds.server;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import io.javalin.rendering.template.JavalinHandlebars;


public class Server {
    public void start() {
        //Bootstrap.init();
        var app = Javalin.create(config -> { //crea la app web
            config.jsonMapper(new JavalinJackson().updateMapper(mapper -> {
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                mapper.registerModule(new Jdk8Module());
            }));
            config.fileRenderer(new JavalinHandlebars());
        });

        //-----MIDDLEWARES SE EXEC ANTES DE TODAS LAS REQ O DE CIERTOS PATH----------
        app.before(ctx -> {
            // runs before all requests
        });
        app.before("/path/*", ctx -> {
            // runs before request to /path/*
        });
        new Router().configure(app);
        app.start(7000);
    }
}
