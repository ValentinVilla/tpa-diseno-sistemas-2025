package ar.edu.utn.frba.dds.dominio.builders;

import java.util.Objects;

import ar.edu.utn.frba.dds.ModoNavegacion;
import ar.edu.utn.frba.dds.consenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.filtros.Filtro;

public class ColeccionBuilder {
  private String titulo;
  private String descripcion;
  private Fuente fuente;
  private Filtro criterio;
  private String handle; // Handle para identificar la colección en el sistema
  private ModoNavegacion modoNavegacion; // Modo de navegación por defecto
  private AlgoritmoConsenso algoritmoConsenso; // Algoritmo de consenso por defecto

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

  public ColeccionBuilder handle(String handle) {
    this.handle = validateNotNullOrEmpty(handle, "El handle no puede ser nulo o vacío");
    return this;
  }

  public ColeccionBuilder modoNavegacion(ModoNavegacion modoNavegacion) {
    this.modoNavegacion = modoNavegacion;
    return this;
  }

  public ColeccionBuilder algoritmoConsenso(AlgoritmoConsenso algoritmoConsenso) {
    this.algoritmoConsenso = algoritmoConsenso;
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

  public String getHandle() {
    return handle;
  }

  public ModoNavegacion getModoNavegacion() {
    return modoNavegacion;
  }

  public AlgoritmoConsenso getAlgoritmoConsenso() {
    return algoritmoConsenso;
  }



  public static class CampoInvalido extends RuntimeException {
    public CampoInvalido(String message) {
      super(message);
    }
  }
}

