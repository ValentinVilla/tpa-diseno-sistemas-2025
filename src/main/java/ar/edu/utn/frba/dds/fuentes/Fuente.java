package ar.edu.utn.frba.dds.fuentes;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;

public interface Fuente {
//    default ArrayList<Hecho> cargarHechos() {
//      return cargarHechos(new ParametrosConsulta());
//    } ESTO LO SAQUE PQ DIJO EL PROFE QUE USEMOS SIEMPRE LOS PARAMETROS DE CONSULTA Y CHAU.
    ArrayList<Hecho> cargarHechos(ParametrosConsulta parametros);

    List<Fuente> getFuente();
}

