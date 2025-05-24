package ar.edu.utn.frba.dds.fuentes;

import java.util.ArrayList;
import ar.edu.utn.frba.dds.dominio.Hecho;

public interface Fuente {
  ArrayList<Hecho> cargarHechos();
}

