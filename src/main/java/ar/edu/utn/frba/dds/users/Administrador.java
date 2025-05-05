package ar.edu.utn.frba.dds.users;

import ar.edu.utn.frba.dds.dominio.CSVReader;
import ar.edu.utn.frba.dds.dominio.Fuente;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;

public class Administrador extends Contribuyente{

  public void crearColeccion(String titulo, String descripcion, Fuente fuente, Filtro criterio) {
    coleccionService.crearColeccion(titulo, descripcion, fuente, criterio);
  }

  public void aceptarSolicitud(SolicitudEliminacion solicitud) {
    solicitudService.aprobarSolicitud(solicitud);
  }

  public void rechazarSolicitud(SolicitudEliminacion solicitud) {
    solicitudService.rechazarSolicitud(solicitud);
  }

  public void mostrarHechosDesdeFuente(String fuente, String categoria) {
    CSVReader reader = new CSVReader();
    reader.leerLote(fuente, categoria, hecho -> {
      System.out.println("Título: " + hecho.getTitulo());
      System.out.println("Descripcion: " + hecho.getDescripcion());
      System.out.println("Categoria: "+ hecho.getCategoria());
      System.out.println("Latitud: " + hecho.getLatitud());
      System.out.println("Longitud: " + hecho.getLongitud());
      System.out.println("---------------------------------------------------");
    });
  }
}


