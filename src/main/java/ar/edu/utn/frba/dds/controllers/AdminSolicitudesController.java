package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;
import java.util.*;

public class AdminSolicitudesController {

  public void mostrarPanel(Context ctx) {
    String activeTab = Optional.ofNullable(ctx.queryParam("tab")).orElse("eliminacion");

    Map<String, Object> model = new HashMap<>();
    model.put("solicitudesSubida", sampleSubida());
    model.put("solicitudesModificacion", sampleModificacion());
    model.put("solicitudesEliminacion", sampleEliminacion());
    model.put("activeTab", activeTab);

    ctx.render("admin_solicitudes.hbs", model);
  }

  public void aprobar(Context ctx) {
    String tipo = ctx.pathParam("tipo");
    String id = ctx.pathParam("id");
    ctx.redirect("/admin/solicitudes?tab=" + tipo + "&msg=Aprobado:" + id);
  }

  public void rechazar(Context ctx) {
    String tipo = ctx.pathParam("tipo");
    String id = ctx.pathParam("id");
    ctx.redirect("/admin/solicitudes?tab=" + tipo + "&msg=Rechazado:" + id);
  }

  private List<Map<String, Object>> sampleSubida() {
    List<Map<String, Object>> lista = new ArrayList<>();
    lista.add(Map.of("id", "S-100", "usuario", "ana", "fuente", "Fuente Estática: CSV", "fecha", "2025-10-22"));
    lista.add(Map.of("id", "S-101", "usuario", "juan", "fuente", "Fuente Dinámica: API Clima", "fecha", "2025-10-21"));
    lista.add(Map.of("id", "S-102", "usuario", "lucia", "fuente", "Fuente Proxy: INDEC", "fecha", "2025-10-20"));
    return lista;
  }

  private List<Map<String, Object>> sampleModificacion() {
    List<Map<String, Object>> lista = new ArrayList<>();
    lista.add(Map.of("id", "M-200", "usuario", "bruno", "hecho", "Hecho #42 - Nombre viejo", "fecha", "2025-10-21"));
    lista.add(Map.of("id", "M-201", "usuario", "sofia", "hecho", "Hecho #58 - Datos actualizados", "fecha", "2025-10-22"));
    lista.add(Map.of("id", "M-202", "usuario", "tomas", "hecho", "Hecho #67 - Corrige valores", "fecha", "2025-10-19"));
    return lista;
  }

  private List<Map<String, Object>> sampleEliminacion() {
    List<Map<String, Object>> lista = new ArrayList<>();
    lista.add(Map.of("id", "E-300", "usuario", "carla", "hecho", "Hecho #99 - Registro X", "motivo", "Duplicado", "fecha", "2025-10-20"));
    lista.add(Map.of("id", "E-301", "usuario", "leo", "hecho", "Hecho #12 - Error de fuente", "motivo", "Información falsa", "fecha", "2025-10-19"));
    lista.add(Map.of("id", "E-302", "usuario", "martina", "hecho", "Hecho #87 - Temporal", "motivo", "Revisión administrativa", "fecha", "2025-10-18"));
    lista.add(Map.of("id", "E-303", "usuario", "dario", "hecho", "Hecho #33 - Duplicado", "motivo", "Registro redundante", "fecha", "2025-10-17"));
    return lista;
  }
}
