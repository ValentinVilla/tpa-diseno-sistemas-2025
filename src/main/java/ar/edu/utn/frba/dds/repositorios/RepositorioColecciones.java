package ar.edu.utn.frba.dds.repositorios;

import java.util.ArrayList;
import java.util.List;
import ar.edu.utn.frba.dds.dominio.Coleccion;

import java.util.*;

public class RepositorioColecciones {
  private final Map<Long, Coleccion> colecciones = new HashMap<>();

  public void guardar(Coleccion coleccion) {
    colecciones.put(coleccion.id, coleccion);
  }

  public Coleccion buscarPorID(Long id) {
    return colecciones.get(id);
  }

  public List<Coleccion> listarTodas() {
    return new ArrayList<>(colecciones.values());
  }

  public void eliminar(Long id) {
    colecciones.remove(id);
  }

  public void actualizar(Coleccion coleccion) {
    if (!colecciones.containsKey(coleccion.getId())) {
      throw new IllegalArgumentException("No existe una colección con ese handle para actualizar.");
    }
    colecciones.put(coleccion.getId(), coleccion);
  }
}
