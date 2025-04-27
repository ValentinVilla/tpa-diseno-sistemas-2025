package ar.edu.utn.frba.dds;

import java.time.LocalDate;

public class Hecho {
  private String titulo;
  private String descripcion;
  private String categoria;
  private double latitud;
  private double longitud;
  private LocalDate fechaAcontecimiento;
  private LocalDate fechaCarga;
  private Origen origen;
  private boolean visible;


  public void setVisible(boolean visible){
    this.visible = visible;
  }

  private Hecho(Builder builder) {
    this.titulo = builder.titulo;
    this.descripcion = builder.descripcion;
    this.categoria = builder.categoria;
    this.latitud = builder.latitud;
    this.longitud = builder.longitud;
    this.fechaAcontecimiento = builder.fechaAcontecimiento;
    this.fechaCarga = builder.fechaCarga;
    this.origen = builder.origen;
    this.visible = builder.visible;
  } // contructor builder del hecho

  public static class Builder {
    private String titulo;
    private String descripcion;
    private String categoria;
    private double latitud;
    private double longitud;
    private LocalDate fechaAcontecimiento;
    private LocalDate fechaCarga;
    private Origen origen;
    private boolean visible = true; //un hecho cuando se cree va a arrancar con visible = true.

    public Builder titulo(String titulo) {
      this.titulo = titulo;
      return this;
    }

    public Builder descripcion(String descripcion) {
      this.descripcion = descripcion;
      return this;
    }

    public Builder categoria(String categoria) {
      this.categoria = categoria;
      return this;
    }

    public Builder latitud(double latitud) {
      this.latitud = latitud;
      return this;
    }

    public Builder longitud(double longitud) {
      this.longitud = longitud;
      return this;
    }

    public Builder fechaAcontecimiento(LocalDate fechaAcontecimiento) {
      this.fechaAcontecimiento = fechaAcontecimiento;
      return this;
    }

    public Builder fechaCarga(LocalDate fechaCarga) {
      this.fechaCarga = fechaCarga;
      return this;
    }

    public Builder origen(Origen origen) {
      this.origen = origen;
      return this;
    }

    public Builder visible(boolean visible) {
      this.visible = visible;
      return this;
    }

    public Hecho build() {
      return new Hecho(this);
    }
  }
}
