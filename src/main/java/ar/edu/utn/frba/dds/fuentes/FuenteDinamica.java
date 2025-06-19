package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.DetectorSpam.ImplementadorSpam;
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
    repositorioHechos.guardar(this, hecho); //repostiro fuentes?? y seria guardar en la fuente
    //subirHecho(-1, hecho);
  }

//  public void subirHecho(int idContribuyenteCreador, HechoDinamico hecho) {
//    hecho.setIdContribuyenteCreador(idContribuyenteCreador);
//    repositorioHechos.guardar(this, hecho); //repostiro fuentes?? y seria guardar en la fuente
//    //crear solicitud subida
//  }

  public void solicitarModificarHecho(int idContribuyenteCreador, HechoDinamico hechoOriginal, HechoDinamico hechoNuevo, String textoArg) {
    if (!puedeModificar(idContribuyenteCreador, hechoOriginal)) {
      throw new RuntimeException("No tenés permiso para modificar este hecho.");
    } else {
      //si no puede modificar, lanza excepcion
      //crear nuevo hecho con fecha modificacion ahora
      //hechoOriginal.actualizarDesde(hechoNuevo);
      SolicitudModificacion solicitudModificacion = new SolicitudModificacion(hechoOriginal, textoArg, new ImplementadorSpam(15),hechoNuevo);
      //crea solicitud y esa solicitud espera a ser aceptada por un administrador, una vez que se acepta la solicitud se pone en visible el nuevo y se pone en no visible el anterior
    }
  }

  public boolean puedeModificar(int idContribuyenteCreador, HechoDinamico hecho) {
    return hecho.getContribuyente().getId() == (idContribuyenteCreador)
        && hecho.estaDentroDePlazo();
  }

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros) {
    return new ArrayList<>(repositorioHechos.obtenerhechosDe(this));
  }

}

