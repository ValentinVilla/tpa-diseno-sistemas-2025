package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;

import java.util.ArrayList;

public class fuenteManual extends FuenteDinamica {
  private final ArrayList<Hecho> hechos = new ArrayList<>();

    @Override
    public ArrayList<Hecho> cargarHechos(){
      return hechos;
    }
}
