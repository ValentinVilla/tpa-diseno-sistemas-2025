package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;

import java.util.ArrayList;

public class FuenteDinamica {
  private final RepositorioHechos repositorioHechos;

  public FuenteDinamica(String nombreFuente, RepositorioHechos repositorioHechos) {
    this.repositorioHechos = repositorioHechos;
  }

  // Caso 1: usuario anónimo
  public void subirHecho(Hecho hecho) {
    hecho.setEditable(false);
    repositorioHechos.guardar(this, hecho);
  }//este subir hechos lo sacaria dejaria solo el otro asumiendo q si no es registrado ese numero de idContribuyenteCreador es -1

  // Caso 2: usuario registrado
  public void subirHecho(int idContribuyenteCreador, Hecho hecho) {
    hecho.setEditable(true); // editable solo por 3 semanas (lo sacaria)
    hecho.setIdContribuyenteCreador(idContribuyenteCreador);
    repositorioHechos.guardar(this, hecho);
  }

  public boolean puedeModificar(int idContribuyenteCreador, Hecho hecho) {
    return hecho.getIdContribuyenteCreador()==(idContribuyenteCreador)
        && hecho.esEditableActualmente();
  }

  public void modificarHecho(int idContribuyenteCreador, Hecho hechoOriginal, Hecho datosNuevos) {
    if (!puedeModificar(idContribuyenteCreador, hechoOriginal)) {
      throw new RuntimeException("No tenés permiso para modificar este hecho.");
    }
    else
      hechoOriginal.actualizarDesde(datosNuevos);
  }

  public ArrayList<Hecho> cargarHechos(){
    return new ArrayList<>(repositorioHechos.obtenerhechosDe(this));
  }
}
