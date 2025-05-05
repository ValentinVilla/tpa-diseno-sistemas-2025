package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.util.ArrayList;
import java.util.List;
import ar.edu.utn.frba.dds.dominio.Coleccion;

public class RepositorioColecciones {
  private List<Coleccion> colecciones = new ArrayList<>();

  public List<Coleccion> obtenerTodas() {
    return new ArrayList<>(colecciones);
  }

  public void guardar(Coleccion coleccion) {
    colecciones.add(coleccion);
  }

  public void actualizar(Coleccion coleccion) {
    //TODO
  }
}
