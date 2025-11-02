package ar.edu.utn.frba.dds.cron;

import ar.edu.utn.frba.dds.model.consenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.model.consenso.ModoNavegacion;
import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.model.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.fuentes.fuenteEstatica.FuenteEstatica;
import ar.edu.utn.frba.dds.model.fuentes.ServicioAgregacion;
import java.util.ArrayList;
import java.util.List;

public class TareasCronometradas {
  public static void main(String[] args) {
    List<Fuente> fuentes = List.of(
        new FuenteEstatica("Hechos.csv", "Hechos", new ArrayList<>(List.of("id", "texto", "fecha"))),
        new FuenteEstatica("Hechos2.csv", "Hechos2", new ArrayList<>(List.of("id", "texto", "fecha")))
    );

    Filtro filtro = new FiltroCategoria("nombreDeLaCategoria");

    ServicioAgregacion servicioAgregacion = new ServicioAgregacion(fuentes);

    Coleccion coleccion = new ColeccionBuilder()
        .titulo("Mi Colección")
        .descripcion("Descripción de la colección")
        .fuente(servicioAgregacion)
        .criterio(filtro)
        .modoNavegacion(ModoNavegacion.CURADA)
        .algoritmoConsenso(AlgoritmoConsenso.ABSOLUTO)
        .build();

    if (args.length == 0) {

      return;
    }
    switch (args[0]) {
      case "actualizarCache":
        servicioAgregacion.actualizarCache();
        System.out.println("se actualizó el cache");
        break;
      case "curar hechos":
        coleccion.ejecutarAlgoritmo(coleccion.mostrarHechos(null));
        System.out.println("Hechos curados.");
        break;

      default:
        System.out.println("falta argumentos.");
    }
  }


}
