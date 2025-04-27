package ar.edu.utn.frba.dds;

public class Coleccion {
  private String titulo;
  private String descripcion;
  private Fuente fuente;
  private CriterioPertenencia criterio;

  // public boolean contiene(Hecho hecho, Hecho hecho2){}


  //Constructor privado para evitar la instanciación directa
  private Coleccion(Builder builder) {
    this.titulo = builder.titulo;
    this.descripcion = builder.descripcion;
    this.fuente = builder.fuente;
    this.criterio = builder.criterio;
  }

  // Builder para construir la colección
  public static class Builder {
    private String titulo;
    private String descripcion;
    private Fuente fuente;
    private CriterioPertenencia criterio;

    public Builder titulo(String titulo) {
      if (titulo == null || titulo.isEmpty()) {
        throw new CampoInvalido("El título no puede ser nulo o vacío");
      }
      this.titulo = titulo;
      return this;
    }

    public Builder descripcion(String descripcion) {
      if (descripcion == null || descripcion.isEmpty()) {
        throw new CampoInvalido("La descripcion no puede ser nula o vacía");
      }
      this.descripcion = descripcion;
      return this;
    }

    public Builder fuente(Fuente fuente) {
      if (fuente == null) {
        throw new CampoInvalido("La fuente no puede ser nula");
      }
      this.fuente = fuente;
      return this;
    }

    public Builder criterio(CriterioPertenencia criterio) {
      if (fuente == null) {
        throw new CampoInvalido("El criterio no puede ser nulo");
      }
      this.criterio = criterio;
      return this;
    }

    public Coleccion build() {
      return new Coleccion(this);
    }
  }

  // Excepciones
  public static class CampoInvalido extends RuntimeException {
    public CampoInvalido(String message) {
      super(message);
    }
  }

}
