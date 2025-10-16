package ar.edu.utn.frba.dds.model.filtros;

import ar.edu.utn.frba.dds.model.dominio.Hecho;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_filtro",  discriminatorType = DiscriminatorType.STRING)
public abstract class Filtro {
  @Id
  @GeneratedValue
  private Long id;

  public abstract boolean cumple(Hecho hecho);
}

