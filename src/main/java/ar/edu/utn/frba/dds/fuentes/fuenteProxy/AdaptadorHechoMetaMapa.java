package ar.edu.utn.frba.dds.fuentes.fuenteProxy;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import org.json.JSONObject;

import java.time.LocalDate;

public class AdaptadorHechoMetaMapa {
  public Hecho desdeJson(JSONObject obj) {
    return new HechoBuilder()
        .titulo(obj.optString("titulo", ""))
        .descripcion(obj.optString("descripcion", ""))
        .latitud(obj.optDouble("latitud", 0.0))
        .longitud(obj.optDouble("longitud", 0.0))
        .categoria(obj.optString("categoria", "otros"))
        .fechaAcontecimiento(parsearFecha(obj.optString("fechaHecho")))
        .fechaCarga(LocalDate.now())
        .origen(Origen.SERVICIOEXTERNO)
        .build();
  }

  private LocalDate parsearFecha(String texto) {
    try {
      return LocalDate.parse(texto);
    } catch (Exception e) {
      return null;
    }
  }
}

