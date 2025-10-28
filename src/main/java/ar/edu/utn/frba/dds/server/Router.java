package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.ColeccionesController;
import ar.edu.utn.frba.dds.controllers.HechosController;
import ar.edu.utn.frba.dds.controllers.SesionesController;
import ar.edu.utn.frba.dds.controllers.UsuariosController;
import ar.edu.utn.frba.dds.controllers.AdminSolicitudesController;
import ar.edu.utn.frba.dds.controllers.AdminController;
import io.javalin.Javalin;

import java.util.Map;

public class Router {
  UsuariosController usuariosController = new UsuariosController();
  ColeccionesController coleccionesController = new ColeccionesController();
  HechosController hechosController = new HechosController();
  SesionesController sesionesController = new SesionesController();
  AdminSolicitudesController adminSolicitudesController = new AdminSolicitudesController();
  AdminController adminController = new AdminController();

  public void configure(Javalin app) {
    // --- Home ---
    app.get("/", ctx -> ctx.redirect("/home"));
    app.get("/home", sesionesController::mostrarHome);

    // --- Sesiones (Login) ---
    app.get("/login", sesionesController::mostrarLogin);
    app.get("/logueo", sesionesController::mostrarLogin);
    app.get("/logout", sesionesController::cerrarSesion);
    app.post("/login", sesionesController::iniciarSesion);

    // --- Registro (Usuarios) ---
    app.get("/register", usuariosController::mostrarRegistro);
    app.post("/register", usuariosController::crearUsuario);

    // --- Hechos ---
    app.get("/hechos", ctx -> ctx.render("hechos.hbs", hechosController.mostrarHechos(ctx)));
    app.get("hechosMapa", ctx -> hechosController.hechosParaMapa(ctx));
    app.get("/hechos/nuevo", hechosController::mostrarFormularioNuevoHecho);
    app.post("/hechos", hechosController::crearHecho);  // un visualizador puede crear hechos sin estar log

    // --- Colecciones ---
    app.get("/colecciones", ctx -> ctx.render("colecciones.hbs", coleccionesController.mostrarColecciones()));

    // --- Rutas Admin ---
    app.get("/admin", adminController::mostrarAdmin);
    app.get("/admin/solicitudes", adminSolicitudesController::mostrarPanel);
    app.get("admin/colecciones/nueva", adminController::mostarCrearColeccion); //posiblemente en las que son solamente mostrar una pestaña no haces adminController sino que metes un ctx render y chau por eso pasar la logica de adminSlocitudes a un Solicitudes Controller o directamente a Admin Controller
    //app.post("/admin/colecciones", adminController::crearColeccion);
    // Endpoints provisorios para aprobar/rechazar (más adelante pasar a POST y validar
    app.post("/admin/solicitudes/aprobadas", adminSolicitudesController::aprobar);
    app.post("/admin/solicitudes/rechazadas", adminSolicitudesController::rechazar);

  }
}