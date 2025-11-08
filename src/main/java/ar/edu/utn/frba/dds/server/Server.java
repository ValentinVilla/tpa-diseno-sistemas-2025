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

  private static final String UPLOADS_DIR_PROP = System.getProperty("UPLOADS_DIR",
      System.getenv().getOrDefault("UPLOADS_DIR", "uploads"));

  private static final int DEFAULT_PORT = 9001;

  public void start() {
    System.out.println("Server.start(): inicio del método");

    try {
      Path uploads = Paths.get(UPLOADS_DIR_PROP);
      if (!Files.exists(uploads)) {
        Files.createDirectories(uploads);
      }
      System.out.println("Server.start(): carpeta uploads OK -> " + uploads.toAbsolutePath());
    } catch (Exception e) {
      System.err.println("Server.start(): ERROR creando carpeta uploads");
      e.printStackTrace();
      throw new RuntimeException("No se pudo crear la carpeta de uploads: " + UPLOADS_DIR_PROP, e);
    }

    System.out.println("Server.start(): creando instancia Javalin...");
    var app = Javalin.create((JavalinConfig config) -> {
      initializeStaticFiles(config);
      initializeTemplating(config);
    });
    System.out.println("Server.start(): Javalin creada");

    System.out.println("Server.start(): configurando Router...");
    try {
      new Router().configure(app);
      System.out.println("Server.start(): Router configurado correctamente");
    } catch (Throwable e) {
      System.err.println("Server.start(): ERROR durante Router.configure(app)");
      e.printStackTrace();
      throw e;
    }

    int port = resolvePort();
    System.out.println("Server.start(): puerto resuelto = " + port);

    try {
      System.out.println("Server.start(): llamando app.start(" + port + ") ...");
      app.start(port);
      System.out.println("Servidor iniciado en http://localhost:" + port);
      System.out.println("Uploads folder: " + UPLOADS_DIR_PROP + " -> served at /uploads");
    } catch (Throwable e) {
      System.err.println("Server.start(): ERROR al invocar app.start()");
      e.printStackTrace();
      throw new RuntimeException("Fallo al iniciar Javalin", e);
    }

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        System.out.println("Deteniendo servidor...");
        app.stop();
      } catch (Exception ignored) {}
    }));

    System.out.println("Server.start(): fin del método");
  }

  private int resolvePort() {
    String portEnv = System.getenv("PORT");
    if (portEnv == null || portEnv.isBlank()) {
      portEnv = System.getProperty("PORT");
    }
    if (portEnv != null && !portEnv.isBlank()) {
      try {
        return Integer.parseInt(portEnv.trim());
      } catch (NumberFormatException e) {
        System.err.println("Valor de PORT inválido: '" + portEnv + "'. Usando puerto por defecto " + DEFAULT_PORT);
      }
    }
    return DEFAULT_PORT;
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
