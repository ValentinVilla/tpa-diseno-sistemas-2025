package ar.edu.utn.frba.dds;

public class Administrador {
  private String username;
  private String password;


  public Coleccion crearColeccion(String titulo, String descripcion, Fuente fuente, CriterioPertenencia criterio) {

    return new Coleccion.Builder()
        .titulo(titulo)
        .descripcion(descripcion)
        .fuente(fuente)
        .criterio(criterio)
        .build();
  }

  void aceptarSolicitud(SolicitudEliminacion solicitud, ListaDeSolicitudes listaDeSolicitudes){
    listaDeSolicitudes.sacarSolicitud(solicitud);
    solicitud.getHecho().setVisible(false); //creamos el campo visible ya que cuando se rechaza la solicitud va a dejar de ser visible el hecho, entonces si la solicitudDeEliminacion es aceptada => visible == false.
  }

  public void rechazarSolicitud(SolicitudEliminacion solicitud, ListaDeSolicitudes listaDeSolicitudes){
    listaDeSolicitudes.sacarSolicitud(solicitud);
  }


 // public importarHechos(){}


}
