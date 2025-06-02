package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;

import java.util.ArrayList;
import java.util.Objects;

public class FuenteProxy implements Fuente {
  private final String urlBase;
  private final FuenteRemota fuenteRemota;
  private final ParametrosConsulta parametros;
  private final String idColeccion;

  public FuenteProxy(String urlBase, FuenteRemota fuenteRemota, ParametrosConsulta parametros) {
    this(urlBase, fuenteRemota, parametros, null);
  }

  public FuenteProxy(String urlBase, FuenteRemota fuenteRemota, ParametrosConsulta parametros, String idColeccion) {
    this.urlBase = Objects.requireNonNull(urlBase);
    this.fuenteRemota = Objects.requireNonNull(fuenteRemota);
    this.parametros = Objects.requireNonNull(parametros);
    this.idColeccion = idColeccion;
  }//pq hay 2 constructores??

  @Override
  public ArrayList<Hecho> cargarHechos() {
    ArrayList<Hecho> hechos = new ArrayList<>();
    if (idColeccion != null) {
      fuenteRemota.procesarHechosColeccionDesde(urlBase, idColeccion, parametros, hechos::add);
    } else {
      fuenteRemota.procesarHechosDesde(urlBase, parametros, hechos::add);
    }
    return hechos;
  }
}
//public void procesarHechosDesde(String urlBase, ParametrosConsulta parametros, Consumer<Hecho> procesador) {
//  List<Hecho> hechos = cliente.obtenerHechos(urlBase, parametros);
//  LocalDateTime haceUnaHora = LocalDateTime.now().minusHours(1);
//
//  List<Hecho> hechosRecientes = hechos.stream()
//    .filter(hecho -> hecho.getFechaCarga().isAfter(haceUnaHora))
//    .collect(Collectors.toList());
//
//  hechosRecientes.forEach(procesador);
//}
