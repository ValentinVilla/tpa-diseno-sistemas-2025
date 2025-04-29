package ar.edu.utn.frba.dds.dominio;

public enum CriterioPertenencia {
  INCENDIOFORESTAL("INCENDIOFORESTAL"),
  INUNDACION("INUNDACION"),
  TERREMOTO("TERREMOTO");

  CriterioPertenencia(String nombreCriterio) {
    this.nombreCriterio = nombreCriterio;
  }

  public String getCriterio() {
    return nombreCriterio;
  }

  final String nombreCriterio;
}
