package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.utn.frba.dds.repositorios.RepositorioFuentes;
import io.javalin.http.Context;

import java.util.*;

public class HechosController {

  public void mostrarHechos(Context ctx) {
    List<Map<String, Object>> hechosParaJS = new ArrayList<>();

    try {
      List<Fuente> fuentes = RepositorioFuentes.getInstancia().listarTodas();

      for (Fuente f : fuentes) {
        try {
          for (Hecho h : f.cargarHechos(new ParametrosConsulta())) {
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
        } catch (Exception e) {
          System.err.println("⚠️ Error al cargar hechos desde la fuente: " + f.getFuente());
          e.printStackTrace();
        }
      }

      ctx.json(hechosParaJS);
    } catch (Exception e) {
      ctx.status(500).result("Error al obtener los hechos.");
    }
  }
}
