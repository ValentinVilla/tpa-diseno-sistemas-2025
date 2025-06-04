package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;

import java.util.ArrayList;

public class FuenteEstatica implements Fuente {
  private final String pathArchivo;
  private final String categoria;
  private final ArrayList<String> campos;

  public FuenteEstatica(String pathArchivo, String categoria, ArrayList<String> campos) {
    this.pathArchivo = pathArchivo;
    this.categoria = categoria;
    this.campos = campos;
  }

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros) {
    return cargarHechos();
  }

  public ArrayList<Hecho> cargarHechos() {
    return new LectorCSV().leerDesde(pathArchivo, categoria, campos);
  }
}
