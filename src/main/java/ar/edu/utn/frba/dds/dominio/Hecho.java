package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
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
  private boolean editable;
  private int idContribuyenteCreador;


  private final List<SolicitudEliminacion> solicitudes = new ArrayList<>();

  public Hecho(HechoBuilder builder) {
    // Cuando tengamos base de datos el ID se le asignara en la carga
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

  public void agregarSolicitud(SolicitudEliminacion solicitud) {
    this.solicitudes.add(solicitud);
  }

  public List<SolicitudEliminacion> getSolicitudes() {
    return this.solicitudes;
  }

  public void setEditable(boolean editable) {
    this.editable = editable;
  }

  public boolean isEditable() {
    return this.editable;
  }

  public void setIdContribuyenteCreador(int idContribuyenteCreador) {
    this.idContribuyenteCreador = idContribuyenteCreador;
  }

  public int getIdContribuyenteCreador() {
    return this.idContribuyenteCreador;
  }

  public void actualizarDesde(Hecho otro) {
    // Se actualizan solo los atributos modificables
    this.titulo = otro.getTitulo();
    this.descripcion = otro.getDescripcion();
    this.categoria = otro.getCategoria();
    this.latitud = otro.getLatitud();
    this.longitud = otro.getLongitud();
    this.fechaAcontecimiento = otro.getFechaAcontecimiento();
    this.fechaModificacion = LocalDate.now();
  }

  public boolean esEditableActualmente() {
    if (!editable) return false;

    long diasDesdeCarga = ChronoUnit.DAYS.between(fechaCarga, LocalDate.now());
    if (diasDesdeCarga > 7) {
      this.editable = false;
      return false;
    }

    return true;
  }

}

