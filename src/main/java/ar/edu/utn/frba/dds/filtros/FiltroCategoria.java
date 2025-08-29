package ar.edu.utn.frba.dds.filtros;
import ar.edu.utn.frba.dds.dominio.Hecho;

import javax.persistence.Entity;

@Entity
public class FiltroCategoria extends Filtro {
  private String categoria;

  public FiltroCategoria(String categoria) {
    this.categoria = categoria;
  }

  public FiltroCategoria() {}

  @Override
  public boolean cumple(Hecho hecho) {
    return hecho.getCategoria().equalsIgnoreCase(categoria);
  }
}
