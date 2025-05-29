package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.util.ArrayList;
import java.util.List;

public class RepositorioHechos {
  private final List<Hecho> hechos = new ArrayList<>();

  public List<Hecho> obtenerTodos() {
    return new ArrayList<>(hechos);
  }

  public void guardar(Hecho hecho) {
    hechos.add(hecho);
  }

  public void eliminar(Hecho hecho) {
    hechos.remove(hecho);
  }

  public void actualizar(Hecho hecho) {
    //TODO
  }
}
