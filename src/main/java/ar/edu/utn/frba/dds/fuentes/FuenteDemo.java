package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.clientes.ClienteDemo;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import org.joda.time.LocalDateTime;

import javax.persistence.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class FuenteDemo extends Fuente {
  protected ClienteDemo cliente;
  private  String direccionApi;
  private LocalDateTime ultimaConsulta;
  private List<Hecho> cacheHechos;
  private RepositorioColecciones repo;

  public FuenteDemo() {}

  public FuenteDemo(ClienteDemo cliente, String direccionApi,  RepositorioColecciones repo) {
    this.cliente = cliente;
    this.direccionApi = direccionApi;
    this.ultimaConsulta = null;
    this.cacheHechos = new ArrayList<>();
    this.repo = repo;
  }

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros) {
    if (pasoUnaHoraDesdeUltimaConsulta()) {
      cacheHechos = cliente.traerHechos(direccionApi);
      ultimaConsulta = LocalDateTime.now();
    }
    Stream<Hecho> stream = cacheHechos.stream();
    if (parametros.getColeccionId() != null) {
      Coleccion coleccion = repo.buscarPorID(parametros.getColeccionId());
      stream = stream.filter(coleccion::hechoPertenece);
    }
    return stream.collect(Collectors.toCollection(ArrayList::new));
  }

  boolean pasoUnaHoraDesdeUltimaConsulta() {
    return ultimaConsulta == null || LocalDateTime.now().isAfter(ultimaConsulta.plusHours(1));
  }

  public void forzarExpiracionCache() {
    ultimaConsulta = LocalDateTime.now().minusHours(2);
  }

  public List<Fuente> getFuente(){
    return List.of(this);
  }
}
