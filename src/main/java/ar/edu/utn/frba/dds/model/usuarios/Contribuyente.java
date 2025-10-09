package ar.edu.utn.frba.dds.model.usuarios;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Contribuyente {
  @Id
  @GeneratedValue
  private Long id;

  private String nombre;
  private String apellido;
  private Integer telefono;
  private String mail;
  private Integer edad;

  protected Contribuyente() {}

  public Contribuyente(String nombre, String apellido, Integer telefono, String mail, Integer edad) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.telefono = telefono;
    this.mail = mail;
    this.edad = edad;
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
