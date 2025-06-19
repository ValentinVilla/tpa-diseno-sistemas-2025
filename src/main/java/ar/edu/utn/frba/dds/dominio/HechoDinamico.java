package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HechoDinamico extends Hecho {
  private int idCreador;

  public HechoDinamico(HechoBuilder builder) {
    super(builder);
  }

  public void setIdContribuyenteCreador(int idContribuyenteCreador) {
    this.idCreador = idContribuyenteCreador;
  }

  public int getIdContribuyenteCreador() {
    return this.idCreador;
  }

  public boolean getVisible(){
    return visible;
  }

  public boolean estaDentroDePlazo() {
    long diasDesdeCarga = ChronoUnit.DAYS.between(getFechaCarga(), LocalDate.now());
    return diasDesdeCarga > 7;
    }

}
