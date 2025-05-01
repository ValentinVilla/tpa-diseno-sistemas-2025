package ar.edu.utn.frba.dds.users;

import ar.edu.utn.frba.dds.dominio.CSVReader;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.Fuente;
import ar.edu.utn.frba.dds.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.solicitudes.ListaDeSolicitudes;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;

public class Administrador extends Contribuyente{

     public Coleccion crearColeccion(String titulo, String descripcion, Fuente fuente, Filtro criterio) {
       return new ColeccionBuilder()
           .titulo(titulo)
           .descripcion(descripcion)
           .fuente(fuente)
           .criterio(criterio)
           .build();
    }

  void aceptarSolicitud(SolicitudEliminacion solicitud) {
    ListaDeSolicitudes.instance().sacarSolicitud(solicitud);
    solicitud.getHecho().setVisible(false); //solicitud rechazada => hecho no visible
  }

  public void rechazarSolicitud(SolicitudEliminacion solicitud) {
    ListaDeSolicitudes.instance().sacarSolicitud(solicitud);
  }

  public void mostrarHechosDesdeFuente(String fuente) {
    CSVReader reader = new CSVReader();
    reader.leerLote(fuente, hecho -> {
      System.out.println("Título: " + hecho.getTitulo());
      System.out.println("Descripcion: " + hecho.getDescripcion());
      System.out.println("Latitud: " + hecho.getLatitud());
      System.out.println("Longitud: " + hecho.getLongitud());
      System.out.println("---------------------------------------------------");
    });
  }
}


