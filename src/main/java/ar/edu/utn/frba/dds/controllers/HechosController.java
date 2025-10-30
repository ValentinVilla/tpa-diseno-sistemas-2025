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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import ar.edu.utn.frba.dds.repositorios.RepositorioFuentes;
import io.javalin.http.Context;

import java.util.*;

public class HechosController {

  public void mostrarHechos(Context ctx) {
    Map<String, Object> modelo = new HashMap<>();

    try {
      List<Coleccion> colecciones = RepositorioColecciones.getInstancia().listarTodas(); 
      ParametrosConsulta filtros = this.armarFiltro(ctx);
      List<Hecho> hechos = this.getHechos(filtros);
      modelo.put("cantidad", hechos.size());

      modelo.put("hechos", hechos);
      
      modelo.put("colecciones", colecciones);

      Contribuyente usuario = ctx.sessionAttribute("usuario_logueado");
      if (usuario != null) modelo.put("nombre", usuario.getNombre());

      ctx.render("hechos.hbs", modelo);
    } catch (Exception e) {
      modelo.put("error", "Error al obtener los hechos.");
      ctx.status(500).result(e.getMessage());
    }
  }

  public void mostrarHechosMapa(Context ctx) {
    try {
      ParametrosConsulta filtros = this.armarFiltro(ctx);
      List<Hecho> hechos = this.getHechos(filtros);

      List<Map<String, Object>> hechosJS = parserHechosJson(hechos);
      ctx.json(hechosJS);
    } catch (Exception e) {
      ctx.status(500).result(e.getMessage());
    }
  }

  public void crearHecho(Context ctx) {
    try {
      Contribuyente contribuyente = ctx.sessionAttribute("usuario_logueado");

      if (contribuyente != null) {
        System.out.println("Creando hecho para el usuario: " + contribuyente.getNombre());
      } else {
        System.out.println("Creando hecho anónimo.");
      }

      HechoBuilder hechoBuilder = this.construirBuilder(ctx);
      HechoDinamico hechoDinamico = new HechoDinamico(hechoBuilder, contribuyente);

      FuenteDinamica fuente = new FuenteDinamica();
      fuente.subirHecho(hechoDinamico);

      ctx.status(201);
      ctx.redirect("/hechos");
    } catch (Exception e) {
      ctx.status(500);
      ctx.redirect("/hechos/nuevo?error=true");
    }
  }

  public void mostrarFormularioNuevoHecho(Context ctx) {
    List<FuenteDinamica> fuentesDinamicas = RepositorioFuentes.getInstancia().listarFuentesDinamicas();

    Map<String, Object> model = new HashMap<>();
    model.put("fuentesDinamicas", fuentesDinamicas);

    ctx.render("crear-hecho.hbs", model);
  }

  private List<Hecho> getHechos(ParametrosConsulta filtros) {
    List<Hecho> hechos = new ArrayList<>();

    List<Fuente> fuentes = RepositorioFuentes.getInstancia().listarTodas();

    for (Fuente fuente : fuentes) {
      try {
        List<Hecho> hechosFuente = fuente.cargarHechos(filtros);
        hechos.addAll(hechosFuente);
      } catch (Exception ignored) {
      }
    }
    return hechos;
  }

  private ParametrosConsulta armarFiltro(Context ctx) {
    String busqueda = ctx.queryParam("busqueda");
    String categoria = ctx.queryParam("categoria");
    String fechaDesdeStr = ctx.queryParam("fechaDesde");
    String fechaHastaStr = ctx.queryParam("fechaHasta");

    ParametrosConsulta filtros = new ParametrosConsulta();
    if (!Objects.equals(busqueda, "")) {
      filtros.setTexto(busqueda);
    }
    if (!Objects.equals(categoria, "")) {
      filtros.setCategoria(categoria);
    }
    if (!Objects.equals(fechaDesdeStr, "") && fechaDesdeStr != null) {
      filtros.setFechaAcontecimientoDesde(LocalDate.parse(fechaDesdeStr));
    }
    if (!Objects.equals(fechaHastaStr, "") && fechaHastaStr != null) {
      filtros.setFechaAcontecimientoHasta(LocalDate.parse(fechaHastaStr));
    }

    return filtros;
  }

  private HechoBuilder construirBuilder(Context ctx) {
    String titulo = ctx.formParam("titulo");
    String descripcion = ctx.formParam("descripcion");
    String categoria = ctx.formParam("categoria");
    double latitud = Double.parseDouble(ctx.formParam("latitud"));
    double longitud = Double.parseDouble(ctx.formParam("longitud"));
    LocalDateTime fechaAcontecimiento = LocalDateTime.parse(ctx.formParam("fechaAcontecimiento"));

    LocalDateTime fechaCarga = LocalDateTime.now();
    Origen origen = Origen.CARGAMANUAL;

    return new HechoBuilder().titulo(titulo)
        .categoria(categoria)
        .descripcion(descripcion)
        .latitud(latitud)
        .longitud(longitud)
        .fechaAcontecimiento(fechaAcontecimiento)
        .fechaCarga(fechaCarga)
        .origen(origen);
  }

  private List<Map<String, Object>> parserHechosJson(List<Hecho> hechos) {
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
}
