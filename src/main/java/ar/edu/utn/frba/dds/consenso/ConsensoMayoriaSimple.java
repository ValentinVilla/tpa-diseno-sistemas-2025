package ar.edu.utn.frba.dds.consenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("MAYORIA_SIMPLE")
public class ConsensoMayoriaSimple extends AlgoritmoConsenso {
  @Override
  public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) { //List<Hecho> hechos
    int coincidencias = 0;
    for (Fuente fuente : fuentes) {
      if (fuente.cargarHechos(null).contains(hecho)) {
        coincidencias++;
      }
    }

    hecho.setConsensuado(coincidencias > (fuentes.size() / 2));
  }
}
