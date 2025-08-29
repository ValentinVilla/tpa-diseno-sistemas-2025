package ar.edu.utn.frba.dds.consenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("DEFAULT")
public class ConsensoDefault extends AlgoritmoConsenso {
  @Override
  public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) {
    hecho.setConsensuado(true);
  }
}
