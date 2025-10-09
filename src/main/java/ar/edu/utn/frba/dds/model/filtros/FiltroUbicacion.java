package ar.edu.utn.frba.dds.model.filtros;

import ar.edu.utn.frba.dds.model.dominio.Hecho;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("UBICACION")
public class FiltroUbicacion extends Filtro {
    private double latitudMin;
    private double latitudMax;
    private double longitudMin;
    private double longitudMax;

    public FiltroUbicacion() {}

    public FiltroUbicacion(double latitudMin, double latitudMax, double longitudMin, double longitudMax) {
      this.latitudMin = latitudMin;
      this.latitudMax = latitudMax;
      this.longitudMin = longitudMin;
      this.longitudMax = longitudMax;
    }

    @Override
    public boolean cumple(Hecho hecho) {
      return hecho.getLatitud() >= latitudMin && hecho.getLatitud() <= latitudMax &&
          hecho.getLongitud() >= longitudMin && hecho.getLongitud() <= longitudMax;
    }
}