package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Hecho {
  private final String id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private double latitud;
  private double longitud;
  private LocalDate fechaAcontecimiento;
  private final LocalDate fechaCarga;
  private final Origen origen;
  private boolean visible;
  private LocalDate fechaModificacion;

  private final List<SolicitudEliminacion> solicitudes = new ArrayList<>();

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

  public void agregarSolicitud(SolicitudEliminacion solicitud) {
    this.solicitudes.add(solicitud);
  }

  public List<SolicitudEliminacion> getSolicitudes() {
    return this.solicitudes;
  }

  public void actualizarDesde(Hecho otro) {
    this.setVisible(false);
    HechoContribuyente nuevoHecho = new HechoContribuyente();
    this.titulo = otro.getTitulo();
    this.descripcion = otro.getDescripcion();
    this.categoria = otro.getCategoria();
    this.latitud = otro.getLatitud();
    this.longitud = otro.getLongitud();
    this.fechaAcontecimiento = otro.getFechaAcontecimiento();


    this.fechaModificacion = LocalDate.now();


    this.setVisible(false);
  }

  public boolean cumpleCon(Filtro filtro) {
    return filtro.cumple(this);
  }

  public void setVisible(boolean visibilidad) {
    this.visible = visibilidad;
  }

  public String getId() {
    return id;
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

  public LocalDate getFechaAcontecimiento() {
    return this.fechaAcontecimiento;
  }

}

