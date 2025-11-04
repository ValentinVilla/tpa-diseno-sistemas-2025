package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.model.dtos.ColeccionDTO;
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
import ar.edu.utn.frba.dds.model.mappers.MapperColeccion;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import ar.edu.utn.frba.dds.repositorios.RepositorioFuentes;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminController {
  private MapperColeccion mapper = new MapperColeccion();
  private final RepositorioFuentes repoFuentes = RepositorioFuentes.getInstancia();

  public void mostrarAdmin(Context ctx) {
    ctx.render("admin.hbs");
  }

  public void mostarCrearColeccion(Context ctx){
    Map<String, Object> model = new HashMap<>();
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
    ctx.render("admin_colecciones_crear.hbs", model);
  }

  public void crearColeccion(Context ctx) {
    ColeccionDTO dto = ctx.bodyAsClass(ColeccionDTO.class);
    Coleccion coleccion = new ColeccionBuilder()
    .titulo(dto.getTitulo())
        .descripcion(dto.getDescripcion())
        .algoritmoConsenso(mapper.toAlgoritmoConsenso(dto.getAlgoritmo()))
        .fuente(repoFuentes.buscarPorID(dto.getFuente()))
        .criterio(mapper.toCriterio(dto.getCriterio()))
        .modoNavegacion(mapper.toNavegacion(dto.getNavegacion()))
        .build();
    // Asignar nombre de fuente si fue provisto
    /*if (coleccion.getFuente() != null && coleccion.getFuente().getNombre() != null && !coleccion.getFuente().getNombre().isBlank()) {
      try { coleccion.getFuente().setNombre(fuenteNombre); } catch (Exception ignored) {}
    }*/
    RepositorioColecciones.getInstancia().guardar(coleccion);
    ctx.status(201).result("Creada");
  }

  // Endpoint para actualizar configuración rápida de una colección
  public void editarColeccion(Context ctx) {
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
    // usar getCriterio() y proteger nulls
    Filtro criterio = coleccion.getCriterio();
    model.put("categoria", criterio != null ? criterio.getDescripcion() : "");

    // algoritmo: marcar la opción correspondiente como true/false esto es necesario para que en la casilla arranque con la opcion qe ya tiene de antes
    String algoritmo = coleccion.getAlgoritmoConsenso() != null ? coleccion.getAlgoritmoConsenso().name() : "DEFAULT";
    model.put("algoritmoConsenso", algoritmo);
    model.put("algoritmo_MAYORIA_SIMPLE", "MAYORIA_SIMPLE".equals(algoritmo));
    model.put("algoritmo_ABSOLUTO", "ABSOLUTO".equals(algoritmo));
    model.put("algoritmo_MULTIPLES_MENCIONES", "MULTIPLES_MENCIONES".equals(algoritmo));
    model.put("algoritmo_DEFAULT", "DEFAULT".equals(algoritmo));

    //aca vamos a decirle al model que fuentes tiene que mostrar como opciones y decirle con selected cual es la inicial
    List<Fuente> fuentesActivas = RepositorioFuentes.getInstancia().listarTodas();
    List<Map<String, Object>> fuentesVM = new ArrayList<>();
    fuentesActivas.forEach( fuente -> {
      Map<String, Object> m = new HashMap<>();
      m.put("fuente_id", fuente.getId());
      m.put("titulo", fuente.getNombre());
      m.put("tipo", fuente.getClass().getSimpleName());
      boolean selected = (coleccion.getFuente().getId() != null && fuente.getId() != null && fuente.getId().equals(coleccion.getFuente().getId()));
      m.put("selected", selected);
      fuentesVM.add(m);
    });
    model.put("FUENTESACTIVAS", fuentesVM);

    ctx.render("admin_colecciones_gestionar.hbs", model);
  }
}