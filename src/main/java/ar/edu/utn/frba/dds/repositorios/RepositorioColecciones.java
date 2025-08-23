package ar.edu.utn.frba.dds.repositorios;

import java.util.ArrayList;
import java.util.List;
import ar.edu.utn.frba.dds.dominio.Coleccion;

import java.util.*;

public class RepositorioColecciones {
  private final Map<String, Coleccion> colecciones = new HashMap<>();

  public void guardar(Coleccion coleccion) {
    colecciones.put(coleccion.handle, coleccion);
  }

  public Coleccion buscarPorHandle(String handle) {
    return colecciones.get(handle);
  }

  public List<Coleccion> listarTodas() {
    return new ArrayList<>(colecciones.values());
  }

  public void eliminar(String handle) {
    colecciones.remove(handle);
  }

  public void actualizar(Coleccion coleccion) {
    if (!colecciones.containsKey(coleccion.handle)) {
      throw new IllegalArgumentException("No existe una colección con ese handle para actualizar.");
    }
    colecciones.put(coleccion.handle, coleccion);
  }
}