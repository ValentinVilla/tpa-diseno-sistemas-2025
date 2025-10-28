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
    System.out.println("Entré al método aprobar - " + ctx.method() + " " + ctx.path());

    // leer id (form param primero, si no existe intentamos path param)
    String idStr = ctx.formParam("id");
    System.out.println("id recibido: " + idStr);

    if (idStr == null || idStr.isBlank()) {
      ctx.status(400).result("id faltante");
      return;
    }

    Long id;
    try {
      id = Long.parseLong(idStr);
    } catch (NumberFormatException e) {
      ctx.status(400).result("id inválido");
      return;
    }

    // buscar solicitud
    Solicitud solicitud = repo.obtenerTodas().stream()
        .filter(s -> s.getId() != null && s.getId().equals(id))
        .findFirst()
        .orElse(null);

    if (solicitud == null) {
      System.out.println("Solicitud " + id + " no encontrada");
      ctx.status(404).result("Solicitud no encontrada");
      return;
    }

    try {
      solicitud.aceptar();
      repo.actualizar(solicitud);
      System.out.println("Solicitud " + id + " aceptada y actualizada en repo");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error al procesar la solicitud: " + e.getMessage());
      return;
    }

    // evitar recarga del iframe en UI; cambiar a redirect si querés navegar
    ctx.redirect("/admin/solicitudes");
    //ctx.status(204).result("");

    /* FORMAS DE VER QUE ME LLEGA DE LA REQUEST
    // path param
    //System.out.println("pathParam id: " + ctx.pathParam("id"));

    // query params
    System.out.println("Query params:");
    ctx.queryParamMap().forEach((k, v) -> System.out.println("  " + k + " = " + v));

    // form params (si el formulario usa POST)
    System.out.println("Form params:");
    ctx.formParamMap().forEach((k, v) -> System.out.println("  " + k + " = " + v));

    // headers
    System.out.println("Headers:");
    ctx.headerMap().forEach((k, v) -> System.out.println("  " + k + " = " + v));

    // body
    String body = ctx.body();
    System.out.println("Body length: " + (body == null ? 0 : body.length()));
    System.out.println("Body content:\n" + (body == null ? "<empty>" : body));*/

    // evitar recarga del iframe mientras debuggeás
  }

  public void rechazar(Context ctx) {
    System.out.println("Entré al método rechazar -" + ctx.method() + " " + ctx.path());

    // leer id (form param primero, si no existe intentamos path param)
    String idStr = ctx.formParam("id");
    System.out.println("id recibido: " + idStr);

    if (idStr == null || idStr.isBlank()) {
      ctx.status(400).result("id faltante");
      return;
    }

    Long id;
    try {
      id = Long.parseLong(idStr);
    } catch (NumberFormatException e) {
      ctx.status(400).result("id inválido");
      return;
    }

    // buscar solicitud
    Solicitud solicitud = repo.obtenerTodas().stream()
        .filter(s -> s.getId() != null && s.getId().equals(id))
        .findFirst()
        .orElse(null);

    if (solicitud == null) {
      System.out.println("Solicitud " + id + " no encontrada");
      ctx.status(404).result("Solicitud no encontrada");
      return;
    }

    // intentar aceptar y persistir
    try {
      solicitud.rechazar();
      repo.actualizar(solicitud);
      System.out.println("Solicitud " + id + " rechazada y actualizada en repo");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error al procesar la solicitud: " + e.getMessage());
      return;
    }

    // evitar recarga del iframe en UI; cambiar a redirect si querés navegar
    ctx.redirect("/admin/solicitudes");

    //ctx.status(204).result("");
  }
}
