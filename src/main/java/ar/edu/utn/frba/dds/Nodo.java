package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.Fuente;

import java.util.List;

public class Nodo {
  List<Fuente> fuentesDelNodo;
  List<Coleccion> coleccionesDelNodo;
  List<Hecho> hechosDelNodo;

  void cargarHechosALista(List<Fuente> fuentes) {
    for (Fuente fuente : fuentes) {
      List<Hecho> hechos = fuente.cargarHechos(null);
      this.hechosDelNodo.addAll(hechos);
    }
  }

  void cargarColeccionesALista() {
    for (Coleccion coleccion : coleccionesDelNodo) {
      this.hechosDelNodo.addAll(coleccion.mostrarHechos());
    }
  }

  void calcularConsenso(){

  }

}
