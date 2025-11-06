package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.ColeccionesController;
import ar.edu.utn.frba.dds.controllers.HechosController;
import ar.edu.utn.frba.dds.controllers.SesionesController;
import ar.edu.utn.frba.dds.controllers.UsuariosController;
import ar.edu.utn.frba.dds.controllers.AdminSolicitudesController;
import ar.edu.utn.frba.dds.controllers.AdminController;
import ar.edu.utn.frba.dds.controllers.AdminColeccionesController;
import io.javalin.Javalin;

import java.util.Map;

public class Router {
  UsuariosController usuariosController = new UsuariosController();
  ColeccionesController coleccionesController = new ColeccionesController();
  HechosController hechosController = new HechosController();
  SesionesController sesionesController = new SesionesController();
  AdminSolicitudesController adminSolicitudesController = new AdminSolicitudesController();
  AdminController adminController = new AdminController();
  AdminColeccionesController adminColeccionesController = new AdminColeccionesController();

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
    app.get("/hechos", hechosController::mostrarVistaHechos);
    app.get("/hechos.json", hechosController::mostrarHechosMapa);
    app.get("/hechos/nuevo", hechosController::mostrarFormularioNuevoHecho);
    app.get("/hechos/delete", hechosController::mostrarFormularioEliminacion);
    app.get("/hechos/{id}", hechosController::mostrarHecho);
    app.post("/hechos", hechosController::crearHecho);
    app.post("/hechos/delete", hechosController::crearSolicitudEliminacion);
    app.post("/hechos/preview", hechosController::mostrarHechoPreview);

    // --- Colecciones ---
    app.get("/colecciones", ctx -> ctx.render("colecciones.hbs", coleccionesController.mostrarColecciones(ctx)));
    app.get("/colecciones/{id}", coleccionesController::mostrarColeccionPorId);
    app.get("/colecciones/{id}/hechos.json", coleccionesController::mostrarHechosColeccionJson);

    // --- Rutas Admin ---
    app.get("/admin", adminController::mostrarAdmin); //pasar a adminController
    app.get("/admin/colecciones", ctx -> ctx.render("admin_colecciones.hbs", adminColeccionesController.mostrarColecciones()));
    app.get("/admin/colecciones/nueva", adminController::mostarCrearColeccion);
    app.post("/admin/colecciones", adminController::crearColeccion);
    app.get("/admin/colecciones/{id}/gestion", adminController::mostrarGestionColeccion);
    app.post("/admin/colecciones/{id}/configuracion", adminController::editarColeccion); //somos conscientes que es un patch a la ruta de /admin/colecciones/{id} pero la tecnologia no soporta patch entonces cambiamos la url

    app.get("/admin/solicitudes", adminSolicitudesController::mostrarPanel);

    // Endpoints provisorios para aprobar/rechazar (más adelante pasar a POST y validar
    app.post("/admin/solicitudes/aprobadas", adminSolicitudesController::aprobar);
    app.post("/admin/solicitudes/rechazadas", adminSolicitudesController::rechazar);

  }
}