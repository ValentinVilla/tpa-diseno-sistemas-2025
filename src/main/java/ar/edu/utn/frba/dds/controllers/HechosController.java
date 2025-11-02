package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.helpers.FiltroHelper;
import ar.edu.utn.frba.dds.helpers.MapperHelper;
import ar.edu.utn.frba.dds.helpers.NotificacionesHelper;
import ar.edu.utn.frba.dds.helpers.SesionHelper;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.model.dominio.Origen;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.model.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import ar.edu.utn.frba.dds.repositorios.RepositorioFuentes;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudEliminacion;
import io.javalin.http.Context;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HechosController {

  public void mostrarVistaHechos(Context ctx) {
    Map<String, Object> modelo = SesionHelper.crearModeloBase(ctx);

    try {
      ParametrosConsulta filtros = FiltroHelper.armarFiltro(ctx);
      List<Hecho> hechos = obtenerHechos(filtros);

      modelo.put("cantidad", hechos.size());
      modelo.put("hechos", hechos);
      modelo.put("notificacion", NotificacionesHelper.crearNotificacion(ctx));

      ctx.render("hechos.hbs", modelo);

    } catch (Exception e) {
      modelo.put("error","Error al obtener los hechos." );
      ctx.status(500).result(e.getMessage());
    }
  }

  public void mostrarHechosMapa(Context ctx) {
    try {
      ParametrosConsulta filtros = FiltroHelper.armarFiltro(ctx);
      List<Hecho> hechos = obtenerHechos(filtros);
      ctx.json(MapperHelper.convertirHechosAJson(hechos));
    } catch (Exception e) {
      ctx.status(500).result(e.getMessage());
    }
  }

  public void mostrarFormularioNuevoHecho(Context ctx) {
    Map<String, Object> modelo = SesionHelper.crearModeloBase(ctx);

    modelo.put("fuentesDinamicas", RepositorioFuentes.getInstancia().listarFuentesDinamicas());
    modelo.put("notificacion", NotificacionesHelper.crearNotificacion(ctx));

    ctx.render("crear-hecho.hbs", modelo);
  }

  public void crearHecho(Context ctx) {
    try {
      Contribuyente usuario = ctx.sessionAttribute("usuario_logueado");
      Long fuenteId = parsearId(ctx.formParam("fuenteId"), "/hechos/nuevo?creacion=error", ctx);
      if (fuenteId == null) return;

      FuenteDinamica fuente = RepositorioFuentes.getInstancia().buscarFuenteDinamicaPorId(fuenteId);
      if (fuente == null) {
        ctx.redirect("/hechos/nuevo?creacion=error");
        return;
      }

      HechoBuilder builder = construirHechoBuilder(ctx);
      fuente.subirHecho(new HechoDinamico(builder, usuario));

      ctx.status(201);
      ctx.redirect("/hechos?creacion=exito");

    } catch (Exception e) {
      ctx.status(500);
      ctx.redirect("/hechos/nuevo?creacion=error");
    }
  }

  public void mostrarFormularioEliminacion(Context ctx) {
    Map<String, Object> modelo = SesionHelper.crearModeloBase(ctx);

    modelo.put("hecho", Map.of(
        "titulo", Objects.requireNonNull(ctx.queryParam("titulo")),
        "descripcion", Objects.requireNonNull(ctx.queryParam("descripcion")),
        "categoria", Objects.requireNonNull(ctx.queryParam("categoria"))
    ));

    ctx.render("solicitud-eliminacion.hbs", modelo);
  }

  public void crearSolicitudEliminacion(Context ctx) {
    try {
      Contribuyente usuario = ctx.sessionAttribute("usuario_logueado");

      HechoDinamico hechoTemporal = new HechoDinamico(
          new HechoBuilder()
              .titulo(ctx.formParam("titulo"))
              .descripcion(ctx.formParam("descripcion"))
              .categoria(ctx.formParam("categoria")),
          usuario
      );

      SolicitudEliminacion solicitud = new SolicitudEliminacion(
          hechoTemporal,
          ctx.formParam("justificacion"),
          texto -> false // TODO: reemplazar con detector real
      );

      RepositorioSolicitudes.getInstancia().guardar(solicitud);
      ctx.redirect("/hechos?solicitud=exito");

    } catch (Exception e) {
      ctx.redirect("/hechos?solicitud=error");
    }
  }

  private Long parsearId(String idStr, String redirectUrl, Context ctx) {
    try {
      if (idStr == null || idStr.isBlank()) {
        ctx.status(400).redirect(redirectUrl);
        return null;
      }
      return Long.parseLong(idStr);
    } catch (NumberFormatException e) {
      ctx.status(400).redirect(redirectUrl);
      return null;
    }
  }

  private HechoBuilder construirHechoBuilder(Context ctx) {
    return new HechoBuilder()
        .titulo(ctx.formParam("titulo"))
        .categoria(ctx.formParam("categoria"))
        .descripcion(ctx.formParam("descripcion"))
        .latitud(Double.parseDouble(Objects.requireNonNull(ctx.formParam("latitud"))))
        .longitud(Double.parseDouble(Objects.requireNonNull(ctx.formParam("longitud"))))
        .fechaAcontecimiento(LocalDateTime.parse(Objects.requireNonNull(ctx.formParam("fechaAcontecimiento"))))
        .fechaCarga(LocalDateTime.now())
        .origen(Origen.CARGAMANUAL);
  }

  private List<Hecho> obtenerHechos(ParametrosConsulta filtros) {
    return RepositorioFuentes.getInstancia().listarTodas().stream()
        .flatMap(f -> {
          try {
            return f.cargarHechos(filtros).stream();
          } catch (Exception ignored) {
            return Stream.empty();
          }
        })
        .collect(Collectors.toList());
  }


}