package ar.edu.utn.frba.dds.model.fuentes.fuenteProxy.AdapterOParser;

import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dominio.Origen;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import org.json.JSONObject;

import java.time.LocalDateTime;

public class AdaptadorHechoMetaMapa {
  public Hecho desdeJson(JSONObject obj) {
    return new HechoBuilder()
        .titulo(obj.optString("titulo", ""))
        .descripcion(obj.optString("descripcion", ""))
        .latitud(obj.optDouble("latitud", 0.0))
        .longitud(obj.optDouble("longitud", 0.0))
        .categoria(obj.optString("categoria", "otros"))
        .fechaAcontecimiento(parsearFecha(obj.optString("fechaHecho")))
        .fechaCarga(LocalDateTime.now())
        .origen(Origen.SERVICIOEXTERNO)
        .build();
  }

  private LocalDateTime parsearFecha(String texto) {
    try {
      return LocalDateTime.parse(texto);
    } catch (Exception e) {
      return null;
    }
  }
}

