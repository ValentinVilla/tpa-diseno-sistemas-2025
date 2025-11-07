package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.model.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.FiltroTexto;
import ar.edu.utn.frba.dds.model.consenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.model.mappers.MapperColeccion;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import ar.edu.utn.frba.dds.repositorios.RepositorioFuentes;
import io.javalin.http.Context;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminController {
  private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
  private final MapperColeccion mapper = new MapperColeccion();
  private final RepositorioFuentes repoFuentes = RepositorioFuentes.getInstancia();

  public void mostrarAdmin(Context ctx) {
    if (!ar.edu.utn.frba.dds.helpers.SesionHelper.esAdminLogueado(ctx)) {
      ctx.redirect("/home");
      return;
    }
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
    // listar filtros existentes y pasarlos a la vista para seleccion
    List<ar.edu.utn.frba.dds.model.filtros.Filtro> filtros = ar.edu.utn.frba.dds.repositorios.RepositorioFiltros.getInstancia().listarTodas();
    List<Map<String,Object>> filtrosVM = new ArrayList<>();
    filtros.forEach(f -> {
      Map<String,Object> fm = new HashMap<>();
      fm.put("id", f.getId());
      fm.put("descripcion", f.getDescripcion());
      fm.put("tipo", f.getClass().getSimpleName());
      filtrosVM.add(fm);
    });
    model.put("FILTROS_ACTIVOS", filtrosVM);
    ctx.render("admin_colecciones_crear.hbs", model);
  }

  public void crearColeccion(Context ctx) {
    ColeccionDTO dto = ctx.bodyAsClass(ColeccionDTO.class);
    // Mapear criterio: posible vías
    // 1) dto.criterioId -> usar filtro existente
    // 2) dto.criterioTipo + dto.criterio (texto) -> crear filtro nuevo del tipo indicado
    // 3) dto.criterio (texto) -> intentar mapper -> fallback a FiltroTexto
    Filtro criterio = null;
    if (dto.getCriterioId() != null) {
      criterio = ar.edu.utn.frba.dds.repositorios.RepositorioFiltros.getInstancia().buscarPorID(dto.getCriterioId());
    }
    if (criterio == null && dto.getCriterioTipo() != null && !dto.getCriterioTipo().isBlank()) {
      String tipo = dto.getCriterioTipo().trim();
      switch (tipo) {
        case "TEXTO" -> criterio = new FiltroTexto(dto.getCriterio() != null ? dto.getCriterio().trim() : null);
        case "CATEGORIA" -> criterio = new ar.edu.utn.frba.dds.model.filtros.FiltroCategoria(dto.getCriterio() != null ? dto.getCriterio().trim() : null);
        case "FECHA" -> criterio = new ar.edu.utn.frba.dds.model.filtros.FiltroFecha(null, null); // UI debe completar fechas
        default -> criterio = mapper.toCriterio(dto.getCriterio());
      }
    }
    if (criterio == null) {
      criterio = mapper.toCriterio(dto.getCriterio());
      if (criterio == null && dto.getCriterio() != null && !dto.getCriterio().trim().isEmpty()) {
        criterio = new FiltroTexto(dto.getCriterio().trim());
      }
    }

    ColeccionBuilder builder = new ColeccionBuilder()
        .titulo(dto.getTitulo())
        .descripcion(dto.getDescripcion())
        .algoritmoConsenso(mapper.toAlgoritmoConsenso(dto.getAlgoritmo()))
        .modoNavegacion(mapper.toNavegacion(dto.getNavegacion()));

    // asignar fuente sólo si existe
    if (dto.getFuente() != null) {
      try {
        Fuente f = repoFuentes.buscarPorID(dto.getFuente());
        if (f != null) builder.fuente(f);
      } catch (Exception ignored) {}
    }

    if (criterio != null) {
      builder.criterio(criterio);
    }

    Coleccion coleccion = builder.build();
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
    if(payload.fuente != null){
      try {
        Long fuente_id = Long.parseLong(payload.fuente);
        Fuente nuevaFuente = RepositorioFuentes.getInstancia().buscarPorID(fuente_id);
        if (nuevaFuente != null) coleccion.setFuente(nuevaFuente);
      } catch (NumberFormatException e) {
        // if parsing fails, ignore and fall back to nombre handling
      }
    } else if (payload.fuenteNombre != null && !payload.fuenteNombre.isBlank()){
      // intentar buscar una fuente por nombre (si existe) o actualizar nombre si la fuente es mutable
      try {
        Fuente actual = coleccion.getFuente();
        if (actual != null) {
          try { actual.setNombre(payload.fuenteNombre); } catch (Exception ignored) {}
        }
      } catch (Exception ignored) {}
    }
    //TODO revisar esta validacion
    // MAPEAR CRITERIO: priorizar criterioId (filtro existente), luego criterioTipo (nuevo filtro), luego criterio textual
    if (payload.criterioId != null) {
      // buscar filtro por id y asignarlo
      ar.edu.utn.frba.dds.model.filtros.Filtro filtroExistente = ar.edu.utn.frba.dds.repositorios.RepositorioFiltros.getInstancia().buscarPorID(payload.criterioId);
      if (filtroExistente != null) {
        coleccion.setCriterio(filtroExistente);
      }
    } else if (payload.criterioTipo != null && !payload.criterioTipo.isBlank()) {
      String tipo = payload.criterioTipo.trim();
      switch (tipo) {
        case "TEXTO" -> coleccion.setCriterio(new FiltroTexto(payload.criterio != null ? payload.criterio.trim() : null));
        case "CATEGORIA" -> coleccion.setCriterio(new ar.edu.utn.frba.dds.model.filtros.FiltroCategoria(payload.criterio != null ? payload.criterio.trim() : null));
        case "FECHA" -> coleccion.setCriterio(new ar.edu.utn.frba.dds.model.filtros.FiltroFecha(null, null));
        default -> { /* dejar que el flujo textual lo maneje si aplica */ }
      }
    }

    // Mapear criterio textual (si viene)
    if (payload.criterio != null) {
      String incoming = payload.criterio.trim();
      // si el criterio no aporta nada, limpiamos
      if (incoming.isBlank()) {
        coleccion.setCriterio(null);
      } else {
        // evitar crear filtro si ya existe y coincide con la descripcion actual
        Filtro actualFiltro = coleccion.getCriterio();
        String actualDesc = actualFiltro != null ? actualFiltro.getDescripcion() : null;
        // Si el incoming contiene el prefijo 'Contiene texto' (o su variacion) extraer la parte entre comillas
        String textoBuscado = incoming;
        if (incoming.toLowerCase().startsWith("contiene texto")) {
          // intentar extraer "..." si existe
          int firstQuote = incoming.indexOf('"');
          int lastQuote = incoming.lastIndexOf('"');
          if (firstQuote >= 0 && lastQuote > firstQuote) {
            textoBuscado = incoming.substring(firstQuote + 1, lastQuote).trim();
          } else {
            // si no hay comillas, intentar sacar todo lo que sigue a ':'
            int colon = incoming.indexOf(':');
            if (colon >= 0 && colon < incoming.length()-1) {
              textoBuscado = incoming.substring(colon+1).trim().replaceAll("^\"|\"$", "");
            }
          }
        }

        // si la descripcion actual ya coincide con incoming o contiene el texto buscado, no actualizar
        if (actualDesc != null) {
          boolean equalsIncoming = actualDesc.equals(incoming);
          boolean containsTexto = (textoBuscado != null && !textoBuscado.isBlank() && actualDesc.contains(textoBuscado));
          if (!equalsIncoming && !containsTexto) {
            coleccion.setCriterio(new FiltroTexto(textoBuscado));
          }
        } else {
          coleccion.setCriterio(new FiltroTexto(textoBuscado));
        }
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

  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ConfigPayload {
     public String algoritmo;
     public String fuente;
     public String fuenteNombre;
     public String criterio;

     // nuevos campos para recibir criterio por id o tipo desde la UI
     public Long criterioId;
     public String criterioTipo;

     public ConfigPayload() {}

     public String getAlgoritmo() { return algoritmo; }
     public void setAlgoritmo(String algoritmo) { this.algoritmo = algoritmo; }
     public String getFuente() { return fuente; }
     public void setFuente(String fuente) { this.fuente = fuente; }
     public String getFuenteNombre() { return fuenteNombre; }
     public void setFuenteNombre(String fuenteNombre) { this.fuenteNombre = fuenteNombre; }
     public String getCriterio() { return criterio; }
     public void setCriterio(String criterio) { this.criterio = criterio; }
     public Long getCriterioId() { return criterioId; }
     public void setCriterioId(Long criterioId) { this.criterioId = criterioId; }
     public String getCriterioTipo() { return criterioTipo; }
     public void setCriterioTipo(String criterioTipo) { this.criterioTipo = criterioTipo; }
   }

  // Nueva vista: mostrar la página de gestión avanzada de una colección
  public void mostrarGestionColeccion(Context ctx) {
    try {
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
      model.put("criterioId", criterio != null ? criterio.getId() : null);
      model.put("criterioTipo", criterio != null ? criterio.getClass().getSimpleName() : "");

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
        boolean selected = (coleccion.getFuente() != null && coleccion.getFuente().getId() != null && fuente.getId() != null && fuente.getId().equals(coleccion.getFuente().getId()));
        m.put("selected", selected);
        fuentesVM.add(m);
      });
      model.put("FUENTESACTIVAS", fuentesVM);
      // pasar tambien filtros existentes
      List<ar.edu.utn.frba.dds.model.filtros.Filtro> filtros = ar.edu.utn.frba.dds.repositorios.RepositorioFiltros.getInstancia().listarTodas();
      List<Map<String,Object>> filtrosVM = new ArrayList<>();
      filtros.forEach(f -> {
        Map<String,Object> fm = new HashMap<>();
        fm.put("id", f.getId());
        fm.put("descripcion", f.getDescripcion());
        fm.put("tipo", f.getClass().getSimpleName());
        fm.put("selected", (criterio != null && criterio.getId() != null && criterio.getId().equals(f.getId())));
        filtrosVM.add(fm);
      });
      model.put("FILTROS_ACTIVOS", filtrosVM);

      ctx.render("admin_colecciones_gestionar.hbs", model);
    } catch (Exception e) {
      // Loguear el error completo en el servidor
      logger.error("Error mostrando la gestión de la colección", e);
      // Construir stacktrace y filtrar líneas que mencionen la dependencia (commons-text/StringEscapeUtils)
      java.io.StringWriter sw = new java.io.StringWriter();
      java.io.PrintWriter pw = new java.io.PrintWriter(sw);
      e.printStackTrace(pw);
      String fullTrace = sw.toString();
      StringBuilder filtered = new StringBuilder();
      String[] lines = fullTrace.split("\\r?\\n");
      for (String line : lines) {
        if (line.contains("org.apache.commons.text") || line.contains("StringEscapeUtils") || line.contains("commons-text")) {
          continue; // omitimos líneas asociadas a la dependencia
        }
        filtered.append(line).append("\n");
      }
      // Escape manual básico para HTML
      String traceEscaped = filtered.toString().replace("&", "&amp;")
          .replace("<", "&lt;")
          .replace(">", "&gt;")
          .replace("\"", "&quot;");
      ctx.status(500).html("<h2>Error al mostrar la gestión de la colección</h2><pre>" + traceEscaped + "</pre>");
    }
  }
}
