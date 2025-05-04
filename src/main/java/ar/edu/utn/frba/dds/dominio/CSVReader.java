package ar.edu.utn.frba.dds.dominio;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVReader {
  public void leerLote(String fuente, String categoria, Consumer<Hecho> procesador) {
    try {
      String path = "src/main/java/ar/edu/utn/frba/dds/files" + fuente;
      Scanner input = new Scanner(new File(path));

      //De acuerdo a la categoria hay que diferenciar como va leer

      Set<String> camposImportantes = Set.of("fecha", "municipio", "lat", "lng");
      Map<String, Integer> indices = new HashMap<>();

      String primeraLinea = input.nextLine();
      String[] header = primeraLinea.split(",");

      for (int i = 0; i < header.length; i++) {
        indices.put(header[i].trim().toLowerCase(), i);
      }

      while (input.hasNextLine()) {
        String linea = input.nextLine();
        Hecho hecho = prepararHecho(linea, categoria, indices, camposImportantes);
        procesador.accept(hecho); // Se procesa el hecho en el momento
      }

      input.close();

    } catch (Exception ex) {
      Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private Hecho prepararHecho(String registro, String categoria, Map<String, Integer> indices, Set<String> camposImportantes) {
    String[] campos = registro.split(",");

    String municipio = obtenerCampo(campos, indices.get("municipio"));
    String fecha = obtenerCampo(campos, indices.get("fecha"));
    String lat = obtenerCampo(campos, indices.get("lat"));
    String lng = obtenerCampo(campos, indices.get("lng"));

    String titulo = !municipio.isEmpty()
            ? "Incendio forestal en " + municipio
            : "Incendio forestal en coordenadas " + lat + ", " + lng;

    if (!fecha.isEmpty()) {
      titulo += " - " + fecha;
    }

    StringBuilder descripcion = new StringBuilder();
    for (String campo : indices.keySet()) {
      if (!camposImportantes.contains(campo)) {
        String valor = obtenerCampo(campos, indices.get(campo));
        if (!valor.isEmpty()) {
          descripcion.append(campo).append(": ").append(valor).append(", ");
        }
      }
    }

    double latParsed = 0.0;
    double lngParsed = 0.0;
    try {
      latParsed = !lat.isEmpty() ? Double.parseDouble(lat) : 0.0;
      lngParsed = !lng.isEmpty() ? Double.parseDouble(lng) : 0.0;
    } catch (NumberFormatException e) {
      //TODO: registrar algun error
    }

    LocalDate fechaHecho = null;
    try {
      fechaHecho = !fecha.isEmpty() ? LocalDate.parse(fecha) : null;
    } catch (Exception e) {
      //TODO: registrar algun error
    }

    return new HechoBuilder()
            .titulo(titulo)
            .descripcion(descripcion.toString())
            .categoria(categoria)
            .latitud(latParsed)
            .longitud(lngParsed)
            .fechaAcontecimiento(fechaHecho)
            .fechaCarga(LocalDate.now())
            .build();
  }

  private String obtenerCampo(String[] campos, Integer indice) {
    if (indice != null && indice >= 0 && indice < campos.length) {
      return campos[indice].trim();
    }
    return "";
  }
}
