package ar.edu.utn.frba.dds.model.filtros;

import java.time.LocalDate;
import ar.edu.utn.frba.dds.model.dominio.Hecho;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FECHA")
public class FiltroFecha extends Filtro {

  private LocalDate fechaDesde;
  private LocalDate fechaHasta;

  public FiltroFecha(LocalDate fechaDesde, LocalDate fechaHasta) {
    this.fechaDesde = fechaDesde;
    this.fechaHasta = fechaHasta;
  }

  public FiltroFecha() {}

  @Override
  public boolean cumple(Hecho hecho) {
    LocalDate fechaHecho = LocalDate.from(hecho.getFechaHecho());

    if (fechaDesde != null && fechaHasta == null) {
      return !fechaHecho.isBefore(fechaDesde);
    }

    if (fechaDesde == null && fechaHasta != null) {
      return !fechaHecho.isAfter(fechaHasta);
    }

    if (fechaDesde != null) {
      return (!fechaHecho.isBefore(fechaDesde)) && (!fechaHecho.isAfter(fechaHasta));
    }

    return true;
  }

  public LocalDate getFechaDesde() { return fechaDesde; }
  public LocalDate getFechaHasta() { return fechaHasta; }

  @Override
  public String getDescripcion() {
    String desde = fechaDesde != null ? fechaDesde.toString() : "∞";
    String hasta = fechaHasta != null ? fechaHasta.toString() : "∞";
    return "Fecha entre " + desde + " y " + hasta;
  }
}
