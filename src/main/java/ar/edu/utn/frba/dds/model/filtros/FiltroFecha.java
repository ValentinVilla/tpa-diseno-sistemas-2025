package ar.edu.utn.frba.dds.model.filtros;

import java.time.LocalDate;
import ar.edu.utn.frba.dds.model.dominio.Hecho;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FECHA")
public class FiltroFecha extends Filtro {
  private LocalDate fechaBuscada;

  public FiltroFecha(LocalDate fechaBuscada) {
    this.fechaBuscada = fechaBuscada;
  }

  public FiltroFecha() {}

  @Override
  public boolean cumple(Hecho hecho) {
    return hecho.getFechaHecho().equals(fechaBuscada);
  }
}
