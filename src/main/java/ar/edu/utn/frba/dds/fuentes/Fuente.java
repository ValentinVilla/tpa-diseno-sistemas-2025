package ar.edu.utn.frba.dds.fuentes;

import java.util.stream.Stream;
import ar.edu.utn.frba.dds.dominio.Hecho;

public interface Fuente {
  Stream<Hecho> cargarHechos();
}

