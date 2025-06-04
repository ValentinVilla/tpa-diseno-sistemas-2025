package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;

import java.util.ArrayList;

public class FuenteDinamica implements Fuente {
  private final RepositorioHechos repositorioHechos;

  public FuenteDinamica(RepositorioHechos repositorioHechos) {
    this.repositorioHechos = repositorioHechos;
  }

  public void subirHecho(Hecho hecho) {
    subirHecho(-1, hecho);
  }

  public void subirHecho(int idContribuyenteCreador, Hecho hecho) {
    hecho.setIdContribuyenteCreador(idContribuyenteCreador);
    repositorioHechos.guardar(this, hecho);
  }

  public void modificarHecho(int idContribuyenteCreador, Hecho hechoOriginal, Hecho datosNuevos) {
    if (!puedeModificar(idContribuyenteCreador, hechoOriginal)) {
      throw new RuntimeException("No tenés permiso para modificar este hecho.");
    }
    else
      hechoOriginal.actualizarDesde(datosNuevos);
  }

  public boolean puedeModificar(int idContribuyenteCreador, Hecho hecho) {
    return hecho.getIdContribuyenteCreador()==(idContribuyenteCreador)
        && hecho.estaDentroDePlazo();
  }

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros) {
    return cargarHechos();
  }

  public ArrayList<Hecho> cargarHechos(){
    return new ArrayList<>(repositorioHechos.obtenerhechosDe(this));
  }
}
