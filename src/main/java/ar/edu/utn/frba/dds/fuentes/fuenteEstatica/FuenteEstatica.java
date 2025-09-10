package ar.edu.utn.frba.dds.fuentes.fuenteEstatica;

import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;

import javax.persistence.Entity;
import javax.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity
public class FuenteEstatica extends Fuente {
  private String pathArchivo;
  private String categoria;
  @Transient
  private ArrayList<String> campos;

  public FuenteEstatica() {}

  public FuenteEstatica(String pathArchivo, String categoria, ArrayList<String> campos) {
    this.pathArchivo = pathArchivo;
    this.categoria = categoria;
    this.campos = campos;
  }

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros) {
    return new LectorCSV().leerDesde(pathArchivo, categoria, campos);
  }

  public List<Fuente> getFuente(){
    return List.of(this);
  }
}
