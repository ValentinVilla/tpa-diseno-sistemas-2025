package ar.edu.utn.frba.dds.usuarios;

public class Contibuyente {
  private Integer id;
  private Integer edad;
  private String nombre;
  private String apellido;

  public Contibuyente(Integer id,Integer edad, String nombre, String apellido) {
    if (nombre == null || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre es obligatorio");
    }
    this.id = id;
    this.nombre = nombre;
    this.apellido = apellido;
  }
}
