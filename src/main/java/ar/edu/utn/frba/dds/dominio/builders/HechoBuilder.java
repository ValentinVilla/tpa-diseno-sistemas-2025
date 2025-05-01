package ar.edu.utn.frba.dds.dominio.builders;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;

import java.time.LocalDate;

public class HechoBuilder {
  private String titulo;
  private String descripcion;
  private String categoria;
  private double latitud;
  private double longitud;
  private LocalDate fechaAcontecimiento;
  private LocalDate fechaCarga;
  private Origen origen;
  private boolean visible = true;

  public HechoBuilder titulo(String titulo) {
    this.titulo = titulo;
    return this;
  }

  public HechoBuilder descripcion(String descripcion) {
    this.descripcion = descripcion;
    return this;
  }

  public HechoBuilder categoria(String categoria) {
    this.categoria = categoria;
    return this;
  }

  public HechoBuilder latitud(double latitud) {
    this.latitud = latitud;
    return this;
  }

  public HechoBuilder longitud(double longitud) {
    this.longitud = longitud;
    return this;
  }

  public HechoBuilder fechaAcontecimiento(LocalDate fechaAcontecimiento) {
    this.fechaAcontecimiento = fechaAcontecimiento;
    return this;
  }

  public HechoBuilder fechaCarga(LocalDate fechaCarga) {
    this.fechaCarga = fechaCarga;
    return this;
  }

  public HechoBuilder origen(Origen origen) {
    this.origen = origen;
    return this;
  }

  public HechoBuilder visible(boolean visible) {
    this.visible = visible;
    return this;
  }

  public Hecho build() {
    return new Hecho(this);
  }

  // Getters para que Hecho acceda a los atributos
  public String getTitulo() { return titulo; }
  public String getDescripcion() { return descripcion; }
  public String getCategoria() { return categoria; }
  public double getLatitud() { return latitud; }
  public double getLongitud() { return longitud; }
  public LocalDate getFechaAcontecimiento() { return fechaAcontecimiento; }
  public LocalDate getFechaCarga() { return fechaCarga; }
  public Origen getOrigen() { return origen; }
  public boolean isVisible() { return visible; }
}
