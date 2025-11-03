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
import ar.edu.utn.frba.dds.model.filtros.FiltroTexto;
import ar.edu.utn.frba.dds.model.consenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.model.consenso.ModoNavegacion;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import ar.edu.utn.frba.dds.repositorios.RepositorioFuentes;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // MAPEAR FUENTE
    if(payload.fuente == null){
      ctx.status(404).result("fuente no encontrada");
    }
    if(payload.fuente != null){
      Long fuente_id = Long.parseLong(payload.fuente);
      Fuente nuevaFuente = RepositorioFuentes.getInstancia().buscarPorID(fuente_id);
      coleccion.setFuente(nuevaFuente);
    }

    // Mapear criterio textual (si viene)
    if (payload.criterio != null) {
      if (payload.criterio.isBlank()) {
        coleccion.setCriterio(null);
      } else {
        coleccion.setCriterio(new FiltroTexto(payload.criterio));
      }
    }

    RepositorioColecciones.getInstancia().actualizar(coleccion);
    // Preparar respuesta JSON con los valores actualizados para que el frontend actualice el DOM
    Map<String, Object> resp = new HashMap<>();
    resp.put("id", coleccion.getId());
    String alg = coleccion.getAlgoritmoConsenso() != null ? coleccion.getAlgoritmoConsenso().name() : "DEFAULT";
    resp.put("algoritmoConsenso", alg);

    String fuenteTipoResp = "";
    String fuenteNombreResp = "";
    String fuenteClassResp = "-";
    try {
      if (coleccion.getFuente() != null) {
        fuenteClassResp = coleccion.getFuente().getClass().getSimpleName();
        fuenteNombreResp = coleccion.getFuente().getNombre() != null ? coleccion.getFuente().getNombre() : "";
        switch (fuenteClassResp) {
          case "FuenteEstatica" -> fuenteTipoResp = "fuente_estatica";
          case "FuenteDinamica" -> fuenteTipoResp = "fuente_dinamica";
          default -> fuenteTipoResp = "fuente_proxy";
        }
      }
    } catch (Exception ignored) {}

    resp.put("fuenteTipo", fuenteTipoResp);
    resp.put("fuenteNombre", fuenteNombreResp);
    resp.put("fuenteClass", fuenteClassResp);

    String criterioDesc = coleccion.getCriterio() != null ? coleccion.getCriterio().getDescripcion() : "-";
    resp.put("criterioPertenencia", criterioDesc);

    ctx.json(resp);
  }

  public static class ConfigPayload {
    public String algoritmo;
    public String fuente;
    public String criterio;

    public ConfigPayload() {}

    public String getAlgoritmo() { return algoritmo; }
    public void setAlgoritmo(String algoritmo) { this.algoritmo = algoritmo; }
    public String getFuente() { return fuente; }
    public void setFuente(String fuente) { this.fuente = fuente; }
    public String getCriterio() { return criterio; }
    public void setCriterio(String criterio) { this.criterio = criterio; }
  }

  // Nueva vista: mostrar la página de gestión avanzada de una colección
  public void mostrarGestionColeccion(Context ctx) {
    String idStr = ctx.pathParam("id");
    Long id;
    try { id = Long.parseLong(idStr); } catch (Exception e) { ctx.status(400).result("ID inválido"); return; }

    Coleccion coleccion = RepositorioColecciones.getInstancia().buscarPorID(id);
    if (coleccion == null) { ctx.status(404).result("Colección no encontrada"); return; }

    Map<String, Object> model = new HashMap<>();
    model.put("id", coleccion.getId());
    model.put("titulo", coleccion.getTitulo());
    model.put("descripcion", coleccion.getDescripcion());
    model.put("fuenteActual", coleccion.getFuente().getNombre());

    // Fuente
    String fuenteTipo = "";
    String fuenteNombre = "";
    try {
      if (coleccion.getFuente() != null) {
        String tipo = coleccion.getFuente().getClass().getSimpleName();
        switch (tipo) {
          case "FuenteEstatica" -> fuenteTipo = "fuente_estatica";
          case "FuenteDinamica" -> fuenteTipo = "fuente_dinamica";
          default -> fuenteTipo = "fuente_proxy";
        }
        try { fuenteNombre = coleccion.getFuente().getNombre(); } catch (Exception ignored){}
      }
    } catch (Exception ignored){}

    model.put("fuenteTipo", fuenteTipo);
    model.put("fuenteNombre", fuenteNombre);

    // Algoritmo
    String algoritmo = coleccion.getAlgoritmoConsenso() != null ? coleccion.getAlgoritmoConsenso().name() : "DEFAULT";
    model.put("algoritmoConsenso", algoritmo);
    model.put("algoritmo_MAYORIA_SIMPLE", "MAYORIA_SIMPLE".equals(algoritmo));
    model.put("algoritmo_ABSOLUTO", "ABSOLUTO".equals(algoritmo));
    model.put("algoritmo_MULTIPLES_MENCIONES", "MULTIPLES_MENCIONES".equals(algoritmo));
    model.put("algoritmo_DEFAULT", "DEFAULT".equals(algoritmo));

    // Criterio (si hay uno)
    model.put("criterioPertenencia", coleccion.getCriterio() != null ? coleccion.getCriterio().getDescripcion() : "");

    // Fuente flags
    model.put("fuente_estatica", "fuente_estatica".equals(fuenteTipo));
    model.put("fuente_dinamica", "fuente_dinamica".equals(fuenteTipo));
    model.put("fuente_proxy", "fuente_proxy".equals(fuenteTipo));

    //aca vamos a decirle al model que fuentes tiene que mostrar como opciones
    List<Fuente> fuentesActivas = RepositorioFuentes.getInstancia().listarTodas();
    List<Map<String, Object>> fuentesVM = new ArrayList<>();
    fuentesActivas.forEach( fuente -> {
      Map<String, Object> m = new HashMap<>();
      m.put("fuente_id", fuente.getId());
      m.put("titulo", fuente.getNombre());
      m.put("tipo", fuente.getClass().getSimpleName());
      fuentesVM.add(m);
    });
    model.put("FUENTESACTIVAS", fuentesVM);

    ctx.render("admin_colecciones_gestionar.hbs", model);
  }
}