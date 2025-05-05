package ar.edu.utn.frba.dds.users;

import ar.edu.utn.frba.dds.fuentes.*;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.fuentes.FuenteEstatica;
import ar.edu.utn.frba.dds.servicios.ColeccionService;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;

public class Administrador extends Contribuyente{
  private FuenteEstatica ultimaFuenteEstatica;

    public Administrador(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

  public void crearColeccion(String titulo, String descripcion, Fuente fuente, Filtro criterio) {
    coleccionService.crearColeccion(titulo, descripcion, fuente, criterio);
  }

  public void aceptarSolicitud(SolicitudEliminacion solicitud) {
    solicitudService.aprobarSolicitud(solicitud);
  }

  public void rechazarSolicitud(SolicitudEliminacion solicitud) {
    solicitudService.rechazarSolicitud(solicitud);
  }

  public void importarHechos(String pathCSV, String categoria) {
    this.ultimaFuenteEstatica = new FuenteEstatica(pathCSV, categoria);
    ultimaFuenteEstatica.cargarHechos();
  }

  public FuenteEstatica getUltimaFuenteEstatica() {
    return ultimaFuenteEstatica;
  }
}


