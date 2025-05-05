package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import java.util.ArrayList;
import java.util.List;

public class RepositorioSolicitudes {
  private List<SolicitudEliminacion> solicitudes = new ArrayList<>();

  public List<SolicitudEliminacion> obtenerTodas() {
    return new ArrayList<>(solicitudes);
  }

  public void guardar(SolicitudEliminacion solicitud) {
    solicitudes.add(solicitud);
  }

  public void eliminar(SolicitudEliminacion solicitud) {
    solicitudes.remove(solicitud);
  }

  public void actualizar(SolicitudEliminacion solicitud) {
    //TODO
  }
}