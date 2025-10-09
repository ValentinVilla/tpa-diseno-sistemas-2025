package ar.edu.utn.frba.dds.model.estadisticas;

import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "estadistica_solicitudes_spam")
@Immutable
public class EstadisticaSolicitudesSpam {
  @Id
  private Long id;

  private int cantidad;
  private String porcentaje;

  public int getCantidad() {
    return cantidad;
  }

  public String getPorcentaje() {
    return porcentaje;
  }
}
