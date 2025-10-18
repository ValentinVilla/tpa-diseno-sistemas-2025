package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.javalin.http.Context;

import java.util.*;

public class HechosController {

  public void mostrarHechos(Context ctx) {
    List<Coleccion> colecciones = RepositorioColecciones.getInstancia().listarTodas();
    List<Map<String, Object>> hechosParaJS = new ArrayList<>();

    for (Coleccion c : colecciones) {
      for (Hecho h : c.mostrarHechos()) {
        // Solo agregamos hechos que tengan lat/lon
        if (h.getLatitud() != null && h.getLongitud() != null) {
          Map<String, Object> json = new HashMap<>();
          json.put("titulo", h.getTitulo());
          json.put("categoria", h.getCategoria());
          json.put("fecha", h.getFechaAcontecimiento());
          json.put("descripcion", h.getDescripcion());
          json.put("lat", h.getLatitud());
          json.put("lon", h.getLongitud());
          hechosParaJS.add(json);
        }
      }
    }

    System.out.println(hechosParaJS);

    ctx.json(hechosParaJS); // devuelve JSON al frontend
  }
}
