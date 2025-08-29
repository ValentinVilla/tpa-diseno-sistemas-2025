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
  public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) {

    long coincidencias = fuentes.stream()
        .flatMap(fuente -> fuente.cargarHechos(null).stream())
        .filter(h -> h.esElMismo(hecho))
        .count();

    boolean hayConsenso = coincidencias > (fuentes.size() / 2);

    hecho.setConsensuado(hayConsenso);
  }
}

