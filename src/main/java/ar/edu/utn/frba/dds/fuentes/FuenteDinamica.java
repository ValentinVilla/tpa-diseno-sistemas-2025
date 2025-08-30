package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.DetectorSpam.ImplementadorSpam;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.solicitudes.SolicitudModificacion;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;

import java.util.ArrayList;
import java.util.List;

public class FuenteDinamica extends Fuente {
  private final ArrayList<HechoDinamico> hechosDinamicos = new ArrayList<>();

  public FuenteDinamica() {
  }

  public void subirHecho(HechoDinamico hecho) {
    hechosDinamicos.add(hecho);
  }

  public void eliminarHecho(HechoDinamico hecho) {
    hecho.setVisible(false);
  }

  public void solicitarModificarHecho(HechoDinamico hechoOriginal, HechoDinamico hechoNuevo, String textoArg) {
    if (!puedeModificar(hechoOriginal, hechoNuevo.getContribuyente())) {
      throw new RuntimeException("No tenés permiso para modificar este hecho.");
    } else {
      SolicitudModificacion solicitudModificacion = new SolicitudModificacion(hechoOriginal, textoArg, new ImplementadorSpam(15),hechoNuevo);
    }
  }

  public boolean puedeModificar(HechoDinamico hecho, Contribuyente contribuyenteModificador) {
    return hecho.getContribuyente().getId().equals(contribuyenteModificador.getId())
        && contribuyenteModificador.getId()!=null
        && hecho.estaDentroDePlazo();
  }

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros){
    ArrayList<Hecho> visibles = new ArrayList<>();
    for (Hecho hecho : hechosDinamicos) {
      if (hecho.getVisible()) {
        visibles.add(hecho);
      }
    }
    return visibles;
  }

  public List<Fuente> getFuente(){
    return List.of(this);
  }
}

