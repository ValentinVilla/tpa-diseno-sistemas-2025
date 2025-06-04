package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;

import java.util.ArrayList;

public abstract class FuenteProxy implements Fuente {
  @Override
  public abstract ArrayList<Hecho> cargarHechos(ParametrosConsulta parametrosConsulta);
}