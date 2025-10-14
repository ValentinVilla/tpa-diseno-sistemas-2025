package ar.edu.utn.frba.dds.fuentes.fuenteProxy;

import ar.edu.utn.frba.dds.clientes.ClienteMetaMapa;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;

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