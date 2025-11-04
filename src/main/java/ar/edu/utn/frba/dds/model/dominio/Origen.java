package ar.edu.utn.frba.dds.model.dominio;

public enum Origen {
  CARGAMANUAL("Carga manual"),
  DATASET("Dataset"),
  CONTRIBUYENTE("Contribuyente"),
  SERVICIOEXTERNO("Servicio externo");

  private final String label;

  Origen(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return label;
  }
}