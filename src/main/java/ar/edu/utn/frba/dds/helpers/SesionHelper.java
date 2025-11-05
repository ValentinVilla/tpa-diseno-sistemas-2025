package ar.edu.utn.frba.dds.helpers;

import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SesionHelper {
  public static Map<String, Object> crearModeloBase(Context ctx) {
    Map<String, Object> modelo = new HashMap<>();
    Contribuyente usuario = ctx.sessionAttribute("usuario_logueado");
    if (usuario != null) modelo.put("nombre", usuario.getNombre());
    return modelo;
  }

  public static void guardarUsuario(Context ctx, Contribuyente usuario) {
    ctx.sessionAttribute("usuario_logueado", usuario);
  }

  public static Contribuyente obtenerUsuario(Context ctx) {
    return ctx.sessionAttribute("usuario_logueado");
  }

  public static void guardarReturnTo(Context ctx, String path) {
    ctx.sessionAttribute("returnTo", path);
  }

  public static String consumirReturnTo(Context ctx) {
    String returnTo = ctx.sessionAttribute("returnTo");
    if (returnTo != null && !returnTo.isEmpty()) {
      ctx.sessionAttribute("returnTo", null); // limpiar
      return returnTo;
    }
    return null;
  }

  public static void redirigirPostLogin(Context ctx) {
    String returnTo = consumirReturnTo(ctx);
    ctx.redirect(Objects.requireNonNullElse(returnTo, "/home"));
  }
}

