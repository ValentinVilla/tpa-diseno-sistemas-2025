package ar.edu.utn.frba.dds.dominio.builders;

import java.util.Objects;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Fuente;
import ar.edu.utn.frba.dds.filtros.Filtro;

public class ColeccionBuilder {
  private String titulo;
  private String descripcion;
  private Fuente fuente;
  private Filtro criterio;

  public ColeccionBuilder titulo(String titulo) {
    this.titulo = validateNotNullOrEmpty(titulo, "El título no puede ser nulo o vacío");
    return this;
  }

  public ColeccionBuilder descripcion(String descripcion) {
    this.descripcion = validateNotNullOrEmpty(descripcion, "La descripcion no puede ser nula o vacía");
    return this;
  }

  public ColeccionBuilder fuente(Fuente fuente) {
    Objects.requireNonNull(fuente, "La fuente no puede ser nula");
    this.fuente = fuente;
    return this;
  }

  public ColeccionBuilder criterio(Filtro criterio) {
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

  // Getters para que Coleccion acceda a los campos
  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Fuente getFuente() {
    return fuente;
  }

  public Filtro getCriterio() {
    return criterio;
  }

  public static class CampoInvalido extends RuntimeException {
    public CampoInvalido(String message) {
      super(message);
    }
  }
}

