package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
public class HechoDinamico extends Hecho {
  @ManyToOne
  @JoinColumn(name = "contribuyente_id")
  private Contribuyente contribuyente;

  protected HechoDinamico() {}

  public HechoDinamico(HechoBuilder builder, Contribuyente contribuyente) {
    super(builder);
    this.contribuyente = contribuyente;
  }

  public boolean estaDentroDePlazo() {
    long diasDesdeCarga = ChronoUnit.DAYS.between(getFechaCarga(), LocalDate.now());
    return diasDesdeCarga <= 7;
  }

  public Contribuyente getContribuyente() {
    return contribuyente;
  }
}
