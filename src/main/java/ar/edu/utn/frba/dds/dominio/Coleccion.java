package ar.edu.utn.frba.dds.dominio;

import java.util.Objects;
import java.util.List;
import ar.edu.utn.frba.dds.filtros.*;

public class Coleccion {
  private final String titulo;
  private final String descripcion;
  private final Fuente fuente;
  private Filtro criterioPertenencia = new FiltroCategoria("General");

  public List<Hecho> getHechos() {
    HechosAlmacenados hechos = HechosAlmacenados.instance();
    List<Hecho> listaHechos = hechos.getHechosAlmacenados();
    return listaHechos.stream().filter(hecho -> criterioPertenencia.cumple(hecho)).toList();
  }

  public void mostrarHechos() {
    getHechos().stream().forEach(hecho -> System.out.println(hecho));
  }

  public List<Hecho> filtrarHechos(Filtro filtro) {
    return getHechos().stream().filter(hecho -> filtro.cumple(hecho)).toList();
  }

  public void mostrarHechosFiltrados(Filtro filtro) {
    filtrarHechos(filtro).stream().forEach(hecho -> System.out.println(hecho));
  }

  public void setFiltro(Filtro filtro) {
    this.criterioPertenencia = filtro;
  }

  private Coleccion(Builder builder) {
    this.titulo = builder.titulo;
    this.descripcion = builder.descripcion;
    this.fuente = builder.fuente;
    this.criterioPertenencia = builder.criterio;
  }

  public static class Builder {
    private String titulo;
    private String descripcion;
    private Fuente fuente;
    private Filtro criterio;

    public Builder titulo(String titulo) {
      this.titulo = validateNotNullOrEmpty(titulo, "El título no puede ser nulo o vacío");
      return this;
    }

    public Builder descripcion(String descripcion) {
      this.descripcion = validateNotNullOrEmpty(descripcion, "La descripcion no puede ser nula o vacía");
      return this;
    }

    public Builder fuente(Fuente fuente) {
      Objects.requireNonNull(fuente, "La fuente no puede ser nula");
      this.fuente = fuente;
      return this;
    }

    public Builder criterio(Filtro criterio) {
      Objects.requireNonNull(criterio, "El criterio no puede ser nulo");
      this.criterio = criterio;
      return this;
    }

    public Coleccion build() {
      return new Coleccion(this);
    }

    private String validateNotNullOrEmpty(String value, String errorMessage) {
      if (value == null || value.isEmpty()) {
        throw new CampoInvalido(errorMessage);
      }
      return value;
    }
  }

  public static class CampoInvalido extends RuntimeException {
    public CampoInvalido(String message) {
      super(message);
    }
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