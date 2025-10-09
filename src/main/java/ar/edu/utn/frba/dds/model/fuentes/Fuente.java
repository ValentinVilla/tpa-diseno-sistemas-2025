package ar.edu.utn.frba.dds.model.fuentes;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_fuente")
public abstract class Fuente {
  @Id
  @GeneratedValue
  private Long id;

  public abstract ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros);
  public abstract List<Fuente> getFuente();
}

