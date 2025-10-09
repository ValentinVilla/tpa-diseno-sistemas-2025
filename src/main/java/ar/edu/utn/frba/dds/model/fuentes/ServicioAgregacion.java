package ar.edu.utn.frba.dds.model.fuentes;

import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("SERVICIO_AGREGACION")
public class ServicioAgregacion extends Fuente{
  @ManyToMany
  @JoinTable(
      name = "servicio_fuentes",
      joinColumns = @JoinColumn(name = "servicio_id"),
      inverseJoinColumns = @JoinColumn(name = "fuente_id")
  )
  private List<Fuente> fuentesQueConsidera;
  @Transient
  private final ArrayList<Hecho> cacheDeHechos = new ArrayList<>();

  public ServicioAgregacion() {}

  public void actualizarCache() {
    //esta es la funcion para vincular al crontab
    cacheDeHechos.clear();
    fuentesQueConsidera.forEach(fuente -> {
      ultimosNHechos(10,fuente.cargarHechos(null)).addAll(cacheDeHechos);
    });
  }

  private List<Hecho> ultimosNHechos(int n, List<Hecho> listaCompleta){
    int size = listaCompleta.size();
    return listaCompleta.subList(Math.max(size - n, 0), size);
  }

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros) {
    return cacheDeHechos;
    //cumple criterios de coleccion
  }

  public List<Fuente> getFuente(){
    return fuentesQueConsidera;
  }

  public ServicioAgregacion(List<Fuente> fuentesQueConsidera) {
    this.fuentesQueConsidera = fuentesQueConsidera;
  }
}
