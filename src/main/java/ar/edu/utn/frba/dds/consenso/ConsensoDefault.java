package ar.edu.utn.frba.dds.consenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;

import java.util.List;

public class ConsensoDefault implements AlgoritmoConsenso {

  @Override
  public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) {
    hecho.setConsensuado(true);
  }

}
