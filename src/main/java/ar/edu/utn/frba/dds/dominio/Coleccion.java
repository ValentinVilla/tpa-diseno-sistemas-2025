package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.fuentes.Fuente;
import java.util.ArrayList;
import java.util.List;

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
    /*
    Acá la propuesta del profe era:
    coleccion.java: return fuente.cargarHechos().stream().filter(hecho -> criterioPertenencia.cumple(hecho)).toList();

    creo que refiere a que los hechos se listen de su respectiva fuente pero mientras tanto elimino los streams como pidio
     */
    List<Hecho> resultado = new ArrayList<>();
    for (Hecho hecho : hechos) {
      if (criterioPertenencia.cumple(hecho)) {
        resultado.add(hecho);
      }
    }
    return resultado;
  }

  public List<Hecho> hechosFiltrados(List<Hecho> hechos, Filtro filtro) {
    List<Hecho> resultado = new ArrayList<>();
    for (Hecho hecho : mostrarHechos(hechos)) {
      if (filtro.cumple(hecho)) {
        resultado.add(hecho);
      }
    }
    return resultado;
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
