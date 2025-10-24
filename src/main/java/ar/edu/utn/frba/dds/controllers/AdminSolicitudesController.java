package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.model.solicitudes.Solicitud;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

public class AdminSolicitudesController {

  private final RepositorioSolicitudes repo = RepositorioSolicitudes.getInstancia();

  public void mostrarPanel(Context ctx) {
    String activeTab = Optional.ofNullable(ctx.queryParam("tab")).orElse("eliminacion");

    // Traemos los datos reales del repositorio
    List<Solicitud> solicitudesSubida = repo.obtenerSubidas();
    List<Solicitud> solicitudesModificacion = repo.obtenerModificaciones();
    List<Solicitud> solicitudesEliminacion = repo.obtenerEliminaciones();

    Map<String, Object> model = new HashMap<>();
    model.put("solicitudesSubida", solicitudesSubida);
    model.put("solicitudesModificacion", solicitudesModificacion);
    model.put("solicitudesEliminacion", solicitudesEliminacion);
    model.put("activeTab", activeTab);

    ctx.render("admin_solicitudes.hbs", model);
  }

  public void aprobar(Context ctx) {
    String tipo = ctx.pathParam("tipo");
    Long id = Long.parseLong(ctx.pathParam("id"));
    Solicitud solicitud = repo.obtenerTodas().stream()
        .filter(s -> s.getId().equals(id))
        .findFirst()
        .orElse(null);
    if (solicitud != null) {
      solicitud.aceptar();
      repo.actualizar(solicitud); // persiste el cambio
    }
    ctx.redirect("/admin/solicitudes?tab=" + tipo + "&msg=Aprobado:" + id);
  }

  public void rechazar(Context ctx) {
    String tipo = ctx.pathParam("tipo");
    Long id = Long.parseLong(ctx.pathParam("id"));
    Solicitud solicitud = repo.obtenerTodas().stream()
        .filter(s -> s.getId().equals(id))
        .findFirst()
        .orElse(null);
    if (solicitud != null) {
      solicitud.rechazar();
      repo.actualizar(solicitud); // persiste el cambio
    }
    ctx.redirect("/admin/solicitudes?tab=" + tipo + "&msg=Rechazado:" + id);
  }

}
