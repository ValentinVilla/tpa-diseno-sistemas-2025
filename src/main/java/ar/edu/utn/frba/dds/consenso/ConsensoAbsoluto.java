package ar.edu.utn.frba.dds.consenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;

import java.util.List;

public class ConsensoAbsoluto implements AlgoritmoConsenso {

  @Override
  public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) {
    for (Fuente fuente : fuentes) {
      if (!fuente.cargarHechos(null).contains(hecho)) {
        hecho.setConsensuado(false);
        return; // Si al menos una fuente no contiene el hecho, no hay consenso absoluto
      }
    }
    hecho.setConsensuado(true);
  }
}
