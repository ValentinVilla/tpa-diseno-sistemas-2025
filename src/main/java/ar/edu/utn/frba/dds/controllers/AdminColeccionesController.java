package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.model.dominio.Coleccion;
import ar.edu.utn.frba.dds.model.fuentes.Fuente;
import ar.edu.utn.frba.dds.model.filtros.Filtro;
import ar.edu.utn.frba.dds.repositorios.RepositorioColecciones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminColeccionesController {

  public Map<String, Object> mostrarColecciones() {
    List<Coleccion> colecciones = RepositorioColecciones.getInstancia().listarTodas();

    List<Map<String, Object>> coleccionesVM = new ArrayList<>();
    for (Coleccion c : colecciones) {
      Map<String, Object> m = new HashMap<>();
      m.put("id", c.getId());
      m.put("titulo", c.getTitulo());
      m.put("descripcion", c.getDescripcion());

      String fuenteStr = "-";
      String fuenteClass = "-";
      try {
        Fuente f = c.getFuente();
        if (f != null) {
          String tipo = f.getClass().getSimpleName();
          String nombre = null;
          try { nombre = f.getNombre(); } catch (Exception ignored) {}
          fuenteStr = nombre != null && !nombre.isBlank() ? tipo + " - " + nombre : tipo;
          fuenteClass = tipo; // guardamos el simple nombre de la clase para filtrado
        }
      } catch (Exception e) {
        fuenteStr = "-";
        fuenteClass = "-";
      }
      m.put("fuente", fuenteStr);
      m.put("fuenteClass", fuenteClass);

      Filtro filtro = c.getCriterio();
      String criterioStr = (filtro != null) ? filtro.getDescripcion() : "-";
      m.put("criterioPertenencia", criterioStr);


      // Enums
      String modoStr = c.getModoNavegacion() != null ? c.getModoNavegacion().name() : "-";
      String algoritmoStr = c.getAlgoritmoConsenso() != null ? c.getAlgoritmoConsenso().name() : "-";
      m.put("modoNavegacion", modoStr);
      m.put("algoritmoConsenso", algoritmoStr);

      coleccionesVM.add(m);
    }

    Map<String, Object> model = new HashMap<>();
    model.put("colecciones", coleccionesVM);
    return model;
  }
}
