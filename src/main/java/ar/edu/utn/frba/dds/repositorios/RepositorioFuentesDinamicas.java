package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuentes.FuenteDinamica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RepositorioFuentesDinamicas {
  // Este repositorio se encarga de almacenar las fuentes dinámicas
  // y proveer métodos para interactuar con ellas, como agregar, eliminar o consultar fuentes.
  private final Map<Integer, FuenteDinamica> fuentes = new HashMap<>();

  // Aquí podrías implementar métodos como:
  // - agregarFuente(Fuente fuente)
  // - eliminarFuente(Fuente fuente)
  // - obtenerFuentes()
  public Fuente buscarFuentePorId(int id){
    return fuentes.get(id); //me retorna la fuente dinámica por su ID que le mapee.
  }

  // Además, podrías considerar usar una lista o un mapa para almacenar las fuentes.

  // Ejemplo de implementación básica:

  // private List<Fuente> fuentes = new ArrayList<>();

  // public void agregarFuente(Fuente fuente) {
  //   fuentes.add(fuente);
  // }

  // public void eliminarFuente(Fuente fuente) {
  //   fuentes.remove(fuente);
  // }

  public List<FuenteDinamica> obtenerFuentes() {
    return new ArrayList<>(fuentes.values());
  }
}
