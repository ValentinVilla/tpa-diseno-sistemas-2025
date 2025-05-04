package ar.edu.utn.frba.dds.filtros;

import ar.edu.utn.frba.dds.dominio.Hecho;

public interface Filtro {
  boolean cumple(Hecho hecho);
}
