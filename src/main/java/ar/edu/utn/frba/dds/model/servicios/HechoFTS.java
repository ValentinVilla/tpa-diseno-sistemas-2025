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
    List<Hecho> resultados = repositorioHechos.buscarPorTextoEnDB(textoBusqueda);

    if (resultados.isEmpty()) {
      resultados = repositorioHechos.buscarPorSimilitud(textoBusqueda);
    }

    return resultados;
  }

  public List<Hecho> buscarPorFuente(String textoBusqueda, Long fuenteId) {
    List<Hecho> resultados = repositorioHechos.buscarPorTextoPorFuente(textoBusqueda, fuenteId);

    if (resultados.isEmpty()) {
      resultados = repositorioHechos.buscarPorSimilitudPorFuente(textoBusqueda,  fuenteId);
    }

    return resultados;
  }
}