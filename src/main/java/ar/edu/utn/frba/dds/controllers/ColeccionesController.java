package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColeccionesController {
  public Map<String, Object> mostrarColecciones() {
    List<Coleccion> colecciones = RepositorioColecciones.getInstancia().listarTodas();

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("colecciones", colecciones);

    return modelo;
  }
}
