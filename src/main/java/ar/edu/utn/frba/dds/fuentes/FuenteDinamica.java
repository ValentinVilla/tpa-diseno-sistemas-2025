package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;

import java.util.ArrayList;

public abstract class FuenteDinamica implements Fuente {
  public abstract ArrayList<Hecho> cargarHechos();
}
