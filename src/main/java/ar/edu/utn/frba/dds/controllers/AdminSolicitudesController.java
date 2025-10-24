package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudModificacion;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudSubida;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.model.solicitudes.Solicitud;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

public class AdminSolicitudesController {

  private final RepositorioSolicitudes repo = RepositorioSolicitudes.getInstancia();

  public void mostrarPanel(Context ctx) {
    String activeTab = Optional.ofNullable(ctx.queryParam("tab")).orElse("eliminacion");

    var repo = RepositorioSolicitudes.getInstancia();
    List<Solicitud> todas = repo.obtenerTodasPendientes();

    List<Map<String, Object>> solicitudesSubida = new ArrayList<>();
    List<Map<String, Object>> solicitudesModificacion = new ArrayList<>();
    List<Map<String, Object>> solicitudesEliminacion = new ArrayList<>();

    for (Solicitud s : todas) {
      Map<String, Object> data = new HashMap<>();
      // No creo que l ID sea necesario
      data.put("id", s.getId());
      data.put("fecha", s.getFecha() != null ? s.getFecha().toString() : "Sin fecha"); // el sin fecha es hasta resetear bdd

      String[] partes = s.getValoresHecho().split(";");
      String titulo = partes.length > 0 ? partes[0] : "";
      String descripcion = partes.length > 1 ? partes[1] : "";
      String categoria = partes.length > 2 ? partes[2] : "";
      String contribuyente = partes.length > 3 ? partes[3] : "Anónimo";

      data.put("titulo", titulo);
      data.put("descripcion", descripcion);
      data.put("categoria", categoria);
      data.put("contribuyente", contribuyente);

      if (s instanceof SolicitudSubida) {
        solicitudesSubida.add(data);
      } else if (s instanceof SolicitudModificacion) {
        data.put("fundamentacion", s.getTextoFundamentacion());
        solicitudesModificacion.add(data);
      } else if (s instanceof SolicitudEliminacion) {
        data.put("fundamentacion", s.getTextoFundamentacion());
        solicitudesEliminacion.add(data);
      }
    }

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
