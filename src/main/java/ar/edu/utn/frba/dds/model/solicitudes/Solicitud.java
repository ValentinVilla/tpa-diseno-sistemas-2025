package ar.edu.utn.frba.dds.model.solicitudes;

import ar.edu.utn.frba.dds.model.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_solicitud")
public abstract class Solicitud {
  @Id
  @GeneratedValue
  protected Long id;
  protected LocalDate fecha;
  protected String textoFundamentacion;
  @Enumerated(EnumType.STRING)
  protected EstadoSolicitud estado;
  @Transient
  public DetectorDeSpam detector;

  protected String valoresHecho;
  protected Solicitud(){}

  public Solicitud(Hecho hecho, String sugerenciaModificacion, DetectorDeSpam detector, Contribuyente contribuyente) {
    this.valoresHecho =  hecho.getTitulo() + " ; " + hecho.getDescripcion() + " ; " + hecho.getCategoria() + " ; " + contribuyente.getNombreCompleto();
    this.textoFundamentacion = sugerenciaModificacion;
    this.detector = detector;
    fecha = LocalDate.now();
    if(detector != null && detector.esSpam(sugerenciaModificacion)){
      this.estado = EstadoSolicitud.RECHAZADA;
    } else {
      this.estado = EstadoSolicitud.PENDIENTE;
    }
  }

  public void aceptar(){
     this.estado = EstadoSolicitud.ACEPTADA;
     aplicarAceptacion();
  }
  public void rechazar(){
      this.estado = EstadoSolicitud.RECHAZADA;
      RepositorioSolicitudes.getInstancia().actualizar(this);
  }

  public abstract void aplicarAceptacion();

  public String getTextoFundamentacion() {
    return textoFundamentacion;
  }

  public EstadoSolicitud getEstado() {
    return estado;
  }

  public LocalDate getFecha(){
    return fecha;
  }

  public String getValoresHecho() {
    return valoresHecho;
  }

  public Long getId() {
    return id;
  }

  public boolean estaPendiente() {
    return this.estado == EstadoSolicitud.PENDIENTE;
  }

}