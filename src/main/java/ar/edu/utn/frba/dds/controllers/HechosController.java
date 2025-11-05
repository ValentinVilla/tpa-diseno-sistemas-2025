package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.helpers.FiltroHelper;
import ar.edu.utn.frba.dds.helpers.HechoHelper;
import ar.edu.utn.frba.dds.helpers.MapperHelper;
import ar.edu.utn.frba.dds.helpers.NotificacionesHelper;
import ar.edu.utn.frba.dds.helpers.SesionHelper;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.model.dominio.Media;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.model.fuentes.fuenteDinamica.FuenteDinamica;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import ar.edu.utn.frba.dds.repositorios.DAOHechos;
import ar.edu.utn.frba.dds.repositorios.RepositorioFuentes;
import ar.edu.utn.frba.dds.repositorios.RepositorioMedia;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudEliminacion;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HechosController {
  private static final int MAX_FILES = 10;
  private static final long MAX_FILE_SIZE = 20L * 1024L * 1024L;
  private static final Path UPLOADS_DIR = Paths.get("uploads");

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
      modelo.put("error", "Error al obtener los hechos.");
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

  public void crearHecho(Context ctx) {
    try {
      Contribuyente contribuyente = ctx.sessionAttribute("usuario_logueado");

      if (contribuyente != null) {
        System.out.println("Creando hecho para el usuario: " + contribuyente.getNombre());
      } else {
        System.out.println("Creando hecho anónimo.");
      }

      HechoBuilder hechoBuilder = HechoHelper.construirBuilder(ctx);
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
        if (!uploaded.isEmpty()) {
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

        RepositorioFuentes.getInstancia().agregarHechoALaFuente(fuenteId, hechoDinamico, contribuyente);

        ctx.status(201);
        ctx.redirect("/hechos?creacion=exito");
      } catch (Exception e) {
        cleanupStoredFiles(storedOnDisk);
        throw e;
      }
    } catch (Exception e) {
      ctx.status(500);
      ctx.redirect("/hechos/nuevo?error=true");
    }
  }

  private String storeUploadedFile(UploadedFile uf) throws IOException {
    String original = uf.filename();
    String ext = "";
    int idx = original.lastIndexOf('.');
    if (idx >= 0) ext = original.substring(idx);
    String storedName = UUID.randomUUID() + ext;
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

  public void mostrarFormularioNuevoHecho(Context ctx) {
    Map<String, Object> modelo = SesionHelper.crearModeloBase(ctx);

    modelo.put("fuentesDinamicas", RepositorioFuentes.getInstancia().listarFuentesDinamicas());
    modelo.put("notificacion", NotificacionesHelper.crearNotificacion(ctx));

    ctx.render("crear-hecho.hbs", modelo);
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
          texto -> false,
          usuario
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

  public void mostrarHecho(Context ctx) {
    long id;
    try {
      id = Long.parseLong(ctx.pathParam("id"));
    } catch (NumberFormatException e) {
      ctx.status(400).result("Id inválido");
      return;
    }

    HechoDinamico hecho = DAOHechos.getInstancia().buscarPorId(id);
    if (hecho == null) {
      ctx.status(404).result("Hecho no encontrado");
      return;
    }

    List<Media> mediaEntities = RepositorioMedia.getInstancia().findByHechoId(hecho.getId());

    String uploadsDirProp = System.getProperty("UPLOADS_DIR", System.getenv().getOrDefault("UPLOADS_DIR", "uploads"));
    Path uploadsDir = Paths.get(uploadsDirProp);

    List<Map<String, Object>> medias = mediaEntities.stream().map(m -> {
      String path = m.getPath();
      String basename = Paths.get(path.startsWith("/") ? path.substring(1) : path).getFileName().toString();
      long sizeBytes = 0L;
      try {
        Path file = uploadsDir.resolve(basename);
        if (Files.exists(file)) sizeBytes = Files.size(file);
      } catch (IOException ignored) {
      }
      boolean isVideo = basename.toLowerCase().matches(".*\\.(mp4|webm|ogg)$");
      Map<String, Object> mm = new HashMap<>();
      mm.put("path", path);
      mm.put("basename", basename);
      mm.put("humanSize", humanReadableSize(sizeBytes));
      mm.put("isVideo", isVideo);
      return mm;
    }).collect(Collectors.toList());

    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    Map<String, Object> model = new HashMap<>();
    model.put("hecho", hecho);
    model.put("fechaAcontecimiento", hecho.getFechaAcontecimiento() != null ? hecho.getFechaAcontecimiento().format(fmt) : "");
    model.put("fechaCarga", hecho.getFechaCarga() != null ? hecho.getFechaCarga().format(fmt) : "");
    model.put("medias", medias);

    if (!hecho.esAnonimo() && hecho.getContribuyente() != null) {
      model.put("contribuyenteNombre", hecho.getContribuyente().getNombre());
    }

    ctx.render("mostrar-hecho.hbs", model);
  }

  private String humanReadableSize(long bytes) {
    if (bytes < 1024) return bytes + " B";
    if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
    return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
  }
}