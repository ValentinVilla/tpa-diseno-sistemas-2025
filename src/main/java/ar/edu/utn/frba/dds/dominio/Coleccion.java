package ar.edu.utn.frba.dds.dominio;

import java.util.List;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.filtros.Filtro;

public class Coleccion {
  private final String titulo;
  private final String descripcion;
  private final Fuente fuente;
  private Filtro criterioPertenencia;

  public Coleccion(ColeccionBuilder builder) {
    this.titulo = builder.getTitulo();
    this.descripcion = builder.getDescripcion();
    this.fuente = builder.getFuente();
    this.criterioPertenencia = builder.getCriterio();
  }

  public List<Hecho> mostrarHechos(List<Hecho> hechos) {
    return hechos.stream().filter(hecho -> criterioPertenencia.cumple(hecho)).toList();
  }

  public List<Hecho> hechosFiltrados(List<Hecho> hechos, Filtro filtro) {
    return mostrarHechos(hechos).stream().filter(filtro::cumple).toList();
  }

  public void setFiltro(Filtro filtro) {
    this.criterioPertenencia = filtro;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Fuente getFuente() {
    return fuente;
  }

  public Filtro getFiltro() {
    return criterioPertenencia;
  }
}
