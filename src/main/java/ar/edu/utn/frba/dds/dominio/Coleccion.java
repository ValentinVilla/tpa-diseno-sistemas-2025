package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.fuentes.Fuente;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ar.edu.utn.frba.dds.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.filtros.Filtro;

public class Coleccion {
  private final String titulo;
  private final String descripcion;
  private final Fuente fuente;
  public Filtro criterioPertenencia;
  public String handle;

  public Coleccion(ColeccionBuilder builder) {
    this.handle = UUID.randomUUID().toString();
    this.titulo = builder.getTitulo();
    this.descripcion = builder.getDescripcion();
    this.fuente = builder.getFuente();
    this.criterioPertenencia = builder.getCriterio();
  }

  public List<Hecho> mostrarHechos() {

    List<Hecho> hechos = fuente.cargarHechos();

    List<Hecho> resultado = new ArrayList<>();

    for (Hecho hecho : hechos) {
      if (criterioPertenencia.cumple(hecho)) {
        resultado.add(hecho);
      }
    }
    return resultado;
  }

  public List<Hecho> hechosFiltrados(Filtro filtro) {
    List<Hecho> resultado = new ArrayList<>();
    for (Hecho hecho : mostrarHechos()) {
      if (filtro.cumple(hecho)) {
        resultado.add(hecho);
      }
    }
    return resultado;
  }

  public boolean hechoPertenece(Hecho hecho) {
    return criterioPertenencia.cumple(hecho);
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
