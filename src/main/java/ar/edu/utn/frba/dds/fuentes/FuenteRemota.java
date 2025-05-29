package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;

import java.util.function.Consumer;

public interface FuenteRemota {
  void procesarHechosDesde(String urlBase, ParametrosConsulta parametros, Consumer<Hecho> procesador);
  void procesarHechosColeccionDesde(String urlBase, String idColeccion, ParametrosConsulta parametros, Consumer<Hecho> procesador);
}
