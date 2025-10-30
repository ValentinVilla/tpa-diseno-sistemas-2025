package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.javalin.http.Context;

public class ColeccionesController {
  public Map<String, Object> mostrarColecciones(Context ctx) {
    String busqueda = ctx.queryParam("q");
    List<Coleccion> colecciones = new ArrayList<>();

    if (busqueda == null){
      colecciones = RepositorioColecciones.getInstancia().listarTodas();
    } else{
      ParametrosConsulta parametrosConsulta = new ParametrosConsulta();
      parametrosConsulta.setTexto(busqueda);
      colecciones = RepositorioColecciones.getInstancia().listarColecciones(parametrosConsulta);
    }

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("cantidad",  colecciones.size());
    modelo.put("colecciones", colecciones);

    return modelo;
  }
}
