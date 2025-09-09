package ar.edu.utn.frba.dds.estadisticas;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "estadistica_hora_top_por_categoria")
public class EstadisticaHoraPorCategoriaTop {
  @Id
  private Long id;

  private String categoria;
  private Integer hora;
  private Long cantidad;

  public int getHora() {
    return hora;
  }
}
