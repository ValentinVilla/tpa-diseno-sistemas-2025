package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_hecho")
public class Hecho {
  @Id
  @GeneratedValue
  private long id;

  protected String titulo;
  protected String descripcion;
  protected String categoria;
  protected Double latitud;
  protected Double longitud;
  protected LocalDateTime fechaAcontecimiento;
  protected LocalDateTime fechaCarga;
  @Enumerated(EnumType.STRING)
  protected Origen origen;
  protected boolean visible;
  protected boolean consensuado = false;
  protected LocalDate fechaModificacion;
  @OneToMany(mappedBy = "hecho", cascade = CascadeType.ALL, orphanRemoval = true)
  protected final List<SolicitudEliminacion> solicitudes = new ArrayList<>();
  private String provincia;

  public Hecho() {}

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

  public long getId() {
    return id;
  }

  public String getCategoria() {
    return this.categoria;
  }

  public LocalDateTime getFechaHecho() {
    return this.fechaAcontecimiento;
  }

  public Double getLatitud() {
    return this.latitud;
  }

  public Double getLongitud() {
    return this.longitud;
  }

  public String getTitulo() {
    return this.titulo;
  }

  public String getDescripcion() {
    return this.descripcion;
  }

  public LocalDateTime getFechaCarga() {
    return this.fechaCarga;
  }

  public LocalDateTime getFechaAcontecimiento() {
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
            Objects.equals(this.latitud, otroHecho.getLatitud()) &&
            Objects.equals(this.longitud, otroHecho.getLongitud()) &&
           this.fechaAcontecimiento.equals(otroHecho.getFechaAcontecimiento()) &&
           this.visible == otroHecho.getVisible();

  }

  public boolean tieneMismoTitulo(Hecho otroHecho) {
    return this.titulo.equals(otroHecho.getTitulo());
  }

  public void setTitulo(String nuevoTitulo) {
    this.titulo = nuevoTitulo;
  }

  public void setProvincia(String provincia) {
    this.provincia = provincia;
  }

  public String getProvincia() {
    return this.provincia;
  }
}

