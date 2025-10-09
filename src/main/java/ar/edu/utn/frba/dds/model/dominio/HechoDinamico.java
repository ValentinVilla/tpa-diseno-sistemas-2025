package ar.edu.utn.frba.dds.model.dominio;

import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class HechoDinamico extends Hecho {
  @ManyToOne
  private Contribuyente contribuyente;//en teoria no puede ser null pero esa validacion la debemos realizar a nivel dominio
  @Id
  private Long id;

  protected HechoDinamico() {}

  public HechoDinamico(HechoBuilder builder, Contribuyente contribuyente) {
    super(builder);
    if(contribuyente == null) {
      throw new IllegalArgumentException("El contribuyente no puede ser null");
    }
    this.contribuyente = contribuyente;
  }

  public boolean estaDentroDePlazo() {
    long diasDesdeCarga = ChronoUnit.DAYS.between(getFechaCarga(), LocalDateTime.now());
    return diasDesdeCarga <= 7;
  }

  public Contribuyente getContribuyente() {
    return contribuyente;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }
}
