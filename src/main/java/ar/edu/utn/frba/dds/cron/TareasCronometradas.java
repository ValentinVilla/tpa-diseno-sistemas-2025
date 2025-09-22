package ar.edu.utn.frba.dds.cron;

import ar.edu.utn.frba.dds.consenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.consenso.ModoNavegacion;
import ar.edu.utn.frba.dds.dominio.Coleccion;
import ar.edu.utn.frba.dds.dominio.builders.ColeccionBuilder;
import ar.edu.utn.frba.dds.filtros.Filtro;
import ar.edu.utn.frba.dds.filtros.FiltroCategoria;
import ar.edu.utn.frba.dds.fuentes.Fuente;
import ar.edu.utn.frba.dds.fuentes.fuenteEstatica.FuenteEstatica;
import ar.edu.utn.frba.dds.fuentes.ServicioAgregacion;
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
        coleccion.ejecutarAlgoritmo(coleccion.mostrarHechos());
        System.out.println("Hechos curados.");
        break;

      default:
        System.out.println("falta argumentos.");
    }
  }


}
