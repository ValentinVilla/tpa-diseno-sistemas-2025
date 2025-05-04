package ar.edu.utn.frba.dds.dominio;

import java.util.List;

import ar.edu.utn.frba.dds.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.filtros.*;

public class Coleccion {
  private final String titulo;
  private final String descripcion;
  private final Fuente fuente; //
  private Filtro criterioPertenencia;

  public Coleccion(ColeccionBuilder builder) {
    this.titulo = builder.getTitulo();
    this.descripcion = builder.getDescripcion();
    this.fuente = builder.getFuente();
    this.criterioPertenencia = builder.getCriterio();
  }

  public List<Hecho> getHechos() {
    HechosAlmacenados hechos = HechosAlmacenados.instance();
    List<Hecho> listaHechos = hechos.getHechosAlmacenados();
    return listaHechos.stream().filter(hecho -> criterioPertenencia.cumple(hecho)).toList();
  }

  public void mostrarHechos() {
    getHechos().forEach(System.out::println);
  }

  public List<Hecho> filtrarHechos(Filtro filtro) {
    return getHechos().stream().filter(filtro::cumple).toList();
  }

  public void mostrarHechosFiltrados(Filtro filtro) {
    filtrarHechos(filtro).forEach(System.out::println);
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
