package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;

import java.util.ArrayList;

public class fuenteManual implements Fuente {
  private final ArrayList<Hecho> hechos = new ArrayList<>();

  @Override
  public ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros) {
    return hechos;
  }

}
