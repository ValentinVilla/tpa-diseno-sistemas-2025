package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;

import java.util.function.Consumer;

public interface FuenteProxy { //fuentes externas
  void procesarHechosDesde(String urlBase, ParametrosConsulta parametros, Consumer<Hecho> procesador);
  //void procesarHechosColeccionDesdeConId(String urlBase, String idColeccion, ParametrosConsulta parametros, Consumer<Hecho> procesador);
//hecha por tobi a chequear
  void procesarHechosColeccionDesde(String urlBase, Coleccion coleccion, ParametrosConsulta parametros, Consumer<Hecho> procesador);


}