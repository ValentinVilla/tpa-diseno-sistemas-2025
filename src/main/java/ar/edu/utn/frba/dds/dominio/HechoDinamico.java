package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.usuarios.Contribuyente;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@DiscriminatorValue("DINAMICO")
public class HechoDinamico extends Hecho {
  @ManyToOne
  private Contribuyente contribuyente;//en teoria no puede ser null pero esa validacion la debemos realizar a nivel dominio

  protected HechoDinamico() {}

  public HechoDinamico(HechoBuilder builder, Contribuyente contribuyente) {
    super(builder);
    if(contribuyente == null) {
      throw new IllegalArgumentException("El contribuyente no puede ser null");
    }
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
