package ar.edu.utn.frba.dds.controllers;

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

import ar.edu.utn.frba.dds.repositorios.RepositorioFuentes;
import ar.edu.utn.frba.dds.repositorios.RepositorioUsuarios;
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

  // DTO to map incoming JSON
  public static class CreateHechoRequest {
    public String titulo;
    public String descripcion;
    public String categoria;
    public Double latitud;
    public Double longitud;
    public String fechaAcontecimiento; // ISO-8601 string

    public CreateHechoRequest() {}
  }

  public void crearHecho(Context ctx) {
    try {
      String userId = ctx.pathParam("id");
      Contribuyente contribuyente = RepositorioUsuarios.getInstancia().buscarPorId(Long.parseLong(userId));
      System.out.println("Creando hecho para el usuario: " + contribuyente.getNombre());

      String titulo = ctx.formParam("titulo");
      String descripcion = ctx.formParam("descripcion");
      String categoria = ctx.formParam("categoria");
      double latitud = Double.parseDouble(ctx.formParam("latitud"));
      double longitud = Double.parseDouble(ctx.formParam("longitud"));
      LocalDateTime fechaAcontecimiento = LocalDateTime.parse(ctx.formParam("fechaAcontecimiento"));

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

      ctx.status(201);
      ctx.redirect("/home?exi to=true");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500);
      ctx.redirect("/hechos/nuevo?error=true");
    }
  }

  public void mostrarFormularioNuevoHecho(Context ctx) {
    Contribuyente usuarioLogueado = ctx.sessionAttribute("usuario_logueado");
    if (usuarioLogueado == null) {
      ctx.redirect("/login");
      return;
    }
    Long idDelUsuario = usuarioLogueado.getId();
    Map<String, Object> model = new HashMap<>();
    model.put("userId", idDelUsuario);
    ctx.render("crear-hecho.hbs", model);
  }

}
