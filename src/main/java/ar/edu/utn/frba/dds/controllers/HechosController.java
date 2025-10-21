package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.model.dominio.Origen;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import ar.edu.utn.frba.dds.repositorios.RepositorioFuentes;
import ar.edu.utn.frba.dds.repositorios.RepositorioUsuarios;
import io.javalin.http.Context;

import java.util.*;

public class HechosController {

  public Map<String, Object> mostrarHechos(Context ctx) {
    Map<String, Object> modelo = new HashMap<>();
    List<Hecho> hechos = new ArrayList<>();

    try {
      List<Fuente> fuentes = RepositorioFuentes.getInstancia().listarTodas();

      for (Fuente fuente : fuentes) {
        try {
          List<Hecho> hechosFuente = fuente.cargarHechos(new ParametrosConsulta());
          hechos.addAll(hechosFuente);
        } catch (Exception ignored) {
        }
      }

      modelo.put("hechos", hechos);

    } catch (Exception e) {
      modelo.put("error", "Error al obtener los hechos.");
      ctx.status(500).result(e.getMessage());
    }

    return modelo;
  }


  public void hechosParaMapa(Context ctx) {
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

  // DTO to map incoming JSON
  public static class CreateHechoRequest {
    public String titulo;
    public String descripcion;
    public String categoria;
    public Double latitud;
    public Double longitud;
    public String fechaAcontecimiento; // ISO-8601 string

    public CreateHechoRequest() {
    }
  }

  public void crearHecho(Context ctx) {
    try {
      String userId = ctx.pathParam("id");
      Contribuyente contribuyente = RepositorioUsuarios.getInstancia().buscarPorId(Long.parseLong(userId));
      System.out.println("Creando hecho para el usuario: " + contribuyente.getNombre());

      CreateHechoRequest req = null;
      try {
        req = ctx.bodyAsClass(CreateHechoRequest.class);
        System.out.println("Request parsed as JSON into CreateHechoRequest.");
      } catch (Exception ex) {
        System.out.println("No JSON body parsed (or invalid JSON)." + ex.getMessage());
      }

      assert req != null;
      String titulo = req.titulo;
      String descripcion = req.descripcion;
      String categoria = req.categoria;
      double latitud = req.latitud;
      double longitud = req.longitud;
      LocalDateTime fechaAcontecimiento = LocalDateTime.parse(req.fechaAcontecimiento);
      LocalDateTime fechaCarga = LocalDateTime.now();
      Origen origen = Origen.CARGAMANUAL;

      HechoBuilder hechoBuilder = new HechoBuilder().titulo(titulo)
          .categoria(categoria)
          .descripcion(descripcion)
          .latitud(latitud)
          .longitud(longitud)
          .fechaAcontecimiento(fechaAcontecimiento)
          .fechaCarga(fechaCarga)
          .origen(origen);

      HechoDinamico hechoDinamico = new HechoDinamico(hechoBuilder, contribuyente);

      FuenteDinamica fuente = new FuenteDinamica();
      fuente.subirHecho(hechoDinamico);

      ctx.status(201).result("Hecho creado exitosamente.");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error al crear el hecho: " + e.getMessage());
    }
  }
}
