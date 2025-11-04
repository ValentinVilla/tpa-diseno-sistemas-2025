package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.dominio.*;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.utn.frba.dds.repositorios.*;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.model.DetectorSpam.DetectorDeSpam;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;

public class HechosController {

  private static final int MAX_FILES = 10;
  private static final long MAX_FILE_SIZE = 20L * 1024L * 1024L; // 20 MB
  private static final Path UPLOADS_DIR = Paths.get("uploads");
  private static final Set<String> ALLOWED_PREFIXES = Set.of("image/", "video/");

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

      String fuenteIdStr = ctx.formParam("fuenteId");
      if (fuenteIdStr == null || fuenteIdStr.isBlank()) {
        ctx.status(400);
        ctx.redirect("/hechos/nuevo?error=true");
        return;
      }

      Long fuenteId;
      try {
        fuenteId = Long.parseLong(fuenteIdStr);
      } catch (NumberFormatException nfe) {
        ctx.status(400);
        ctx.redirect("/hechos/nuevo?error=true");
        return;
      }

      FuenteDinamica fuente = RepositorioFuentes.getInstancia().buscarFuenteDinamicaPorId(fuenteId);
      if (fuente == null) {
        ctx.status(400);
        ctx.redirect("/hechos/nuevo?error=fuente_no_encontrada");
        return;
      }

