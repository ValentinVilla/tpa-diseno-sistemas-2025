package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.dominio.Hecho;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public abstract class Filtro {
  @Id
  @GeneratedValue
  private Long id;

  public abstract boolean cumple(Hecho hecho);
}
