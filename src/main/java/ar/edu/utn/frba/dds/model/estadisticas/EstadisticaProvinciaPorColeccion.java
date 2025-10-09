package ar.edu.utn.frba.dds.model.estadisticas;

import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "estadistica_provincia_por_coleccion")
@Immutable
public class EstadisticaProvinciaPorColeccion {
    @Id
  private Long id;

  @ManyToOne
  private Coleccion coleccion;
  private String provincia;
  private Long cantidad;

  public String getProvincia() {
    return provincia;
  }
}
