package ar.edu.utn.frba.dds.helpers;

import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.Optional;

public class FiltroHelper {
  public static ParametrosConsulta armarFiltro(Context ctx) {
    ParametrosConsulta filtros = new ParametrosConsulta();

    Optional.ofNullable(ctx.queryParam("busqueda"))
        .filter(s -> !s.isBlank())
        .ifPresent(filtros::setTexto);

    Optional.ofNullable(ctx.queryParam("categoria"))
        .filter(s -> !s.isBlank())
        .ifPresent(filtros::setCategoria);

    Optional.ofNullable(ctx.queryParam("fechaDesde"))
        .filter(s -> !s.isBlank())
        .ifPresent(f -> filtros.setFechaAcontecimientoDesde(LocalDate.parse(f)));

    Optional.ofNullable(ctx.queryParam("fechaHasta"))
        .filter(s -> !s.isBlank())
        .ifPresent(f -> filtros.setFechaAcontecimientoHasta(LocalDate.parse(f)));

    return filtros;
  }
}
