package ar.edu.utn.frba.dds.model.dtos;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ParametrosConsulta {
  private String texto;
  private String categoria;
  private LocalDate fechaReporteDesde;
  private LocalDate fechaReporteHasta;
  private LocalDate fechaAcontecimientoDesde;
  private LocalDate fechaAcontecimientoHasta;
  private String ubicacion;
  private Long coleccionId;

  public ParametrosConsulta() {
    this.texto = null;
    this.categoria = null;
    this.fechaReporteDesde = null;
    this.fechaReporteHasta = null;
    this.fechaAcontecimientoDesde = null;
    this.fechaAcontecimientoHasta = null;
    this.ubicacion = null;
    this.coleccionId = null;
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

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public void setFechaReporteDesde(LocalDate fechaReporteDesde) {
    this.fechaReporteDesde = fechaReporteDesde;
  }

  public void setFechaReporteHasta(LocalDate fechaReporteHasta) {
    this.fechaReporteHasta = fechaReporteHasta;
  }

  public void setFechaAcontecimientoDesde(LocalDate fechaAcontecimientoDesde) {
    this.fechaAcontecimientoDesde = fechaAcontecimientoDesde;
  }

  public void setFechaAcontecimientoHasta(LocalDate fechaAcontecimientoHasta) {
    this.fechaAcontecimientoHasta = fechaAcontecimientoHasta;
  }

  public void setUbicacion(String ubicacion) {
    this.ubicacion = ubicacion;
  }

  public void setColeccionId(Long coleccionId) {
    this.coleccionId = coleccionId;
  }

  public String getCategoria() {
    return this.categoria;
  }

  public LocalDate getFechaReporteDesde() {
    return this.fechaReporteDesde;
  }

  public LocalDate getFechaReporteHasta() {
    return this.fechaReporteHasta;
  }

  public LocalDate getFechaAcontecimientoDesde() {
    return this.fechaAcontecimientoDesde;
  }

  public LocalDate getFechaAcontecimientoHasta() {
    return this.fechaAcontecimientoHasta;
  }

  public String getUbicacion() {
    return this.ubicacion;
  }

  public void setTexto(String busqueda) {
    this.texto = busqueda;
  }

  public String getTexto() {
    return this.texto;
  }
}
