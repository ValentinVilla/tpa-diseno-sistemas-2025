package ar.edu.utn.frba.dds.consenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import java.util.List;

public class ConsensoMultiplesMenciones implements AlgoritmoConsenso {
  @Override
  public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) {
    int coincidencias = 0;
    int coincidenciasTitulo = 0;

    /*
    El problema es que equals compara si son iguales las direcciones de los objetos, no sus atributos
    Habria que modicar eso
    */
    for (Fuente fuente : fuentes) {
      for (Hecho hechoComparable : fuente.cargarHechos(null)) {
        if (hechoComparable.esElMismo(hecho)) {
          coincidencias++;
        }
        // Si el hecho tiene el mismo titulo, pero no es el mismo hecho, se cuenta como coincidencia de titulo
        if (hechoComparable.getTitulo().equals(hecho.getTitulo())) {
          coincidenciasTitulo++;
        }
      }
    }
    hecho.setConsensuado(coincidencias >= 2 && coincidenciasTitulo == coincidencias);
  }
}
