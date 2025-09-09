package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.ModoNavegacion;
import ar.edu.utn.frba.dds.consenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import ar.edu.utn.frba.dds.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.filtros.Filtro;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Coleccion {
  @Id
  @GeneratedValue
  public Long id;

  private String titulo;
  private String descripcion;
  public String handle;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "fuente_id",  nullable = false)
  private Fuente fuente;
  @ManyToOne(cascade = CascadeType.ALL)
  public Filtro criterioPertenencia;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "consenso_id", nullable = false)
  AlgoritmoConsenso algoritmoConsenso;
  @Enumerated(EnumType.STRING)
  ModoNavegacion modoNavegacion;

  public Coleccion(){}

  public Coleccion(ColeccionBuilder builder) {
    this.titulo = builder.getTitulo();
    this.descripcion = builder.getDescripcion();
    this.fuente = builder.getFuente();
    this.criterioPertenencia = builder.getCriterio();
    this.modoNavegacion = builder.getModoNavegacion();
    this.algoritmoConsenso = builder.getAlgoritmoConsenso();
  }

  public List<Hecho> mostrarHechos() {

    List<Hecho> hechos = fuente.cargarHechos(null);
    List<Hecho> resultado = new ArrayList<>();

    for (Hecho hecho : hechos) {
      if (criterioPertenencia.cumple(hecho)) {
        resultado.add(hecho);
      }
    }

    return modoNavegacion.mostrarHechos(resultado);
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

  public void ejecutarAlgoritmo(List<Hecho> hechos) {
    for (Hecho hecho : hechos) {
      algoritmoConsenso.tieneConsenso(hecho, fuente.getFuente());
    }
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

  public Long getId() {
    return id;
  }
}


