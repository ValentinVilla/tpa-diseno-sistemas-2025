package ar.edu.utn.frba.dds.model.dominio;

import javax.persistence.*;

@Entity
public class Media {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "hecho_id", nullable = true)
  private HechoDinamico hecho;

  @Column(name = "path", nullable = false, length = 1024)
  private String path;

  protected Media() {} // JPA requirement -> rompe las bolas si no

  public Media(HechoDinamico hecho, String path) {
    this.hecho = hecho;
    this.path = path;
  }

  public void setHecho(HechoDinamico hecho) {
    this.hecho = hecho;
  }
}
