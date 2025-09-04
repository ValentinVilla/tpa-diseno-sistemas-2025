package ar.edu.utn.frba.dds.fuentes;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) //Justificacion: decidimos usar esta estrategia de herencia ya que en ningun momento necesitamos listar todas las fuentes con un getFuentes() si hicieramos eso el motor de BD deberia realizar muchos unions lo que restaria a nivel performance
public abstract class Fuente {
  @Id
  @GeneratedValue //quizas convenga mas especificar que el generated value sea SEQUENCE
  private Long id;

  public abstract ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros);
  public abstract List<Fuente> getFuente();
}

