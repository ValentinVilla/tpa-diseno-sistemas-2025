package ar.edu.utn.frba.dds.filtros;
import ar.edu.utn.frba.dds.dominio.Hecho;

public class FiltroCategoria implements Filtro {
  private String categoria;

  public FiltroCategoria(String categoria) {
    this.categoria = categoria;
  }

  @Override
  public boolean cumple(Hecho hecho) {
    return hecho.getCategoria().equalsIgnoreCase(categoria);
  }
}
