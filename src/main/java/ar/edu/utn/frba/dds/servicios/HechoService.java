package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;

import java.util.List;

public class HechoService {

  private final RepositorioHechos repositorio;

  public HechoService(RepositorioHechos repositorio) {
    this.repositorio = repositorio;
  }

  public void guardarHecho(Hecho hecho) {
    repositorio.guardar(hecho);
  }

  public List<Hecho> obtenerTodos() {
    return repositorio.obtenerTodos();
  }

  public void eliminarHecho(Hecho hecho) {
    repositorio.eliminar(hecho);
  }

  public void actualizarHecho(Hecho hecho) {
    repositorio.actualizar(hecho);
  }
}
