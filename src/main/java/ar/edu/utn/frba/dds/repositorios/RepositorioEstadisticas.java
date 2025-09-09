package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.*;

public class RepositorioEstadisticas {
  public String provinciaConMasHechos(Coleccion coleccion) {
    List<Hecho> hechos = coleccion.mostrarHechos();

    Map<String, Long> conteoPorProvincia = hechos.stream()
        .filter(h -> h.getProvincia() != null) // evitamos nulls
        .collect(Collectors.groupingBy(
            Hecho::getProvincia,
            Collectors.counting()
        ));

    return conteoPorProvincia.entrySet().stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse("Sin datos");
  }
}
