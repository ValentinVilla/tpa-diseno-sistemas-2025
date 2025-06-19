package ar.edu.utn.frba.dds.usuarios;

public class Contribuyente {
  private Integer id;
  private Integer edad;
  private String nombre;
  private String apellido;

  public Contribuyente(Integer id, Integer edad, String nombre, String apellido) {
    if (nombre == null || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre es obligatorio");
    }
    this.edad = edad;
    this.id = id;
    this.nombre = nombre;
    this.apellido = apellido;
  }

  public Integer getId() {
    return id;
  }

}
