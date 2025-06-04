package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import java.util.ArrayList;
import java.util.List;
import ar.edu.utn.frba.dds.dominio.Coleccion;

public class RepositorioColecciones {
  private List<Coleccion> colecciones = new ArrayList<>();

  public Coleccion obtenerColeccionPorId(String idGuardarropa) throws Exception {
    List<GuardaRopa> guardarropas = this.guardaRopa.stream().filter(guardarropa -> guardarropa.id() == idGuardarropa).collect(Collectors.toList());
    if(guardarropas.isEmpty())
      throw new Exception();
    return guardarropas.get(0);
  }
}
