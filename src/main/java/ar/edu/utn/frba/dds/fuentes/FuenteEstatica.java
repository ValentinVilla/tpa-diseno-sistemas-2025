package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.util.ArrayList;

public class FuenteEstatica implements Fuente {
  private final String pathArchivo;
  private final String categoria;

  public FuenteEstatica(String pathArchivo, String categoria) {
    this.pathArchivo = pathArchivo;
    this.categoria = categoria;
  }

  @Override
  public ArrayList<Hecho> cargarHechos() {
    return new LectorCSV().leerDesde(pathArchivo, categoria);
  }
}
