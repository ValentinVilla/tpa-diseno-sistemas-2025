package ar.edu.utn.frba.dds.model.dominio;


import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.model.servicios.GeorefAPI;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class HechoDinamico extends Hecho {
  @ManyToOne(optional = true)
  @JoinColumn(name = "contribuyente_id", nullable = true) //permito los null para poder guardar hechos anonimos
  private Contribuyente contribuyente;

  private boolean visible = false;

  private LocalDate fechaModificacion;

  protected HechoDinamico() {
  }

  public HechoDinamico(HechoBuilder builder, Contribuyente contribuyente) {
    super(builder);
    // pues ahora el contribuyente puede ser null (no logueado)
    this.contribuyente = contribuyente;
  }

  public boolean estaDentroDePlazo() {
    long diasDesdeCarga = ChronoUnit.DAYS.between(getFechaCarga(), LocalDateTime.now());
    return diasDesdeCarga <= 7;
  }

  public Contribuyente getContribuyente() {
    return contribuyente;
  }

  public boolean esAnonimo() {
    return this.contribuyente == null;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public boolean getVisible() {
    return visible;
  }

  public void revisarubicacion() {
    if (this.getLatitud() != null && this.getLongitud() != null) {
      try {
        String provincia = GeorefAPI.getProvincia(
            this.getLatitud(), this.getLongitud()
        );
        this.setProvincia(provincia);
      } catch (Exception e) {
        System.err.println("Error al obtener provincia: " + e.getMessage());
        this.setProvincia("Desconocida");
      }
    }
  }
}