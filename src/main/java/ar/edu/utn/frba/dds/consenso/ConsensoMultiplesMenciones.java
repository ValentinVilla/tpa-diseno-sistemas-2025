package ar.edu.utn.frba.dds.consenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import java.util.List;

public class ConsensoMultiplesMenciones implements AlgoritmoConsenso {
  @Override
  public boolean tieneConsenso(Hecho hecho, List<Fuente> fuentes) {
    int coincidencias = 0;

    /*
    El problema es que equals compara si son iguales las direcciones de los objetos, no sus atributos
    Habria que modicar eso
    */
    for (Fuente fuente : fuentes) {
      for (Hecho hechoComparable : fuente.cargarHechos(null)) {
        if (hechoComparable.equals(hecho)) {
          coincidencias++;
        } else if (hechoComparable.getTitulo().equals(hecho.getTitulo()) && !hechoComparable.equals(hecho)) {
          return false; // mismo titulo pero distintos atributos
        }
      }
    }
    return coincidencias >= 2;
  }
}
