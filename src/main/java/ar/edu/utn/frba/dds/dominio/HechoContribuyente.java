package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HechoContribuyente extends Hecho{
  private int idCreador;

  public HechoContribuyente(HechoBuilder builder,  int idCreador) {
    super(builder);
    this.idCreador = idCreador;
  }

  public void setIdContribuyenteCreador(int idContribuyenteCreador) {
    this.idCreador = idContribuyenteCreador;
  }

  public int getIdContribuyenteCreador() {
    return this.idCreador;
  }

  public boolean estaDentroDePlazo() {
    long diasDesdeCarga = ChronoUnit.DAYS.between(getFechaCarga(), LocalDate.now());
    if (diasDesdeCarga > 7) {
      return false;
    }
    else
      return true;
  }

}
