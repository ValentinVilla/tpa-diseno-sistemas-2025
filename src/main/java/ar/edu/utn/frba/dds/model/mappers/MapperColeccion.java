package ar.edu.utn.frba.dds.model.mappers;

import ar.edu.utn.frba.dds.model.consenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.model.consenso.ModoNavegacion;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.model.filtros.FiltroFecha;

public class MapperColeccion {



    public ModoNavegacion toNavegacion(String modoNavegacion) {
        return switch (modoNavegacion.trim()) {
            case "curada" -> ModoNavegacion.CURADA;
            default -> ModoNavegacion.IRRESTRICTA;
        };
    }

    public AlgoritmoConsenso toAlgoritmoConsenso(String algoritmoConsenso) {
        return switch (algoritmoConsenso.trim()) {
            case "ABSOLUTO" -> AlgoritmoConsenso.ABSOLUTO;
            case "MAYORIA_SIMPLE" -> AlgoritmoConsenso.MAYORIA_SIMPLE;
            case "MULTIPLES_MENCIONES" -> AlgoritmoConsenso.MULTIPLES_MENCIONES;
            default -> AlgoritmoConsenso.DEFAULT;
        };
    }

    public Filtro toCriterio(String criterio){
        if (criterio == null || criterio.trim().isEmpty()) {
            return null; // evitar crear un filtro por defecto que muestre "contiene texto"
        }
        return switch (criterio.trim()) {
            case "incendios" -> new FiltroCategoria("Incendio");
            case "inundaciones" -> new FiltroCategoria("Inundación");
            case "filtro_geografico", "filtro_categoria" -> new FiltroCategoria(null); // si realmente se quiere un filtro vacío, conservar aquí
            case "filtro_fecha" -> new FiltroFecha(null, null);
            default -> null; // desconocido -> null en lugar de crear FiltroCategoria(null)
        };
    }
}
