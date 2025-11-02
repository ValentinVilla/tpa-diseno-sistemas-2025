package ar.edu.utn.frba.dds.model.dominio;


import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.model.servicios.GeorefAPI;
import ar.edu.utn.frba.dds.model.usuarios.Contribuyente;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
public class HechoDinamico extends Hecho {
  @ManyToOne(optional = true)
  @JoinColumn(name = "contribuyente_id", nullable = true) //permito los null para poder guardar hechos anonimos
  private Contribuyente contribuyente;

  private boolean visible = false;

  private LocalDate fechaModificacion;

  @OneToMany(mappedBy = "hecho", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Media> medias = new ArrayList<>();

  protected HechoDinamico() {
  }

  public HechoDinamico(HechoBuilder builder, Contribuyente contribuyente) {
    super(builder);
    // pues ahora el contribuyente puede ser null (no logueado)
    this.contribuyente = contribuyente;
  }

  public boolean estaDentroDePlazo() {
    long diasDesdeCarga = ChronoUnit.DAYS.between(getFechaCarga(), LocalDateTime.now());
    return diasDesdeCarga <= 7;
  }

  public Contribuyente getContribuyente() {
    return contribuyente;
  }

  public boolean esAnonimo() {
    return this.contribuyente == null;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public boolean getVisible() {
    return visible;
  }

  public void revisarubicacion() {
    if (this.getLatitud() != null && this.getLongitud() != null) {
      try {
        String provincia = GeorefAPI.getProvincia(
            this.getLatitud(), this.getLongitud()
        );
        this.setProvincia(provincia);
      } catch (Exception e) {
        System.err.println("Error al obtener provincia: " + e.getMessage());
        this.setProvincia("Desconocida");
      }
    }
  }

  public void addMedia(String path) {
    Media media = new Media(this, path);
    media.setHecho(this);
    this.medias.add(media);
  }

  public void removeMedia(Media media) {
    this.medias.remove(media);
    media.setHecho(null);
  }
}