package ar.edu.utn.frba.dds.repositorios;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.HechoDinamico;
import ar.edu.utn.frba.dds.dominio.HechoEliminado;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioHechosEliminados {
    private static RepositorioHechosEliminados instancia;
    private List<HechoEliminado> hechosEliminados;


    public void agregarHechoEliminado(HechoEliminado hecho) {
        hechosEliminados.add(hecho);
    }

    public ArrayList<Hecho> losQueNoFueronEliminados(ArrayList<Hecho> hechos) {
        return hechos.stream()
            .filter(hecho -> hechosEliminados.stream()
                .noneMatch(hechoEliminado -> hechoEliminado.correspondeA(hecho)))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<HechoDinamico> losQueNoFueronEliminadosDinamicos(ArrayList<HechoDinamico> hechos) {
        return hechos.stream()
            .filter(hecho -> hechosEliminados.stream()
                .noneMatch(hechoEliminado -> hechoEliminado.correspondeA(hecho)))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public static RepositorioHechosEliminados getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioHechosEliminados();
        }
        return instancia;
    }

    RepositorioHechosEliminados() {
        this.hechosEliminados = List.of();
    }
}//TODO: QUE TODAS LAS FUENTES ANTES DE CARGAR HECHOS FILTREN POR LOS QUE NO ESTAN ELIMINADOS (USAR EL REPO DE HECHOS ELIMINADOS)
