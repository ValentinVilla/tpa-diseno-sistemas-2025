package ar.edu.utn.frba.dds.solicitudes;

import ar.edu.utn.frba.dds.DetectorSpam.DetectorDeSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_solicitud")
public abstract class Solicitud {
  @Id
  @GeneratedValue
  protected Long id;
  protected String textoFundamentacion;
  @Enumerated(EnumType.STRING)
  protected EstadoSolicitud estado;
  @Transient
  public DetectorDeSpam detector;

  protected String valoresHecho;
  protected Solicitud(){}

  public Solicitud(Hecho hecho, String sugerenciaModificacion, DetectorDeSpam detector) {
    this.valoresHecho =  hecho.getTitulo() + " | " + hecho.getDescripcion() + " | " + hecho.getCategoria();
    this.textoFundamentacion = sugerenciaModificacion;
    this.detector = detector;
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

  public Long getId() {
    return id;
  }
}
