package ar.edu.utn.frba.dds.model.fuentes.fuenteProxy;

import ar.edu.utn.frba.dds.model.clientes.ClienteMetaMapa;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;

import javax.persistence.Entity;
import javax.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity
public class FuenteMetaMapa extends Fuente {
  private String urlBase;
  @Transient
  public ClienteMetaMapa cliente;

  public FuenteMetaMapa(){}

  public FuenteMetaMapa(String urlBase) {
    this.cliente = new ClienteMetaMapa();
    this.urlBase = urlBase;
  }

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros) {
    DAOHechos repo = DAOHechos.getInstancia();
    ArrayList<Hecho> hechosMetaMapa;
    if (parametros.getColeccionId() != null) {
      return new ArrayList<>(
          cliente.obtenerHechosColeccion(urlBase, parametros.getColeccionId().toString(), parametros)
      );
    } else {
      hechosMetaMapa = new ArrayList<>(cliente.obtenerHechos(urlBase, parametros));
    }
    return repo.losQueNoFueronEliminados(hechosMetaMapa);
  }

  public void enviarSolicitudEliminacion(String url, SolicitudEliminacion solicitud) {
    cliente.enviarSolicitudEliminacion(urlBase, solicitud);
  }

  public List<Fuente> getFuente(){
    return List.of(this);
  }
}