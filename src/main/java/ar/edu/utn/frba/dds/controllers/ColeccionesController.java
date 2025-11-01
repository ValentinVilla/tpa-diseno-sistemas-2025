package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.model.filtros.FiltroFecha;
import ar.edu.utn.frba.dds.model.filtros.FiltroTexto;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.model.fuentes.fuenteEstatica.FuenteEstatica;
import ar.edu.utn.frba.dds.model.fuentes.fuenteProxy.FuenteDemo;
import ar.edu.utn.frba.dds.model.fuentes.fuenteProxy.FuenteMetaMapa;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.javalin.http.Context;

public class ColeccionesController {
  public Map<String, Object> mostrarColecciones(Context ctx) {
    String busqueda = ctx.queryParam("q");
    List<Coleccion> colecciones = new ArrayList<>();

    if (busqueda == null) {
      colecciones = RepositorioColecciones.getInstancia().listarTodas();
    } else {
      ParametrosConsulta parametrosConsulta = new ParametrosConsulta();
      parametrosConsulta.setTexto(busqueda);
      colecciones = RepositorioColecciones.getInstancia().listarColecciones(parametrosConsulta);
    }

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("cantidad", colecciones.size());
    modelo.put("colecciones", colecciones);
    Contribuyente usuario = ctx.sessionAttribute("usuario_logueado");
    if (usuario != null) {
      modelo.put("nombre", usuario.getNombre());
    }

    return modelo;
  }

  public void mostrarColeccionPorId(Context ctx) {
    try {
      String idStr = ctx.pathParam("id");
      Long id = Long.parseLong(idStr);

      Coleccion coleccion = RepositorioColecciones.getInstancia().buscarPorID(id);

      if (coleccion == null) {
        ctx.status(404).redirect("/colecciones");
        return;
      }

      List<Hecho> hechos = coleccion.mostrarHechos();

      Map<String, Object> modelo = new HashMap<>();
      modelo.put("coleccion", coleccion);
      modelo.put("hechos", hechos);
      modelo.put("cantidad", hechos.size());
      modelo.put("coleccionId", id);

      // Información adicional para el resumen
      modelo.put("tipoFuente", obtenerTipoFuente(coleccion.getFuente()));
      modelo.put("infoCriterio", obtenerInfoCriterio(coleccion.getCriterio()));
      try {
        java.lang.reflect.Field campoAlgoritmo = Coleccion.class.getDeclaredField("algoritmoConsenso");
        campoAlgoritmo.setAccessible(true);
        Object algoritmo = campoAlgoritmo.get(coleccion);
        modelo.put("algoritmoConsenso", algoritmo != null ? algoritmo.toString() : "Sin consenso");
      } catch (Exception e) {
        modelo.put("algoritmoConsenso", null);
      }

      Contribuyente usuario = ctx.sessionAttribute("usuario_logueado");
      if (usuario != null) {
        modelo.put("nombre", usuario.getNombre());
      }

      ctx.render("coleccion-detalle.hbs", modelo);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      ctx.status(400).redirect("/colecciones");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).redirect("/colecciones");
    }
  }

  public void mostrarHechosColeccionJson(Context ctx) {
    try {
      String idStr = ctx.pathParam("id");
      Long id = Long.parseLong(idStr);

      Coleccion coleccion = RepositorioColecciones.getInstancia().buscarPorID(id);

      if (coleccion == null) {
        ctx.status(404).json(new ArrayList<>());
        return;
      }

      List<Hecho> hechos = coleccion.mostrarHechos();
      List<Map<String, Object>> hechosJS = parserHechosJson(hechos);
      ctx.json(hechosJS);
    } catch (Exception e) {
      ctx.status(500).json(new ArrayList<>());
    }
  }

  private List<Map<String, Object>> parserHechosJson(List<Hecho> hechos) {
    List<Map<String, Object>> hechosParaJS = new ArrayList<>();
    for (Hecho h : hechos) {
      if (h.getLatitud() != null && h.getLongitud() != null) {
        Map<String, Object> json = new HashMap<>();
        json.put("titulo", h.getTitulo());
        json.put("categoria", h.getCategoria());
        json.put("fecha", h.getFechaAcontecimiento() != null ? h.getFechaAcontecimiento().toString() : "");
        json.put("descripcion", h.getDescripcion());
        json.put("lat", h.getLatitud());
        json.put("lon", h.getLongitud());
        hechosParaJS.add(json);
      }
    }
    return hechosParaJS;
  }

  private String obtenerTipoFuente(Fuente fuente) {
    if (fuente == null) return "Desconocida";
    if (fuente instanceof FuenteDinamica) return "Fuente Dinámica";
    if (fuente instanceof FuenteEstatica) return "Fuente Estática (CSV)";
    if (fuente instanceof FuenteMetaMapa) return "Fuente Proxy (MetaMapa)";
    if (fuente instanceof FuenteDemo) return "Fuente Demo";
    return fuente.getClass().getSimpleName();
  }

  private String obtenerInfoCriterio(Filtro criterio) {
    if (criterio == null) return "Sin criterio específico";
    
    if (criterio instanceof FiltroCategoria) {
      FiltroCategoria filtroCat = (FiltroCategoria) criterio;
      try {
        java.lang.reflect.Field campoCategoria = FiltroCategoria.class.getDeclaredField("categoria");
        campoCategoria.setAccessible(true);
        String categoria = (String) campoCategoria.get(filtroCat);
        return "Categoría: " + (categoria != null ? categoria : "No especificada");
      } catch (Exception e) {
        return "Filtro por categoría";
      }
    }
    
    if (criterio instanceof FiltroFecha) {
      FiltroFecha filtroFecha = (FiltroFecha) criterio;
      try {
        java.lang.reflect.Field campoDesde = FiltroFecha.class.getDeclaredField("fechaDesde");
        java.lang.reflect.Field campoHasta = FiltroFecha.class.getDeclaredField("fechaHasta");
        campoDesde.setAccessible(true);
        campoHasta.setAccessible(true);
        java.time.LocalDate fechaDesde = (java.time.LocalDate) campoDesde.get(filtroFecha);
        java.time.LocalDate fechaHasta = (java.time.LocalDate) campoHasta.get(filtroFecha);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (fechaDesde != null && fechaHasta != null) {
          return "Rango de fechas: " + fechaDesde.format(formatter) + " - " + fechaHasta.format(formatter);
        } else if (fechaDesde != null) {
          return "Desde: " + fechaDesde.format(formatter);
        } else if (fechaHasta != null) {
          return "Hasta: " + fechaHasta.format(formatter);
        }
        return "Filtro por fecha";
      } catch (Exception e) {
        return "Filtro por fecha";
      }
    }
    
    if (criterio instanceof FiltroTexto) {
      return "Filtro por texto";
    }
    
    return criterio.getClass().getSimpleName();
  }
}
