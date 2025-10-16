package ar.edu.utn.frba.dds.model.fuentes.fuenteProxy;

import ar.edu.utn.frba.dds.model.clientes.ClienteDemo;
import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;
import org.joda.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Transient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class FuenteDemo extends Fuente {
  private  String direccionApi;
  private LocalDateTime ultimaConsulta;
  @Transient
  protected ClienteDemo cliente;
  @Transient
  private List<Hecho> cacheHechos;
  @Transient
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
    DAOHechos repo = DAOHechos.getInstancia();
    return repo.losQueNoFueronEliminados(stream.collect(Collectors.toCollection(ArrayList::new)));
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
