package ar.edu.utn.frba.dds.filtros;

import java.time.LocalDate;
import ar.edu.utn.frba.dds.dominio.Hecho;

public class FiltroFecha implements Filtro {
  private final LocalDate fechaBuscada;

  public FiltroFecha(LocalDate fechaBuscada) {
    this.fechaBuscada = fechaBuscada;
  }

  @Override
  public boolean cumple(Hecho hecho) {
    return hecho.getFechaHecho().equals(fechaBuscada);
  }
}
