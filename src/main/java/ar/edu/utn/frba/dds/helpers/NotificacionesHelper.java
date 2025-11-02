package ar.edu.utn.frba.dds.helpers;

import io.javalin.http.Context;

import java.util.Map;

public class NotificacionesHelper {
  public static Map<String, String> crearNotificacion(Context ctx) {
    String creacion = ctx.queryParam("creacion");
    String solicitud = ctx.queryParam("solicitud");

    if ("exito".equals(creacion)) {
      return Map.of("tipo", "exito", "mensaje", "Hecho creado correctamente.");
    } else if ("exito".equals(solicitud)) {
      return Map.of("tipo", "exito", "mensaje", "Solicitud enviada correctamente.");
    } else if ("error".equals(solicitud)) {
      return Map.of("tipo", "error", "mensaje", "Hubo un error al enviar la solicitud.");
    } else if ("error".equals(creacion)) {
      return Map.of("tipo", "error", "mensaje", "Error en la creacion del hecho. Intentelo nuevamente.");
    }
    return null;
  }
}
