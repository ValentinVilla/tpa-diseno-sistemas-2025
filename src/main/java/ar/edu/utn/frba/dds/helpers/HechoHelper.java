package ar.edu.utn.frba.dds.helpers;

import ar.edu.utn.frba.dds.model.dominio.Origen;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.util.Objects;

public class HechoHelper {
  public static HechoBuilder construirBuilder(Context ctx) {
    String titulo = ctx.formParam("titulo");
    String descripcion = ctx.formParam("descripcion");
    String categoria = ctx.formParam("categoria");
    double latitud = Double.parseDouble(Objects.requireNonNull(ctx.formParam("latitud")));
    double longitud = Double.parseDouble(Objects.requireNonNull(ctx.formParam("longitud")));
    LocalDateTime fechaAcontecimiento = LocalDateTime.parse(Objects.requireNonNull(ctx.formParam("fechaAcontecimiento")));

    LocalDateTime fechaCarga = LocalDateTime.now();

    return new HechoBuilder().titulo(titulo)
        .categoria(categoria)
        .descripcion(descripcion)
        .latitud(latitud)
        .longitud(longitud)
        .fechaAcontecimiento(fechaAcontecimiento)
        .fechaCarga(fechaCarga)
        .origen(Origen.CARGAMANUAL);
  }
}
