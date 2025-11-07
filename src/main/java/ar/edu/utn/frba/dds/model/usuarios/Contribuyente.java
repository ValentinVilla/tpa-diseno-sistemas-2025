package ar.edu.utn.frba.dds.model.usuarios;

import javax.persistence.Column;
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
  private String telefono;
  private String mail;
  private Integer edad;
  private String password;
  @Column(nullable = false)
  private Boolean esAdmin = false;

  protected Contribuyente() {}

  public Contribuyente(String nombre, String apellido, String telefono, String mail, Integer edad, String password) {
    this(nombre, apellido, telefono, mail, edad, password, false);
  }

  public Contribuyente(String nombre, String apellido, String telefono, String mail, Integer edad, String password, Boolean esAdmin) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.telefono = telefono;
    this.mail = mail;
    this.edad = edad;
    this.password = password;
    this.esAdmin = esAdmin != null && esAdmin;
  }

  public Long getId() {
    return id;
  }

  public Integer getEdad() { return edad; }
  public String getNombre() { return nombre; }
  public String getApellido() { return apellido; }
  public String getPassword() { return password; }

  public Boolean getEsAdmin() { return esAdmin != null && esAdmin; }

  public String getNombreCompleto(){ return this.nombre + " " + this.apellido; }

  public void setEsAdmin(Boolean esAdmin) { this.esAdmin = esAdmin != null && esAdmin; }
  public void setEdad(Integer edad) { this.edad = edad; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public void setApellido(String apellido) { this.apellido = apellido; }
  public void setPassword(String password) { this.password = password; }
}
