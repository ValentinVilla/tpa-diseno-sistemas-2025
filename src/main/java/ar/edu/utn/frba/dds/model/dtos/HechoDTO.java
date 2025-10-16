package ar.edu.utn.frba.dds.model.dtos;

import ar.edu.utn.frba.dds.model.dominio.Hecho;

import java.time.LocalDateTime;

public class HechoDTO {
  public String titulo;
  public String descripcion;
  public String categoria;
  private final double latitud;
  private final double longitud;
  public LocalDateTime fechaReporte;
  public LocalDateTime fechaAcontecimiento;

  public HechoDTO(Hecho hecho) {
    this.titulo = hecho.getTitulo();
    this.descripcion = hecho.getDescripcion();
    this.categoria = hecho.getCategoria();
    this.latitud = hecho.getLatitud();
    this.longitud = hecho.getLongitud();
    this.fechaReporte = hecho.getFechaCarga();
    this.fechaAcontecimiento = hecho.getFechaAcontecimiento();
  }
}
