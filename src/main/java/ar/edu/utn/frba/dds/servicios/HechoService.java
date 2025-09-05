package ar.edu.utn.frba.dds.servicios;

// HechoService.java
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HechoService {
  private final RepositorioHechos repositorioHechos;

  public HechoService(RepositorioHechos repositorioHechos) {
    this.repositorioHechos = repositorioHechos;
  }

  public List<Hecho> buscar(String textoBusqueda) {
    String formattedQuery = Arrays.stream(textoBusqueda.split("\\s+"))
        .collect(Collectors.joining(" & "));

    return repositorioHechos.buscarPorTextoEnDB(formattedQuery);
  }
}