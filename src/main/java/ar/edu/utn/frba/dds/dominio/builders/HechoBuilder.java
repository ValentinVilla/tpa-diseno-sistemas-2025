package ar.edu.utn.frba.dds.dominio.builders;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;

import java.time.LocalDate;

public class HechoBuilder {
  private String titulo;
  private String descripcion;
  private String categoria;
  private double latitud;
  private double longitud;
  private LocalDate fechaAcontecimiento;
  private LocalDate fechaCarga;
  private Origen origen;
  private boolean visible = true;
  private LocalDate fechaModificacion;
  private String provincia;

  public HechoBuilder titulo(String titulo) {
    this.titulo = validateNotNullOrEmpty(titulo, "El título no puede ser nulo o vacío");
    return this;
  }

  public HechoBuilder descripcion(String descripcion) {
    this.descripcion = validateNotNullOrEmpty(descripcion, "La descripción no puede ser nula o vacía");
    return this;
  }

  public HechoBuilder categoria(String categoria) {
    this.categoria = validateNotNullOrEmpty(categoria, "La categoría no puede ser nula o vacía");
    return this;
  }

  public HechoBuilder latitud(double latitud) {
    if (latitud < -90 || latitud > 90) {
      throw new CampoInvalido("La latitud debe estar entre -90 y 90");
    }
    this.latitud = latitud;
    return this;
  }

  public HechoBuilder longitud(double longitud) {
    if (longitud < -180 || longitud > 180) {
      throw new CampoInvalido("La longitud debe estar entre -180 y 180");
    }
    this.longitud = longitud;
    return this;
  }

  public HechoBuilder fechaAcontecimiento(LocalDate fechaAcontecimiento) {
    if (fechaAcontecimiento == null) {
      throw new CampoInvalido("La fecha de acontecimiento no puede ser nula");
    }
    this.fechaAcontecimiento = fechaAcontecimiento;
    return this;
  }

  public HechoBuilder fechaCarga(LocalDate fechaCarga) {
    if (fechaCarga == null) {
      throw new CampoInvalido("La fecha de carga no puede ser nula");
    }
    this.fechaCarga = fechaCarga;
    return this;
  }

  public HechoBuilder origen(Origen origen) {
    if (origen == null) {
      throw new CampoInvalido("El origen no puede ser nulo");
    }
    this.origen = origen;
    return this;
  }

  public HechoBuilder visible(boolean visible) {
    this.visible = visible;
    return this;
  }
  public HechoBuilder fechaModificacion(LocalDate fechaModificacion) {
    this.fechaModificacion = fechaModificacion;
    return this;
  }

  public HechoBuilder provincia(String provincia) {
    this.provincia =  provincia;
    return this;
  }

  public Hecho build() {
    // Validación final por seguridad (defensiva, en caso de setters alternativos)
    validateNotNullOrEmpty(titulo, "El título no puede ser nulo o vacío");
    validateNotNullOrEmpty(descripcion, "La descripción no puede ser nula o vacía");
    validateNotNullOrEmpty(categoria, "La categoría no puede ser nula o vacía");

    if (fechaAcontecimiento == null) {
      throw new CampoInvalido("La fecha de acontecimiento no puede ser nula");
    }
    if (fechaCarga == null) {
      throw new CampoInvalido("La fecha de carga no puede ser nula");
    }
    if (origen == null) {
      throw new CampoInvalido("El origen no puede ser nulo");
    }


    return new Hecho(this);
  }

  private String validateNotNullOrEmpty(String value, String errorMessage) {
    if (value == null || value.isEmpty()) {
      throw new CampoInvalido(errorMessage);
    }
    return value;
  }

  public String getTitulo() { return titulo; }
  public String getDescripcion() { return descripcion; }
  public String getCategoria() { return categoria; }
  public double getLatitud() { return latitud; }
  public double getLongitud() { return longitud; }
  public LocalDate getFechaAcontecimiento() { return fechaAcontecimiento; }
  public LocalDate getFechaCarga() { return fechaCarga; }
  public Origen getOrigen() { return origen; }
  public boolean isVisible() { return visible; }
  public LocalDate getFechaModificacion() { return fechaModificacion; }

  public static class CampoInvalido extends RuntimeException {
    public CampoInvalido(String mensaje) {
      super(mensaje);
    }
  }
}
