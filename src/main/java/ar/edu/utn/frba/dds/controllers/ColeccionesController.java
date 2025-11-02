package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.helpers.MapperHelper;
import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.javalin.http.Context;

public class ColeccionesController {
  public Map<String, Object> mostrarColecciones(Context ctx) {
    String busqueda = ctx.queryParam("q");
    List<Coleccion> colecciones;

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

      List<Hecho> hechos = coleccion.mostrarHechos(null);

      Map<String, Object> modelo = new HashMap<>();
      modelo.put("coleccion", coleccion);
      modelo.put("hechos", hechos);
      modelo.put("cantidad", hechos.size());
      modelo.put("coleccionId", id);

      modelo.put("tipoFuente", MapperHelper.obtenerTipoFuente(coleccion.getFuente()));
      modelo.put("infoCriterio", MapperHelper.obtenerInfoCriterio(coleccion.getCriterio()));
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
      ctx.status(400).redirect("/colecciones");
    } catch (Exception e) {
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

      List<Hecho> hechos = coleccion.mostrarHechos(null);
      List<Map<String, Object>> hechosJS = MapperHelper.convertirHechosAJson(hechos);
      ctx.json(hechosJS);
    } catch (Exception e) {
      ctx.status(500).json(new ArrayList<>());
    }
  }
}