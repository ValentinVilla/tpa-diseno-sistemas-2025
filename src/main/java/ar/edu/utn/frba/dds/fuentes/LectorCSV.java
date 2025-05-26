package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import com.opencsv.CSVReaderHeaderAware;

import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LectorCSV {

  public ArrayList<Hecho> leerDesde(String path, String categoria) {
    try {
      CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(path));

      Iterable<Map<String, String>> iterable = () -> new Iterator<>() {
        Map<String, String> next = leerSiguiente();

        private Map<String, String> leerSiguiente() {
          try {
            return reader.readMap();
          } catch (Exception e) {
            throw new RuntimeException("Error leyendo fila del CSV", e);
          }
        }

        @Override
        public boolean hasNext() {
          return next != null;
        }

        @Override
        public Map<String, String> next() {
          Map<String, String> actual = next;
          next = leerSiguiente();
          return actual;
        }
      };

      // Usamos un map para evitar duplicados por título
      Map<String, Hecho> hechosMap = new LinkedHashMap<>(); // mantiene orden de inserción

      for (Map<String, String> fila : iterable) {
        Hecho hecho = crearHechoDesdeMap(fila, categoria);
        if (!hecho.getTitulo().isEmpty()) {
          String clave = hecho.getTitulo().trim().toLowerCase();
          hechosMap.put(clave, hecho); // sobrescribe si ya existe
        }
      }

      return new ArrayList<>(hechosMap.values());

    } catch (Exception e) {
      throw new RuntimeException("Error leyendo archivo CSV en " + path, e);
    }
  }


  private Hecho crearHechoDesdeMap(Map<String, String> fila, String categoria) {
    String titulo = fila.getOrDefault("Título", "").trim();
    String descripcion = fila.getOrDefault("Descripción", "").trim();
    String latitud = fila.getOrDefault("Latitud", "").trim();
    String longitud = fila.getOrDefault("Longitud", "").trim();
    String fechaHecho = fila.getOrDefault("Fecha del hecho", "").trim();

    return new HechoBuilder()
        .titulo(titulo)
        .descripcion(descripcion)
        .categoria(categoria)
        .latitud(parsearDouble(latitud))
        .longitud(parsearDouble(longitud))
        .fechaAcontecimiento(parsearFecha(fechaHecho))
        .fechaCarga(LocalDate.now())
        .origen(Origen.CARGAMANUAL)
        .build();
  }

  private Double parsearDouble(String valor) {
    try {
      return Double.parseDouble(valor);
    } catch (Exception e) {
      return 0.0;
    }
  }

  private LocalDate parsearFecha(String valor) {
    try {
      return LocalDate.parse(valor);
    } catch (Exception e) {
      return null;
    }
  }
}

