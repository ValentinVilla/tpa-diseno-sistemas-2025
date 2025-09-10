package ar.edu.utn.frba.dds.estadisticas;

import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "estadistica_provincia_por_categoria_top")
@Immutable
public class EstadisticaProvinciaPorCategoriaTop {
  @Id
  private Long id;

  private String provincia;
  private String categoria;
  private Long cantidad;

  public String getProvincia() {
    return provincia;
  }
}