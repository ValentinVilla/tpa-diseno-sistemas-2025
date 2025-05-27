package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Hecho {
  //TODO: agregar ID
  private final String titulo;
  private final String descripcion;
  private final String categoria;
  private final double latitud;
  private final double longitud;
  private final LocalDate fechaAcontecimiento;
  private final LocalDate fechaCarga;
  private final Origen origen;
  private boolean visible;

  private List<SolicitudEliminacion> solicitudes = new ArrayList<>();

  public Hecho(HechoBuilder builder) {
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

  public void agregarSolicitud(SolicitudEliminacion solicitud) {
    this.solicitudes.add(solicitud);
  }

  public List<SolicitudEliminacion> getSolicitudes() {
    return this.solicitudes;
  }
}