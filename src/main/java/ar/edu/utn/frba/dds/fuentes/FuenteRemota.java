package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.fuentes.fuentesExternas.FuenteProxy;

import java.util.ArrayList;
import java.util.Objects;

public class FuenteRemota implements Fuente {
  private final String urlBase;
  private final FuenteProxy fuenteRemota;
  private final ParametrosConsulta parametros;
  private final String idColeccion; //este parametro q esta haciendo aca?

  public FuenteRemota(String urlBase, FuenteProxy fuenteProxy, ParametrosConsulta parametros) {
    this(urlBase, fuenteProxy, parametros, null);
  }

  public FuenteRemota(String urlBase, FuenteProxy fuenteProxy, ParametrosConsulta parametros, String idColeccion) {
    this.urlBase = Objects.requireNonNull(urlBase);
    this.fuenteRemota = Objects.requireNonNull(fuenteProxy);
    this.parametros = Objects.requireNonNull(parametros);
    this.idColeccion = idColeccion;
  }

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
