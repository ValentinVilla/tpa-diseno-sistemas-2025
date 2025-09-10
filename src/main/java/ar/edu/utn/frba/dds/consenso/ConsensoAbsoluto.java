package ar.edu.utn.frba.dds.consenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("ABSOLUTO")
public class ConsensoAbsoluto extends AlgoritmoConsenso {
  @Override
  public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) {
//    for (Fuente fuente : fuentes) {
//      if (!fuente.cargarHechos(null).contains(hecho)) {
//        hecho.setConsensuado(false);
//        return; // Si al menos una fuente no contiene el hecho, no hay consenso absoluto
//      }
//    }
    boolean consenso = fuentes.stream().allMatch(fuente -> fuente.cargarHechos(null).contains(hecho));
    hecho.setConsensuado(consenso);
  }
}
