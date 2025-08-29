package ar.edu.utn.frba.dds.consenso;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import java.util.List;

public class ConsensoMultiplesMenciones implements AlgoritmoConsenso {
  @Override
  public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) {

    List<Hecho> coincidenciasTitulo = fuentes.stream()
        .flatMap(fuente -> fuente.cargarHechos(null).stream())
        .filter(alguno -> alguno.tieneMismoTitulo(hecho))
        .toList();
    if (coincidenciasTitulo.size() <= 1) {
      hecho.setConsensuado(false);
    }
    if (coincidenciasTitulo.size() >= 2) {
      int cantCoinicidenciasTitulo = coincidenciasTitulo.size();
      int cantidadCoincidencias = coincidenciasTitulo.stream().filter(unHecho -> hecho.esElMismo(unHecho)).toList().size();
      hecho.setConsensuado(cantCoinicidenciasTitulo == cantidadCoincidencias);
    }//esto lo que hace es primero filtra por todos los que coinciden en titulo luego se fija si hay mas de dos que coinicidan en titulo, de ser el caso se va a fijar si todas las coincidencias de titulo son iguales que las coincidencias del hecho completo pq de ser el caso debe estar consensuado.
  }
}/*
    El problema es que equals compara si son iguales las direcciones de los objetos, no sus atributos
    Habria que modicar eso

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
    if (coincidencias >= 2 && coincidenciasTitulo == coincidencias) {
      hecho.setConsensuado(true);
    } else {
      hecho.setConsensuado(false);
    }
    */

