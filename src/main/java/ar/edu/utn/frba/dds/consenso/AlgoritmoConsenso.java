package ar.edu.utn.frba.dds.consenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import java.util.List;

public interface AlgoritmoConsenso {
  void tieneConsenso(Hecho hecho, List<Fuente> fuentes);
}
