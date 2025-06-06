package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.solicitudes.Solicitud;
import java.util.ArrayList;
import java.util.List;

public class RepositorioSolicitudes {
  private List<Solicitud> solicitudes = new ArrayList<>();

  public List<Solicitud> obtenerTodas() {
    return new ArrayList<>(solicitudes);
  }

  public void guardar(Solicitud solicitud) {
    solicitudes.add(solicitud);
  }

  public void eliminar(Solicitud solicitud) {
    solicitudes.remove(solicitud);
  }

  public void actualizar(Solicitud solicitud) {
    //TODO
  }
}