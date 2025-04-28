package ar.edu.utn.frba.dds;

import java.util.Objects;

public class Coleccion {
  private final String titulo;
  private final String descripcion;
  private final Fuente fuente;
  private final CriterioPertenencia criterio;

  // Constructor privado para evitar la instanciación directa
  private Coleccion(Builder builder) {
    this.titulo = builder.titulo;
    this.descripcion = builder.descripcion;
    this.fuente = builder.fuente;
    this.criterio = builder.criterio;
  }

  // Getters para acceder a los atributos
  public String getTitulo() {
    return titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public Fuente getFuente() {
    return fuente;
  }

  public CriterioPertenencia getCriterio() {
    return criterio;
  }

  // Método para verificar si un Hecho está contenido en la colección
  public boolean contiene(Hecho hecho) {
    // Lógica para verificar si el hecho está en la colección
    return false; // Solo un ejemplo, deberías definir la lógica aquí.
  }

  // Builder para construir la colección
  public static class Builder {
    private String titulo;
    private String descripcion;
    private Fuente fuente;
    private CriterioPertenencia criterio;

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

    public Builder criterio(CriterioPertenencia criterio) {
      Objects.requireNonNull(criterio, "El criterio no puede ser nulo");
      this.criterio = criterio;
      return this;
    }

    // Método para construir la instancia
    public Coleccion build() {
      return new Coleccion(this);
    }

    // Método privado para validar que los valores no sean nulos o vacíos
    private String validateNotNullOrEmpty(String value, String errorMessage) {
      if (value == null || value.isEmpty()) {
        throw new CampoInvalido(errorMessage);
      }
      return value;
    }
  }

  // Excepciones
  public static class CampoInvalido extends RuntimeException {
    public CampoInvalido(String message) {
      super(message);
    }
  }
}

