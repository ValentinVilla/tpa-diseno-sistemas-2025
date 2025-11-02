package ar.edu.utn.frba.dds.helpers;

import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.model.filtros.FiltroFecha;
import ar.edu.utn.frba.dds.model.filtros.FiltroTexto;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.model.fuentes.fuenteEstatica.FuenteEstatica;
import ar.edu.utn.frba.dds.model.fuentes.fuenteProxy.FuenteDemo;
import ar.edu.utn.frba.dds.model.fuentes.fuenteProxy.FuenteMetaMapa;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapperHelper {
  public static List<Map<String, Object>> convertirHechosAJson(List<Hecho> hechos) {
    List<Map<String, Object>> hechosParaJS = new ArrayList<>();
    for (Hecho h : hechos) {
      if (h.getLatitud() != null && h.getLongitud() != null) {
        Map<String, Object> json = new HashMap<>();
        json.put("titulo", h.getTitulo());
        json.put("categoria", h.getCategoria());
        json.put("fecha", h.getFechaAcontecimiento().toString());
        json.put("descripcion", h.getDescripcion());
        json.put("lat", h.getLatitud());
        json.put("lon", h.getLongitud());
        hechosParaJS.add(json);
      }
    }
    return hechosParaJS;
  }

  public static String obtenerTipoFuente(Fuente fuente) {
    if (fuente == null) return "Desconocida";
    if (fuente instanceof FuenteDinamica) return "Fuente Dinámica";
    if (fuente instanceof FuenteEstatica) return "Fuente Estática (CSV)";
    if (fuente instanceof FuenteMetaMapa) return "Fuente Proxy (MetaMapa)";
    if (fuente instanceof FuenteDemo) return "Fuente Demo";
    return fuente.getClass().getSimpleName();
  }

  public static String obtenerInfoCriterio(Filtro criterio) {
    if (criterio == null) return "Sin criterio específico";

    if (criterio instanceof FiltroCategoria filtroCat) {
      try {
        java.lang.reflect.Field campoCategoria = FiltroCategoria.class.getDeclaredField("categoria");
        campoCategoria.setAccessible(true);
        String categoria = (String) campoCategoria.get(filtroCat);
        return "Categoría: " + (categoria != null ? categoria : "No especificada");
      } catch (Exception e) {
        return "Filtro por categoría";
      }
    }

    if (criterio instanceof FiltroFecha filtroFecha) {
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
