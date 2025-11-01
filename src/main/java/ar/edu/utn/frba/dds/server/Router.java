package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.ColeccionesController;
import ar.edu.utn.frba.dds.controllers.HechosController;
import ar.edu.utn.frba.dds.controllers.SesionesController;
import ar.edu.utn.frba.dds.controllers.UsuariosController;
import io.javalin.Javalin;

public class Router {
  UsuariosController usuariosController = new UsuariosController();
  ColeccionesController coleccionesController = new ColeccionesController();
  HechosController hechosController = new HechosController();
  SesionesController sesionesController = new SesionesController();

  public void configure(Javalin app) {
    // --- Home ---
    app.get("/", ctx -> ctx.redirect("/home"));
    app.get("/home", sesionesController::mostrarHome);

    // --- Sesiones (Login) ---
    app.get("/login", sesionesController::mostrarLogin);
    app.get("/logout", sesionesController::cerrarSesion);
    app.post("/login", sesionesController::iniciarSesion);

    // --- Registro (Usuarios) ---
    app.get("/register", usuariosController::mostrarRegistro);
    app.post("/register", usuariosController::crearUsuario);

    // --- Hechos ---
    app.get("/hechos.json", hechosController::mostrarHechosMapa);
    app.get("/hechos", hechosController::mostrarHechos);
    app.get("/hechos/nuevo", hechosController::mostrarFormularioNuevoHecho);
    app.post("/hechos", hechosController::crearHecho);
    app.get("/hechos/delete", hechosController::mostrarFormularioEliminacion);
    app.post("/hechos/delete", hechosController::crearSolicitudEliminacion);

    // --- Colecciones ---
    app.get("/colecciones", ctx -> ctx.render("colecciones.hbs", coleccionesController.mostrarColecciones(ctx)));
    app.get("/colecciones/{id}", coleccionesController::mostrarColeccionPorId);
    app.get("/colecciones/{id}/hechos.json", coleccionesController::mostrarHechosColeccionJson);
  }
}