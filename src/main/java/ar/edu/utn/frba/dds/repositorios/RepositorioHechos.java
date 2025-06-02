package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.FuenteDinamica;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RepositorioHechos {
  private Map<FuenteDinamica, List<Hecho>> hechosPorFuente = new HashMap<>();

  public List<Hecho> obtenerTodos() {
    return hechosPorFuente.values()
        .stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  public List<Hecho> obtenerhechosDe(FuenteDinamica fuente) {
    return hechosPorFuente.getOrDefault(fuente, Collections.emptyList());
  }

  public void guardar(FuenteDinamica fuente, Hecho hecho) {
    hechosPorFuente
        .computeIfAbsent(fuente, f -> new ArrayList<>())
        .add(hecho);
  }

  public void eliminar(Hecho hecho) {
    for (List<Hecho> lista : hechosPorFuente.values()) {
      if (lista.remove(hecho)) {
        break;
      }
    }
  }

  //opcional para eliminar de una fuente en particular
  public void eliminarDeFuente(FuenteDinamica fuente, Hecho hecho) {
    List<Hecho> lista = hechosPorFuente.get(fuente);
    if (lista != null) {
      lista.remove(hecho);
    }
  }

  public void actualizar(Hecho hecho) {
    //TODO
  }
}
