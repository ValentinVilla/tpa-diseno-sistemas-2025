package ar.edu.utn.frba.dds.model.fuentes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.model.filtros.FiltroFecha;
import ar.edu.utn.frba.dds.model.filtros.FiltroTexto;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_fuente")
public abstract class Fuente {
  @Id
  @GeneratedValue
  protected Long id;

  public abstract ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros);

  public abstract List<Fuente> getFuente();

  protected ArrayList<Hecho> filtrarHechos(List<Hecho> hechos, ParametrosConsulta p) {
    FiltroFecha filtroFecha = new FiltroFecha(p.getFechaAcontecimientoDesde(), p.getFechaAcontecimientoHasta());
    FiltroCategoria filtroCategoria = new FiltroCategoria(p.getCategoria());

    List<Hecho> hechosPreFiltrados = null;
    if (p.getTexto() != null) {
      hechosPreFiltrados = this.filtrarBusquedaTexto(hechos, p.getTexto());
    } else {
      hechosPreFiltrados = hechos;
    }

    List<Hecho> hechosFiltrados = hechosPreFiltrados.stream()
        .filter(h -> p.getCategoria() == null || filtroCategoria.cumple(h))
        .filter(h -> p.getFechaAcontecimientoDesde() == null || filtroFecha.cumple(h)).toList();
    return new ArrayList<>(hechosFiltrados);
  }

  public List<Hecho> filtrarBusquedaTexto(List<Hecho> hechos, String texto) {
    FiltroTexto filtroTexto = new FiltroTexto(texto);
    return hechos.stream().filter(filtroTexto::cumple).toList();
  }
}