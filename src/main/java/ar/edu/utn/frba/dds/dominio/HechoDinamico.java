package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HechoDinamico extends Hecho {
  private Contribuyente contribuyente;

  public HechoDinamico(HechoBuilder builder, Contribuyente contribuyente) {
    super(builder);
    this.contribuyente = contribuyente;
  }

//  public void setIdContribuyenteCreador(int idContribuyenteCreador) {
//    this.idCreador = idContribuyenteCreador;
//  }
//
//  public int getIdContribuyenteCreador() {
//    return this.idCreador;
//  }



  public boolean estaDentroDePlazo() {
    long diasDesdeCarga = ChronoUnit.DAYS.between(getFechaCarga(), LocalDate.now());
    return diasDesdeCarga <= 7;
  }

  public Contribuyente getContribuyente(){
    return contribuyente;
  }

}
