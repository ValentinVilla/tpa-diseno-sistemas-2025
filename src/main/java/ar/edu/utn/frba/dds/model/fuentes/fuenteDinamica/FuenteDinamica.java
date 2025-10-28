package ar.edu.utn.frba.dds.model.fuentes.fuenteDinamica;

import ar.edu.utn.frba.dds.model.DetectorSpam.ImplementadorSpam;
import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.servicios.HechoFTS;
import ar.edu.utn.frba.dds.model.solicitudes.Solicitud;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudModificacion;
import ar.edu.utn.frba.dds.model.solicitudes.SolicitudSubida;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;
import ar.edu.utn.frba.dds.repositorios.DAOHechos;
import ar.edu.utn.frba.dds.repositorios.RepositorioSolicitudes;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

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

    Solicitud soliciudSubida = new SolicitudSubida(hecho, "", new ImplementadorSpam(10), hecho.getContribuyente());

    RepositorioSolicitudes repoSolicitudes = RepositorioSolicitudes.getInstancia();
    repoSolicitudes.guardar(soliciudSubida);
  }

  public void solicitarModificarHecho(HechoDinamico hechoOriginal, HechoDinamico hechoNuevo, String textoArg, Contribuyente contribuyente) {
    if (!puedeModificar(hechoOriginal, hechoNuevo.getContribuyente())) {
      throw new RuntimeException("No tenés permiso para modificar este hecho.");
    } else {
      SolicitudModificacion solicitudModificacion = new SolicitudModificacion(hechoOriginal, textoArg, new ImplementadorSpam(15),hechoNuevo, contribuyente);
      RepositorioSolicitudes repoSolicitudes = RepositorioSolicitudes.getInstancia();
      repoSolicitudes.guardar(solicitudModificacion);
    }
  }

  public void solicitarEliminacionHecho(Hecho hecho, Contribuyente contribuyente){
    Solicitud soliciudEliminacion = new SolicitudEliminacion(hecho, "", new ImplementadorSpam(10), contribuyente);

    RepositorioSolicitudes repoSolicitudes = RepositorioSolicitudes.getInstancia();
    repoSolicitudes.guardar(soliciudEliminacion);
  }

  private boolean puedeModificar(HechoDinamico hecho, Contribuyente contribuyenteModificador) {
    return hecho.getContribuyente().getId().equals(contribuyenteModificador.getId())
        && contribuyenteModificador.getId()!=null
        && hecho.estaDentroDePlazo();
  }

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros){
    ArrayList<Hecho> hechosNoEliminados = new ArrayList<>();
    DAOHechos repo = DAOHechos.getInstancia();
    for (Hecho hecho : hechosDinamicos) {
      if (!repo.fueEliminado(hecho)) {
        hechosNoEliminados.add(hecho);
      }
    }
    return filtrarHechos(hechosNoEliminados, parametros);
  }

  @Override
  public List<Hecho> filtrarBusquedaTexto(List<Hecho> hechos, String titulo){
    HechoFTS fullTextSearch = new HechoFTS(DAOHechos.getInstancia());
    return fullTextSearch.buscarPorFuente(titulo, this.id);
  }

  public List<Fuente> getFuente(){
    return List.of(this);
  }
}

