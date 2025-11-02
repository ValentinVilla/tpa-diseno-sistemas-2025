package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.fuenteEstatica.FuenteEstatica;
import ar.edu.utn.frba.dds.model.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.model.fuentes.fuenteProxy.FuenteMetaMapa;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.model.filtros.FiltroFecha;
import ar.edu.utn.frba.dds.model.consenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.model.consenso.ModoNavegacion;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import io.javalin.http.Context;

import java.util.List;

public class AdminController {

  public void mostrarAdmin(Context ctx) {
    ctx.render("admin.hbs");
  }

  public void mostarCrearColeccion(Context ctx){
    ctx.render("admin_colecciones_crear.hbs");
  }

  public void crearColeccion(Context ctx) {
    String titulo = ctx.formParam("titulo");
    String descripcion = ctx.formParam("descripcion");
    List<String> fuentes = ctx.formParams("fuentes");
    String criterio = ctx.formParam("criterio");
    String algoritmo = ctx.formParam("algoritmo");
    String modo = ctx.formParam("modo");
    String fuenteNombre = ctx.formParam("fuenteNombre");

    // Mapea la primera fuente seleccionada a una implementación concreta.
    String fuenteSeleccionada = !fuentes.isEmpty() ? fuentes.get(0) : null;

    Fuente fuenteObj = null;
    if (fuenteSeleccionada != null) {
      switch (fuenteSeleccionada) {
        case "fuente_estatica" -> fuenteObj = new FuenteEstatica();
        case "fuente_dinamica" -> fuenteObj = new FuenteDinamica();
        case "fuente_proxy" -> fuenteObj = new FuenteMetaMapa();
        default -> fuenteObj = new FuenteEstatica();
      }
    }

    // Asignar nombre de fuente si fue provisto
    if (fuenteObj != null && fuenteNombre != null && !fuenteNombre.isBlank()) {
      try { fuenteObj.setNombre(fuenteNombre); } catch (Exception ignored) {}
    }

    // Mapea criterio a un Filtro simple
    Filtro filtro = null;
    if (criterio != null) {
      switch (criterio) {
        case "filtro_geografico", "filtro_categoria" -> filtro = new FiltroCategoria(null);
        case "filtro_fecha" -> filtro = new FiltroFecha(null, null);
        default -> filtro = new FiltroCategoria(null);
      }
    }

    // Mapear algoritmo del formulario a los enums reales del proyecto
    AlgoritmoConsenso algoritmoEnum = AlgoritmoConsenso.DEFAULT; // default razonable
    if (algoritmo != null) {
      switch (algoritmo) {
        case "mayoria" -> algoritmoEnum = AlgoritmoConsenso.MAYORIA_SIMPLE;
        case "unanimidad" -> algoritmoEnum = AlgoritmoConsenso.ABSOLUTO;
        case "ponderado" -> algoritmoEnum = AlgoritmoConsenso.MULTIPLES_MENCIONES;
      }
    }

    // Mapear modo de navegación a los enums existentes (evitar asignación redundante)
    ModoNavegacion modoEnum = null;
    if (modo != null) {
      switch (modo) {
        case "lista" -> modoEnum = ModoNavegacion.IRRESTRICTA;
        case "mapa" -> modoEnum = ModoNavegacion.CURADA;
        case "paginado" -> modoEnum = ModoNavegacion.IRRESTRICTA;
      }
    }
    if (modoEnum == null) modoEnum = ModoNavegacion.IRRESTRICTA;

    ColeccionBuilder builder = new ColeccionBuilder();
    builder.titulo(titulo != null ? titulo : "Sin título");
    builder.descripcion(descripcion != null ? descripcion : "");

    try {
      if (fuenteObj != null) builder.fuente(fuenteObj);
    } catch (Exception e) {
      // ignoramos y continuamos con la creación
    }

    if (filtro != null) builder.criterio(filtro);
    builder.algoritmoConsenso(algoritmoEnum);
    builder.modoNavegacion(modoEnum);

    Coleccion coleccion = builder.build();

    RepositorioColecciones.getInstancia().guardar(coleccion);

    ctx.redirect("/admin/colecciones");
  }

  // Endpoint para actualizar configuración rápida de una colección
  public void configurarColeccion(Context ctx) {
    String idStr = ctx.pathParam("id");
    Long id;
    try {
      id = Long.parseLong(idStr);
    } catch (Exception e) {
      ctx.status(400).result("ID inválido");
      return;
    }

    ConfigPayload payload = ctx.bodyAsClass(ConfigPayload.class);

    Coleccion coleccion = RepositorioColecciones.getInstancia().buscarPorID(id);
    if (coleccion == null) {
      ctx.status(404).result("Colección no encontrada");
      return;
    }

    // Mapear algoritmo
    if (payload.algoritmo != null) {
      switch (payload.algoritmo) {
        case "MAYORIA_SIMPLE" -> coleccion.setAlgoritmoConsenso(AlgoritmoConsenso.MAYORIA_SIMPLE);
        case "ABSOLUTO" -> coleccion.setAlgoritmoConsenso(AlgoritmoConsenso.ABSOLUTO);
        case "MULTIPLES_MENCIONES" -> coleccion.setAlgoritmoConsenso(AlgoritmoConsenso.MULTIPLES_MENCIONES);
        default -> coleccion.setAlgoritmoConsenso(AlgoritmoConsenso.DEFAULT);
      }
    }

    // Mapear fuente
    if (payload.fuenteTipo != null) {
      Fuente nuevaFuente = null;
      switch (payload.fuenteTipo) {
        case "fuente_estatica" -> nuevaFuente = new FuenteEstatica();
        case "fuente_dinamica" -> nuevaFuente = new FuenteDinamica();
        case "fuente_proxy" -> nuevaFuente = new FuenteMetaMapa();
        default -> nuevaFuente = new FuenteEstatica();
      }

      // Si payload trae nombre, setearlo
      if (payload.fuenteNombre != null && !payload.fuenteNombre.isBlank()) {
        try { nuevaFuente.setNombre(payload.fuenteNombre);} catch (Exception ignored){}
      }

      coleccion.setFuente(nuevaFuente);
    }

    RepositorioColecciones.getInstancia().actualizar(coleccion);
    ctx.status(200).result("OK");
  }

  public static class ConfigPayload {
    public String algoritmo;
    public String fuenteTipo;
    public String fuenteNombre;

    public ConfigPayload() {}

    public String getAlgoritmo() { return algoritmo; }
    public void setAlgoritmo(String algoritmo) { this.algoritmo = algoritmo; }
    public String getFuenteTipo() { return fuenteTipo; }
    public void setFuenteTipo(String fuenteTipo) { this.fuenteTipo = fuenteTipo; }
    public String getFuenteNombre() { return fuenteNombre; }
    public void setFuenteNombre(String fuenteNombre) { this.fuenteNombre = fuenteNombre; }
  }
}