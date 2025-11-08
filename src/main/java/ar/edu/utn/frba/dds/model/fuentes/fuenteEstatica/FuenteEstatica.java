package ar.edu.utn.frba.dds.model.fuentes.fuenteEstatica;

import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("FUENTE_ESTATICA")
public class FuenteEstatica extends Fuente {
  private String pathArchivo;
  private String categoria;
  private ArrayList<String> campos;

  public FuenteEstatica() {
  }

  public FuenteEstatica(String pathArchivo, String categoria, ArrayList<String> campos) {
    this.pathArchivo = pathArchivo;
    this.categoria = categoria;
    this.campos = campos;
  }

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros) {
    DAOHechos repo = DAOHechos.getInstancia();
    LectorCSV lector = new LectorCSV();
    ArrayList<Hecho> hechosCSV = lector.leerDesde(this.pathArchivo, this.categoria, this.campos);
    ArrayList<Hecho> hechosNoEliminados = repo.losQueNoFueronEliminados(hechosCSV);
    return filtrarHechos(hechosNoEliminados, parametros);
  }

  public List<Fuente> getFuente() {
    return List.of(this);
  }
}
