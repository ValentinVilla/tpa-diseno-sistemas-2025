package ar.edu.utn.frba.dds.model.filtros;

import ar.edu.utn.frba.dds.model.dominio.Hecho;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("TEXTO")
public class FiltroTexto extends Filtro {

  private String textoBuscado;

  public FiltroTexto(String textoBuscado) {
    this.textoBuscado = textoBuscado != null ? textoBuscado.toLowerCase() : null;
  }

  public FiltroTexto() {}

  @Override
  public boolean cumple(Hecho hecho) {
    if (textoBuscado == null || textoBuscado.isEmpty()) {
      return true;
    }

    String titulo = hecho.getTitulo() != null ? hecho.getTitulo().toLowerCase() : "";
    String descripcion = hecho.getDescripcion() != null ? hecho.getDescripcion().toLowerCase() : "";

    return titulo.contains(textoBuscado) || descripcion.contains(textoBuscado);
  }

  public String getTextoBuscado() { return textoBuscado; }

  @Override
  public String getDescripcion() {
    return "Contiene texto: \"" + (textoBuscado != null ? textoBuscado : "") + "\"";
  }
}


