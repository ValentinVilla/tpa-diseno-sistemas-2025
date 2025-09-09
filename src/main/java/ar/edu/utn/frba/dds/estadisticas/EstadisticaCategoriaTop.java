package ar.edu.utn.frba.dds.estadisticas;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "estadistica_categoria_top")
public class EstadisticaCategoriaTop {
  @Id
  private Long id;

  private String categoria;
  private Long cantidad;

  public String getCategoria() {
    return categoria;
  }
}
