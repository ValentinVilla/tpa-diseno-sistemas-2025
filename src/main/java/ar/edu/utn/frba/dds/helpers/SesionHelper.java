package ar.edu.utn.frba.dds.helpers;

import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.repositorios.RepositorioUsuarios;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SesionHelper {
  private static final int BCRYPT_LOG_ROUNDS = 12;

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
      ctx.sessionAttribute("returnTo", null);
      return returnTo;
    }
    return null;
  }

  public static void redirigirPostLogin(Context ctx) {
    String returnTo = consumirReturnTo(ctx);
    ctx.redirect(Objects.requireNonNullElse(returnTo, "/home"));
  }

  public static String hashPassword(String plainPassword) {
    if (plainPassword == null) return null;
    return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_LOG_ROUNDS));
  }

  public static boolean matchesPassword(String plainPassword, String storedHash) {
    if (plainPassword == null || storedHash == null) return false;
    try {
      return BCrypt.checkpw(plainPassword, storedHash);
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static boolean looksLikeBcrypt(String value) {
    if (value == null) return false;
    return value.startsWith("$2a$") || value.startsWith("$2y$") || value.startsWith("$2b$");
  }

  public static Contribuyente authenticateByEmailAndPassword(String email, String plainPassword) {
    if (email == null || plainPassword == null) return null;
    Contribuyente usuario = RepositorioUsuarios.getInstancia().buscarPorEmail(email);
    if (usuario == null) return null;

    String stored = usuario.getPassword();
    if (stored == null) return null;

    if (looksLikeBcrypt(stored)) {
      if (matchesPassword(plainPassword, stored)) {
        return usuario;
      } else {
        return null;
      }
    } else {
      if (stored.equals(plainPassword)) {
        String newHash = hashPassword(plainPassword);
        usuario.setPassword(newHash);
        RepositorioUsuarios.getInstancia().actualizar(usuario);
        return usuario;
      } else {
        return null;
      }
    }
  }
}

