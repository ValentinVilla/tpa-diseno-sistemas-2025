package ar.edu.utn.frba.dds.dominio;

public enum Origen {
  CARGAMANUAL("cargaManual"),
  DATASET("dataSet"),
  CONTRIBUYENTE("contribuyente");

  Origen(String origen) {
    this.origen = origen;
  }

  public String getOrigen() {
    return origen;
  }
  String origen;
}
