package ar.edu.utn.frba.dds.fuentes;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;


public interface Fuente {
    ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros);

    List<Fuente> getFuente();
}

