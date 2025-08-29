package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Hecho {
  protected final String id;
  protected String titulo;
  protected String descripcion;
  protected String categoria;
  protected double latitud;
  protected double longitud;
  protected LocalDate fechaAcontecimiento;
  protected final LocalDate fechaCarga;
  protected final Origen origen;
  protected boolean visible;
  protected boolean consensuado = false;
  protected LocalDate fechaModificacion;


  protected final List<SolicitudEliminacion> solicitudes = new ArrayList<>();

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

  public boolean getVisible(){
    return visible;
  }

  public void setConsensuado(boolean b) {
    this.consensuado = b;
  }

  public boolean estaConsensuado(){
    return this.consensuado;
  }

  public boolean esElMismo(Hecho otroHecho) {
    return
        this.titulo.equals(otroHecho.getTitulo()) &&
            this.descripcion.equals(otroHecho.getDescripcion()) &&
            this.categoria.equals(otroHecho.getCategoria()) &&
           this.latitud == otroHecho.getLatitud() &&
           this.longitud == otroHecho.getLongitud() &&
           this.fechaAcontecimiento.equals(otroHecho.getFechaAcontecimiento()) &&
           this.visible == otroHecho.getVisible();

  }

  public boolean tieneMismoTitulo(Hecho otroHecho) {
    return this.titulo.equals(otroHecho.getTitulo());
  }
}

