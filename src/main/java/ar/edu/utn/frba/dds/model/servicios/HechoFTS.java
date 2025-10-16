package ar.edu.utn.frba.dds.model.servicios;

import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;

import java.util.List;

public class HechoFTS {
  private final DAOHechos repositorioHechos;

  public HechoFTS(DAOHechos repositorioHechos) {
    this.repositorioHechos = repositorioHechos;
  }

  public List<Hecho> buscar(String textoBusqueda) {
    /*
    esto era lo que hacia antes, pero ahora con el plainto_tsquery no es necesario

    String formattedQuery = Arrays.stream(textoBusqueda.split("\\s+"))
        .collect(Collectors.joining(" & "));

     */
    List<Hecho> resultados = repositorioHechos.buscarPorTextoEnDB(textoBusqueda);

    if (resultados.isEmpty()) {
      //Si no hubo coincidencias, probamos con la solucion que usa similitud, por si las palabras estan mal escritas
      resultados = repositorioHechos.buscarPorSimilitud(textoBusqueda);
    }

    return resultados;
  }
}