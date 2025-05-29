package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dtos.HechoDTO;
import ar.edu.utn.frba.dds.servicios.HechoService;
import ar.edu.utn.frba.dds.dominio.Hecho;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hechos")
public class HechoController {
  private final HechoService hechoService;

  public HechoController (HechoService hechoService) {
    this.hechoService = hechoService;
  }
  
  @GetMapping
  public List<HechoDTO> listarHechos(
      @RequestParam(required = false) String categoria,
      @RequestParam(name = "fecha_reporte_desde", required = false) LocalDate fechaReporteDesde,
      @RequestParam(name = "fecha_reporte_hasta", required = false) LocalDate fechaReporteHasta,
      @RequestParam(name = "fecha_acontecimiento_desde", required = false) LocalDate fechaAcontecimientoDesde,
      @RequestParam(name = "fecha_acontecimiento_hasta", required = false) LocalDate fechaAcontecimientoHasta
  ) {
    List <Hecho> hechosFiltrador = hechoService.filtrarHechos(
        categoria,
        fechaReporteDesde, fechaReporteHasta,
        fechaAcontecimientoDesde, fechaAcontecimientoHasta
    );

    return hechosFiltrador.stream()
        .map(HechoDTO::new)
        .collect(Collectors.toList());
  }
}
