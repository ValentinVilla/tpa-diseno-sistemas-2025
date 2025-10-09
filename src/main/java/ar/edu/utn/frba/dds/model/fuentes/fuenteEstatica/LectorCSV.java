package ar.edu.utn.frba.dds.model.fuentes.fuenteEstatica;

import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dominio.Origen;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import ar.edu.utn.frba.dds.repositorios.RepositorioHechos;
import com.opencsv.CSVReaderHeaderAware;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LectorCSV {

  public ArrayList<Hecho> leerDesde(String path, String categoria, ArrayList<String> campos) {
    try {
      Iterable<Map<String, String>> iterable = getMaps(path);

      Map<String, Hecho> hechosMap = new LinkedHashMap<>();

      for (Map<String, String> fila : iterable) {
        Hecho hecho = crearHechoDesdeMap(fila, categoria, campos);
        if (!hecho.getTitulo().isEmpty()) {
          String clave = hecho.getTitulo().trim().toLowerCase();
          hechosMap.put(clave, hecho);
          RepositorioHechos.getInstancia().guardar(hecho);
        }
      }

      return new ArrayList<>(hechosMap.values());

    } catch (Exception e) {
      throw new RuntimeException("Error leyendo archivo CSV en " + path, e);
    }
  }

  private static Iterable<Map<String, String>> getMaps(String path) throws IOException {
    CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(path));

    return () -> new Iterator<>() {
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
  }

  private Hecho crearHechoDesdeMap(Map<String, String> fila, String categoria, ArrayList<String> campos) {
    String titulo = fila.getOrDefault(campos.get(0), "").trim();
    String descripcion = fila.getOrDefault(campos.get(1), "").trim();
    String latitud = fila.getOrDefault(campos.get(2), "").trim();
    String longitud = fila.getOrDefault(campos.get(3), "").trim();
    String fechaHecho = fila.getOrDefault(campos.get(4), "").trim();

    return new HechoBuilder()
        .titulo(titulo)
        .descripcion(descripcion)
        .categoria(categoria)
        .latitud(parsearDouble(latitud))
        .longitud(parsearDouble(longitud))
        .fechaAcontecimiento(parsearFecha(fechaHecho))
        .fechaCarga(LocalDateTime.now())
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

  private LocalDateTime parsearFecha(String valor) {
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
      return LocalDateTime.parse(valor, formatter);
    } catch (Exception e) {
      return null;
    }
  }
}

