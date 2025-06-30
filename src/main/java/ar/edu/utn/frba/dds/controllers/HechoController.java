package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dtos.HechoDTO;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import ar.edu.utn.frba.dds.dominio.Hecho;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hechos")
public class HechoController {
  private final RepositorioHechos repositorio;

  public HechoController (RepositorioHechos repositorio) {
    this.repositorio = repositorio;
  }
  
  @GetMapping
  public List<HechoDTO> listarHechos(
      @RequestParam(required = false) String categoria,
      @RequestParam(name = "fecha_reporte_desde", required = false) LocalDate fechaReporteDesde,
      @RequestParam(name = "fecha_reporte_hasta", required = false) LocalDate fechaReporteHasta,
      @RequestParam(name = "fecha_acontecimiento_desde", required = false) LocalDate fechaAcontecimientoDesde,
      @RequestParam(name = "fecha_acontecimiento_hasta", required = false) LocalDate fechaAcontecimientoHasta
  ) {
    List <Hecho> hechosFiltrador = filtrarHechos(
        categoria,
        fechaReporteDesde, fechaReporteHasta,
        fechaAcontecimientoDesde, fechaAcontecimientoHasta
    );

    return hechosFiltrador.stream()
        .map(HechoDTO::new)
        .collect(Collectors.toList());
  }

  private List<Hecho> filtrarHechos(
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
}