      List<UploadedFile> uploaded = ctx.uploadedFiles("medias");
      List<Path> storedOnDisk = new ArrayList<>();
      try {
        if (uploaded != null && !uploaded.isEmpty()) {
          if (uploaded.size() > MAX_FILES) {
            ctx.status(400);
            ctx.redirect("/hechos/nuevo?error=max_files");
            return;
          }

          try {
            Files.createDirectories(UPLOADS_DIR);
          } catch (IOException e) {
            throw new RuntimeException("No se pudo crear directorio de uploads", e);
          }

          for (UploadedFile uf : uploaded) {
            /*String contentType = uf.contentType();
            if (contentType == null || ALLOWED_PREFIXES.stream().noneMatch(contentType::startsWith)) {
              cleanupStoredFiles(storedOnDisk);
              ctx.status(400);
              ctx.redirect("/hechos/nuevo?error=tipo_no_permitido");
              return;
            }*/

            String publicPath = storeUploadedFile(uf);
            Path storedPath = resolveServerPath(publicPath);
            storedOnDisk.add(storedPath);

            try {
              long size = Files.size(storedPath);
              if (size > MAX_FILE_SIZE) {
                cleanupStoredFiles(storedOnDisk);
                ctx.status(400);
                ctx.redirect("/hechos/nuevo?error=file_too_large");
                return;
              }
            } catch (IOException ioe) {
              cleanupStoredFiles(storedOnDisk);
              throw new RuntimeException("No se pudo leer tamaño de archivo guardado", ioe);
            }

            hechoDinamico.addMedia(publicPath);
          }
        }

        RepositorioFuentes.getInstancia().agregarHechoALaFuente(fuenteId, hechoDinamico);

        ctx.status(201);
        ctx.redirect("/hechos?creacion=exito");
      } catch (Exception e) {
        cleanupStoredFiles(storedOnDisk);
        throw e;
      }
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500);
      ctx.redirect("/hechos/nuevo?error=true");
    }
  }

  private String storeUploadedFile(UploadedFile uf) throws IOException {
    String original = uf.filename();
    String ext = "";
    if (original != null) {
      int idx = original.lastIndexOf('.');
      if (idx >= 0) ext = original.substring(idx);
    }
    String storedName = UUID.randomUUID().toString() + ext;
    Path target = UPLOADS_DIR.resolve(storedName);

    try (InputStream in = uf.content()) {
      Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
    }

    return "/uploads/" + storedName;
  }

  private Path resolveServerPath(String publicPath) {
    String maybe = publicPath.startsWith("/") ? publicPath.substring(1) : publicPath;
    return UPLOADS_DIR.resolve(Paths.get(maybe).getFileName());
  }

  private void cleanupStoredFiles(List<Path> paths) {
    for (Path p : paths) {
      try {
        Files.deleteIfExists(p);
      } catch (Exception ex) {
        System.err.println("No se pudo borrar archivo en limpieza: " + p + " -> " + ex.getMessage());
      }
    }
  }

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

      String solicitudStatus = ctx.queryParam("solicitud");
      if (solicitudStatus != null) {
        Map<String, String> notificacion = new HashMap<>();
        if (solicitudStatus.equals("exito")) {
          notificacion.put("tipo", "exito");
          notificacion.put("mensaje", "Solicitud enviada correctamente.");
        }
        modelo.put("notificacion", notificacion);
      }

      String creacionStatus = ctx.queryParam("creacion");
      if (creacionStatus != null && creacionStatus.equals("exito")) {
        Map<String, String> notificacion = new HashMap<>();
        notificacion.put("tipo", "exito");
        notificacion.put("mensaje", "Hecho creado correctamente.");
        modelo.put("notificacion", notificacion);
      }

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

  public void mostrarFormularioNuevoHecho(Context ctx) {
    List<FuenteDinamica> fuentesDinamicas = RepositorioFuentes.getInstancia().listarFuentesDinamicas();

    Map<String, Object> model = new HashMap<>();
    model.put("fuentesDinamicas", fuentesDinamicas);

    Contribuyente usuario = ctx.sessionAttribute("usuario_logueado");
    if (usuario != null) {
      model.put("nombre", usuario.getNombre());
    }

    ctx.render("crear-hecho.hbs", model);
  }

  public void mostrarFormularioEliminacion(Context ctx) {
    Map<String, Object> model = new HashMap<>();

    Contribuyente usuario = ctx.sessionAttribute("usuario_logueado");
    if (usuario != null) {
      model.put("nombre", usuario.getNombre());
    }

    Map<String, String> hecho = new HashMap<>();
    hecho.put("titulo", ctx.queryParam("titulo"));
    hecho.put("descripcion", ctx.queryParam("descripcion"));
    hecho.put("categoria", ctx.queryParam("categoria"));

    model.put("hecho", hecho);

    ctx.render("solicitud-eliminacion.hbs", model);
  }

  public void crearSolicitudEliminacion(Context ctx) {
    try {
      String titulo = ctx.formParam("titulo");
      String descripcion = ctx.formParam("descripcion");
      String categoria = ctx.formParam("categoria");
      String justificacion = ctx.formParam("justificacion");

      Contribuyente contribuyente = ctx.sessionAttribute("usuario_logueado");

      HechoDinamico hechoTemporal = new HechoDinamico(
          new HechoBuilder()
              .titulo(titulo)
              .descripcion(descripcion)
              .categoria(categoria),
          contribuyente
      );

      DetectorDeSpam detectorSiempreFalse = texto -> false; // TODO: chequear q carajo hacer con el detector de spam

      SolicitudEliminacion solicitud = new SolicitudEliminacion(
          hechoTemporal,
          justificacion,
          detectorSiempreFalse
      );

      RepositorioSolicitudes.getInstancia().guardar(solicitud);

      ctx.status(201);
      ctx.redirect("/hechos?solicitud=exito");
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).result("Error al procesar la solicitud: " + e.getMessage());
      ctx.redirect("/hechos?solicitud=error");
    }
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

  public void mostrarHecho(Context ctx) {
    Long id;
    try {
      id = Long.parseLong(ctx.pathParam("id"));
    } catch (NumberFormatException e) {
      ctx.status(400).result("Id inválido");
      return;
    }

    Hecho hecho = DAOHechos.getInstancia().buscarPorId(id);
    if (hecho == null) {
      ctx.status(404).result("Hecho no encontrado");
      return;
    }

    List<Media> mediaEntities = RepositorioMedia.getInstancia().findByHechoId(hecho.getId());

    String uploadsDirProp = System.getProperty("UPLOADS_DIR", System.getenv().getOrDefault("UPLOADS_DIR", "uploads"));
    Path uploadsDir = Paths.get(uploadsDirProp);

    List<Map<String,Object>> medias = mediaEntities.stream().map(m -> {
      String path = m.getPath();
      String basename = Paths.get(path.startsWith("/") ? path.substring(1) : path).getFileName().toString();
      long sizeBytes = 0L;
      try {
        Path file = uploadsDir.resolve(basename);
        if (Files.exists(file)) sizeBytes = Files.size(file);
      } catch (IOException ignored) {}
      boolean isVideo = basename.toLowerCase().matches(".*\\.(mp4|webm|ogg)$");
      Map<String,Object> mm = new HashMap<>();
      mm.put("path", path);
      mm.put("basename", basename);
      mm.put("humanSize", humanReadableSize(sizeBytes));
      mm.put("isVideo", isVideo);
      return mm;
    }).collect(Collectors.toList());

    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    Map<String,Object> model = new HashMap<>();
    model.put("hecho", hecho);
    model.put("fechaAcontecimiento", hecho.getFechaAcontecimiento() != null ? hecho.getFechaAcontecimiento().format(fmt) : "");
    model.put("fechaCarga", hecho.getFechaCarga() != null ? hecho.getFechaCarga().format(fmt) : "");
    model.put("medias", medias);

    if (hecho instanceof HechoDinamico) {
      HechoDinamico hd = (HechoDinamico) hecho;
      if (!hd.esAnonimo() && hd.getContribuyente() != null) {
        model.put("contribuyenteNombre", hd.getContribuyente().getNombre());
      }
    }

    ctx.render("mostrar-hecho.hbs", model);
  }

  private String humanReadableSize(long bytes) {
    if (bytes < 1024) return bytes + " B";
    if (bytes < 1024*1024) return String.format("%.1f KB", bytes / 1024.0);
    return String.format("%.2f MB", bytes / (1024.0*1024.0));
  }
}