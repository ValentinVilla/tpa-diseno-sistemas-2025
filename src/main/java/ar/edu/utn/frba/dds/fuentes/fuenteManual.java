package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;

import java.util.ArrayList;

public class fuenteManual implements Fuente {
  private final ArrayList<Hecho> hechos = new ArrayList<>();
  
    public ArrayList<Hecho> cargarHechos(){
      return hechos;
    }
}
