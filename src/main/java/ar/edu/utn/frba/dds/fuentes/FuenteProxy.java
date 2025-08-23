package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;

import java.util.ArrayList;

public interface FuenteProxy extends Fuente {
  @Override
  ArrayList<Hecho> cargarHechos(ParametrosConsulta parametrosConsulta);
}
