package ar.edu.utn.frba.dds.usuarios;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Contribuyente {
  @Id
  @GeneratedValue
  private Long id;

  private Integer edad;
  private String nombre;
  private String apellido;

  protected Contribuyente() {}

  public Contribuyente(Integer edad, String nombre, String apellido) {
    this.edad = edad;
    this.nombre = nombre;
    this.apellido = apellido;
  }

  public Long getId() {
    return id;
  }

  public Integer getEdad() { return edad; }
  public String getNombre() { return nombre; }
  public String getApellido() { return apellido; }

  public void setEdad(Integer edad) { this.edad = edad; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public void setApellido(String apellido) { this.apellido = apellido; }
}
