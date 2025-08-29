package ar.edu.utn.frba.dds.fuentes;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public abstract class Fuente {
  @Id
  @GeneratedValue
  private Long id;

  public abstract ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros);
  public abstract List<Fuente> getFuente();
}

