package ar.edu.utn.frba.dds.servicios;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;

import java.util.List;

public class ColeccionService {

  private final RepositorioColecciones repositorio;
  private final RepositorioHechos repositorioHechos;
  public ColeccionService(RepositorioColecciones repositorio, RepositorioHechos repositorioHechos) {
    this.repositorio = repositorio;
    this.repositorioHechos = repositorioHechos;
  }

  public void crearColeccion(String titulo, String descripcion, Fuente fuente, Filtro criterio) {
    Coleccion nueva = new ColeccionBuilder()
        .titulo(titulo)
        .descripcion(descripcion)
        .fuente(fuente)
        .criterio(criterio)
        .build();
    repositorio.guardar(nueva);
  }

  public List<Coleccion> obtenerTodas() {
    return repositorio.obtenerTodas();
  }

  public void aplicarFiltro(Coleccion coleccion, Filtro filtro) {
    coleccion.setFiltro(filtro);
    repositorio.actualizar(coleccion);
  }

  public List<Hecho> obtenerHechosDe(Coleccion coleccion) {
    return coleccion.mostrarHechos(repositorioHechos.obtenerTodos());
  }

  public List<Hecho> obtenerHechosFiltradosDe(Coleccion coleccion, Filtro filtro) {
    return coleccion.hechosFiltrados(repositorioHechos.obtenerTodos(), filtro);
  }

}