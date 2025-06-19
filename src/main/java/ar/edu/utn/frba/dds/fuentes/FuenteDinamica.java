package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import ar.edu.utn.frba.dds.solicitudes.SolicitudModificacion;

import java.util.ArrayList;

public class FuenteDinamica implements Fuente {
  private final RepositorioHechos repositorioHechos;

  public FuenteDinamica(RepositorioHechos repositorioHechos) {
    this.repositorioHechos = repositorioHechos;
  }

  public void subirHecho(HechoDinamico hecho) {
    subirHecho(-1, hecho);
  }

  public void subirHecho(int idContribuyenteCreador, HechoDinamico hecho) {
    hecho.setIdContribuyenteCreador(idContribuyenteCreador);
    repositorioHechos.guardar(this, hecho);
    //crear solicitud subida
  }

  public void modificarHecho(int idContribuyenteCreador, HechoDinamico hechoOriginal, HechoDinamico hechoNuevo) {
    if (!puedeModificar(idContribuyenteCreador, hechoOriginal)) {
      throw new RuntimeException("No tenés permiso para modificar este hecho.");
    } else {
      //si no puede modificar, lanza excepcion
      //crear nuevo hecho con fecha modificacion ahora
      //hechoOriginal.actualizarDesde(hechoNuevo);
      SolicitudModificacion solicitudModificacion = new SolicitudModificacion(hechoOriginal, hechoNuevo);
      //crea solicitud y le manda el id del hecho original
    }
  }

  public boolean puedeModificar(int idContribuyenteCreador, HechoDinamico hecho) {
    return hecho.getIdContribuyenteCreador() == (idContribuyenteCreador)
        && hecho.estaDentroDePlazo();
  }

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros) {
    return new ArrayList<>(repositorioHechos.obtenerhechosDe(this));
  }

}

