package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.HechoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hechos")
public class HechoController {
  private final ArrayList<Hecho> hechos;
  /*
  En teoria el repositorio solo usaba los hechos dinamicos y es para metaMapa asi que mientras tanto dejo
  como parametro una lista de hechos y vuelo el repositorio
   */
  public HechoController(ArrayList<Hecho> hechos) {
    this.hechos = hechos;
  }

  @GetMapping
  public List<HechoDTO> listarHechos(
      @RequestParam(required = false) String categoria,
      @RequestParam(name = "fecha_reporte_desde", required = false) LocalDate fechaReporteDesde,
      @RequestParam(name = "fecha_reporte_hasta", required = false) LocalDate fechaReporteHasta,
      @RequestParam(name = "fecha_acontecimiento_desde", required = false) LocalDate fechaAcontecimientoDesde,
      @RequestParam(name = "fecha_acontecimiento_hasta", required = false) LocalDate fechaAcontecimientoHasta
  ) {
    List<Hecho> hechosFiltrador = filtrarHechos(
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
    return hechos.stream()
        .filter(hecho -> (categoria == null || hecho.getCategoria().equals(categoria)))
        .filter(hecho -> (fechaReporteDesde == null || hecho.getFechaCarga().isAfter(fechaReporteDesde)))
        .filter(hecho -> (fechaReporteHasta == null || hecho.getFechaCarga().isBefore(fechaReporteHasta)))
        .filter(hecho -> (fechaAcontecimientoDesde == null || hecho.getFechaAcontecimiento().isAfter(fechaAcontecimientoDesde)))
        .filter(hecho -> (fechaAcontecimientoHasta == null || hecho.getFechaAcontecimiento().isBefore(fechaAcontecimientoHasta)))
        .collect(Collectors.toList());
  }
}
