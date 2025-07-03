package ar.edu.utn.frba.dds.consenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;

import java.util.List;

public class ConsensoMayoriaSimple implements AlgoritmoConsenso {

  @Override
  public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) { //List<Hecho> hechos
    int coincidencias = 0;

    for (Fuente fuente : fuentes) {
      if (fuente.cargarHechos(null).contains(hecho)) {
        coincidencias++;
      }
    }

    if(coincidencias > (fuentes.size() / 2)) {
      hecho.setConsensuado(true);
      // Hay consenso si la mayoria de las fuentes contienen el hecho
    } else {
      hecho.setConsensuado(false);
      // No hay consenso si no se alcanza la mayoría
    }
  }
}
