package ar.edu.utn.frba.dds.consenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("MULTIPLES_MENCIONES")
public class ConsensoMultiplesMenciones extends AlgoritmoConsenso {

  @Override
  public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) {
    int coincidencias = 0;
    int coincidenciasTitulo = 0;

    for (Fuente fuente : fuentes) {
      for (Hecho hechoComparable : fuente.cargarHechos(null)) {
        if (hechoComparable.esElMismo(hecho)) {
          coincidencias++;
        }
        if (hechoComparable.getTitulo().equals(hecho.getTitulo())) {
          coincidenciasTitulo++;
        }
      }
    }
    hecho.setConsensuado(coincidencias >= 2 && coincidenciasTitulo == coincidencias);
  }
}
