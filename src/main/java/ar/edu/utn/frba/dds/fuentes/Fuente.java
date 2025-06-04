package ar.edu.utn.frba.dds.fuentes;

import java.util.ArrayList;
import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;

public interface Fuente {
    default ArrayList<Hecho> cargarHechos() {
      return cargarHechos(new ParametrosConsulta());
    }
    ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros);
}

