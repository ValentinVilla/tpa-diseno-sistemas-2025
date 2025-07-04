package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RepositorioHechos {
  private final Map<Fuente, List<Hecho>> hechosPorFuente = new HashMap<>();

  public List<Hecho> obtenerTodos() {
    return hechosPorFuente.values()
        .stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  public List<Hecho> obtenerhechosDe(Fuente fuente) {
    return hechosPorFuente.getOrDefault(fuente, Collections.emptyList());
  }

  private Fuente buscarFuenteDe(Hecho hecho) {
    for (Map.Entry<Fuente, List<Hecho>> entry : hechosPorFuente.entrySet()) {
      if (entry.getValue().contains(hecho)) {
        return entry.getKey();
      }
    }
    throw new IllegalArgumentException("Hecho no encontrado en ninguna fuente");
  }

  public void guardar(Fuente fuente, Hecho hecho) {
    hechosPorFuente
        .computeIfAbsent(fuente, f -> new ArrayList<>())
        .add(hecho);
  }

  public void eliminar(Hecho hecho) {
    hecho.setVisible(false);
    actualizar(hecho);
  }

  public void actualizar(Hecho hecho) {
    Fuente fuente = buscarFuenteDe(hecho);
    List<Hecho> lista = hechosPorFuente.get(fuente);
    for (int i = 0; i < lista.size(); i++) {
      if (lista.get(i).equals(hecho)) {
        lista.set(i, hecho);
        return;
      }
    }
  }

}
