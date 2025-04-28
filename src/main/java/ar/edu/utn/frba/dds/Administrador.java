package ar.edu.utn.frba.dds;

public class Administrador {
  public Coleccion crearColeccion(String titulo, String descripcion, Fuente fuente, CriterioPertenencia criterio) {
    return new Coleccion.Builder()
        .titulo(titulo)
        .descripcion(descripcion)
        .fuente(fuente)
        .criterio(criterio)
        .build();
  }

  void aceptarSolicitud(SolicitudEliminacion solicitud) {
    ListaDeSolicitudes.instance().sacarSolicitud(solicitud);
    solicitud.getHecho().setVisible(false);
    //creamos el campo visible ya que cuando se rechaza la solicitud va a dejar de ser visible el hecho, entonces si la solicitudDeEliminacion es aceptada => visible == false.
  }

  public void rechazarSolicitud(SolicitudEliminacion solicitud) {
    ListaDeSolicitudes.instance().sacarSolicitud(solicitud);
  }


 // public importarHechos(){}


}
