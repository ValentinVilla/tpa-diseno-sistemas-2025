package ar.edu.utn.frba.dds.dtos;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ParametrosConsulta {
  private final String categoria;
  private final LocalDate fechaReporteDesde;
  private final LocalDate fechaReporteHasta;
  private final LocalDate fechaAcontecimientoDesde;
  private final LocalDate fechaAcontecimientoHasta;
  private final String ubicacion;
  private final Long coleccionId;

  public ParametrosConsulta() {
    this.categoria = null;
    this.fechaReporteDesde = null;
    this.fechaReporteHasta = null;
    this.fechaAcontecimientoDesde = null;
    this.fechaAcontecimientoHasta = null;
    this.ubicacion = null;
    this.coleccionId = null;
  }

  public ParametrosConsulta(String categoria,
                            LocalDate fechaReporteDesde,
                            LocalDate fechaReporteHasta,
                            LocalDate fechaAcontecimientoDesde,
                            LocalDate fechaAcontecimientoHasta,
                            String ubicacion,
                            Long coleccionId) {
    this.categoria = categoria;
    this.fechaReporteDesde = fechaReporteDesde;
    this.fechaReporteHasta = fechaReporteHasta;
    this.fechaAcontecimientoDesde = fechaAcontecimientoDesde;
    this.fechaAcontecimientoHasta = fechaAcontecimientoHasta;
    this.ubicacion = ubicacion;
    this.coleccionId = coleccionId;
  }

  public Map<String, String> comoMapa() {
    Map<String, String> map = new HashMap<>();
    if (categoria != null) map.put("categoria", categoria);
    if (fechaReporteDesde != null) map.put("fecha_reporte_desde", fechaReporteDesde.toString());
    if (fechaReporteHasta != null) map.put("fecha_reporte_hasta", fechaReporteHasta.toString());
    if (fechaAcontecimientoDesde != null) map.put("fecha_acontecimiento_desde", fechaAcontecimientoDesde.toString());
    if (fechaAcontecimientoHasta != null) map.put("fecha_acontecimiento_hasta", fechaAcontecimientoHasta.toString());
    if (ubicacion != null) map.put("ubicacion", ubicacion);
    return map;
  }

  public Long getColeccionId() {
    return this.coleccionId;
  }
}
