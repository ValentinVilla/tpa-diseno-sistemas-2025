package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.server.templates.JavalinHandlebars;
import ar.edu.utn.frba.dds.server.templates.JavalinRenderer;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Server {

  private static final String UPLOADS_DIR_PROP = System.getProperty("UPLOADS_DIR", System.getenv().getOrDefault("UPLOADS_DIR", "uploads"));

  public void start() {
    try {
      Path uploads = Paths.get(UPLOADS_DIR_PROP);
      if (!Files.exists(uploads)) {
        Files.createDirectories(uploads);
      }
    } catch (Exception e) {
      throw new RuntimeException("No se pudo crear la carpeta de uploads: " + UPLOADS_DIR_PROP, e);
    }

    var app = Javalin.create(config -> {
      initializeStaticFiles(config);
      initializeTemplating(config);
    });

    new Router().configure(app);

    app.start(9001);
    System.out.println("Servidor iniciado en http://localhost:9001");
    System.out.println("Uploads folder: " + UPLOADS_DIR_PROP + " -> served at /uploads");
  }

  private void initializeTemplating(JavalinConfig config) {
    config.fileRenderer(new JavalinRenderer().register("hbs", new JavalinHandlebars()));
  }

  private static void initializeStaticFiles(JavalinConfig config) {
    config.staticFiles.add(staticFileConfig -> {
      staticFileConfig.hostedPath = "/";
      staticFileConfig.directory = "/public";
      staticFileConfig.location = Location.CLASSPATH;
    });
    config.staticFiles.add(staticFileConfig -> {
      staticFileConfig.hostedPath = "/uploads";
      staticFileConfig.directory = UPLOADS_DIR_PROP;
      staticFileConfig.location = Location.EXTERNAL;
    });
  }
}
