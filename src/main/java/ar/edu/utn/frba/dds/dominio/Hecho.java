package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;

import java.time.LocalDate;
import java.util.UUID;

public class Hecho {
  private final String id;
  private final String titulo;
  private final String descripcion;
  private final String categoria;
  private final double latitud;
  private final double longitud;
  private final LocalDate fechaAcontecimiento;
  private final LocalDate fechaCarga;
  private final Origen origen;
  private boolean visible;

  public Hecho(HechoBuilder builder) {
    this.id = UUID.randomUUID().toString();
    this.titulo = builder.getTitulo();
    this.descripcion = builder.getDescripcion();
    this.categoria = builder.getCategoria();
    this.latitud = builder.getLatitud();
    this.longitud = builder.getLongitud();
    this.fechaAcontecimiento = builder.getFechaAcontecimiento();
    this.fechaCarga = builder.getFechaCarga();
    this.origen = builder.getOrigen();
    this.visible = builder.isVisible();
  }

  public String getId() {
    return id;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public String getCategoria() {
    return this.categoria;
  }

  public LocalDate getFechaHecho() {
    return this.fechaAcontecimiento;
  }

  public double getLatitud() {
    return this.latitud;
  }

  public double getLongitud() {
    return this.longitud;
  }

  public String getTitulo() {
    return this.titulo;
  }

  public String getDescripcion() {
    return this.descripcion;
  }

  public LocalDate getFechaCarga() {
    return this.fechaCarga;
  }

  public LocalDate getFechaAcontecimiento(){
    return this.fechaAcontecimiento;
  }
}
