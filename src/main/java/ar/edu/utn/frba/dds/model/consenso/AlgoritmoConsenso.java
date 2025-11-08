package ar.edu.utn.frba.dds.model.consenso;

import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;

import java.util.List;

public enum AlgoritmoConsenso {
    DEFAULT{
        @Override
        public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) {
            hecho.setConsensuado(true);
        }
    },

    ABSOLUTO{
        @Override
        public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) {
            boolean consenso = fuentes.stream().allMatch(fuente -> fuente.cargarHechos(null).contains(hecho));
            hecho.setConsensuado(consenso);
        }
    },

    MAYORIA_SIMPLE{
        @Override
        public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) {
            long coincidencias = fuentes.stream()
                    .flatMap(fuente -> fuente.cargarHechos(null).stream())
                    .filter(h -> h.esElMismo(hecho))
                    .count();

            boolean hayConsenso = coincidencias > (fuentes.size() / 2);

            hecho.setConsensuado(hayConsenso);
        }
    },
    MULTIPLES_MENCIONES{
        @Override
        public void tieneConsenso(Hecho hecho, List<Fuente> fuentes) {
            List<Hecho> coincidenciasTitulo = fuentes.stream()
                    .flatMap(fuente -> fuente.cargarHechos(null).stream())
                    .filter(alguno -> alguno.tieneMismoTitulo(hecho))
                    .toList();
            if (coincidenciasTitulo.size() <= 1) {
                hecho.setConsensuado(false);
            }
            if (coincidenciasTitulo.size() >= 2) {
                int cantCoinicidenciasTitulo = coincidenciasTitulo.size();
                int cantidadCoincidencias = coincidenciasTitulo.stream().filter(hecho::esElMismo).toList().size();
                hecho.setConsensuado(cantCoinicidenciasTitulo == cantidadCoincidencias);
            }
        }
    };

    public abstract void tieneConsenso(Hecho hecho, List<Fuente> fuentes);
}

