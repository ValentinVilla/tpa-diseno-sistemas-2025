package ar.edu.utn.frba.dds.dtos;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.time.LocalDate;

public class HechoDTO {
  public String titulo;
  public String descripcion;
  public String categoria;
  private final double latitud;
  private final double longitud;
  public LocalDate fechaReporte;
  public LocalDate fechaAcontecimiento;

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
