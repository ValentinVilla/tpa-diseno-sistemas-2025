package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HechoService {

  private final RepositorioHechos repositorio;

  public HechoService(RepositorioHechos repositorio) {
    this.repositorio = repositorio;
  }

  public List<Hecho> filtrarHechos(
      String categoria,
      LocalDate fechaReporteDesde,
      LocalDate fechaReporteHasta,
      LocalDate fechaAcontecimientoDesde,
      LocalDate fechaAcontecimientoHasta
  ) {
    return repositorio.obtenerTodos().stream()
        .filter(hecho -> (categoria == null || hecho.getCategoria().equals(categoria)))
        .filter(hecho -> (fechaReporteDesde == null || hecho.getFechaCarga().isAfter(fechaReporteDesde)))
        .filter(hecho -> (fechaReporteHasta == null || hecho.getFechaCarga().isBefore(fechaReporteHasta)))
        .filter(hecho -> (fechaAcontecimientoDesde == null || hecho.getFechaAcontecimiento().isAfter(fechaAcontecimientoDesde)))
        .filter(hecho -> (fechaAcontecimientoHasta == null || hecho.getFechaAcontecimiento().isBefore(fechaAcontecimientoHasta)))
        .collect(Collectors.toList());
  }

  public List<Hecho> obtenerTodos() {
    return repositorio.obtenerTodos();
  }

  public void eliminarHecho(Hecho hecho) {
    //buscar y eliminar el hecho
  }

  public void actualizarHecho(Hecho hecho) {
    //actualizar hecho
  }
}
