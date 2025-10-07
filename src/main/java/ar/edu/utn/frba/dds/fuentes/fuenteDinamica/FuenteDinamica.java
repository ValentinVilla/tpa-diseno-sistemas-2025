package ar.edu.utn.frba.dds.fuentes.fuenteDinamica;

import ar.edu.utn.frba.dds.DetectorSpam.ImplementadorSpam;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechosEliminados;
import ar.edu.utn.frba.dds.solicitudes.SolicitudModificacion;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("FUENTE_DINAMICA")
public class FuenteDinamica extends Fuente {

  // Esto es para poder asociar la fk en HechoDinamico sin pasarle una fuente en el constructor
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "fuente_id") // esto crea la FK en la tabla de hechos
  private List<HechoDinamico> hechosDinamicos = new ArrayList<>();

  public FuenteDinamica() {}

  public void subirHecho(HechoDinamico hecho) {
    hecho.revisarubicacion();
    hechosDinamicos.add(hecho);
    //aca habria que crear la solicitud de subida
    //asumo que el usuario ya viene asociado al hecho
  }

/*
//  public void subirHecho(int idContribuyenteCreador, HechoDinamico hecho) {
//    hecho.setIdContribuyenteCreador(idContribuyenteCreador);
//    repositorioHechos.guardar(this, hecho); //repostiro fuentes?? y seria guardar en la fuente
//    //crear solicitud subida
//  }
*/

  public void solicitarModificarHecho(HechoDinamico hechoOriginal, HechoDinamico hechoNuevo, String textoArg) {
    //el contribuyente necesitaba un texto argumentativo??
    if (!puedeModificar(hechoOriginal, hechoNuevo.getContribuyente())) {
      throw new RuntimeException("No tenés permiso para modificar este hecho.");
    } else {
      SolicitudModificacion solicitudModificacion = new SolicitudModificacion(hechoOriginal, textoArg, new ImplementadorSpam(15),hechoNuevo);
      //crea solicitud y esa solicitud espera a ser aceptada por un administrador, una vez que se acepta la solicitud se pone en visible el nuevo y se pone en no visible el anterior
    }
  }

  public boolean puedeModificar(HechoDinamico hecho, Contribuyente contribuyenteModificador) {
    return hecho.getContribuyente().getId().equals(contribuyenteModificador.getId())
        && contribuyenteModificador.getId()!=null
        && hecho.estaDentroDePlazo();
  }

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros){
    ArrayList<Hecho> hechosNoEliminados = new ArrayList<>();
    RepositorioHechosEliminados repo = RepositorioHechosEliminados.getInstancia();
    for (Hecho hecho : hechosDinamicos) {
      if (!repo.fueEliminado(hecho)) {
        hechosNoEliminados.add(hecho);
      }
    }
    return hechosNoEliminados;
  }

  public List<Fuente> getFuente(){
    return List.of(this);
  }
}

