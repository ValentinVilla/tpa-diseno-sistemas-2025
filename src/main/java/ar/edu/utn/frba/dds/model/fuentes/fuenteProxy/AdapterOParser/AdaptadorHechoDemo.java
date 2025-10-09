package ar.edu.utn.frba.dds.model.fuentes.fuenteProxy.AdapterOParser;

import ar.edu.utn.frba.dds.model.dominio.Hecho;
import ar.edu.utn.frba.dds.model.dominio.Origen;
import ar.edu.utn.frba.dds.model.dominio.builders.HechoBuilder;
import org.joda.time.DateTime;

import java.time.LocalDateTime;
import java.util.Map;

public class AdaptadorHechoDemo {
  public Hecho desdeMapa(Map<String, Object> dato) {
    return new HechoBuilder()
        .titulo((String) dato.getOrDefault("titulo", ""))
        .descripcion((String) dato.getOrDefault("descripcion", ""))
        .latitud((double) dato.getOrDefault("latitud", null))
        .longitud((double) dato.getOrDefault("longitud", null))
        .fechaAcontecimiento(
            dato.get("fecha") instanceof DateTime ? ((LocalDateTime) dato.get("fecha")) : null
        ).fechaCarga(LocalDateTime.now())
        .origen(Origen.SERVICIOEXTERNO)
        .build();
  }

  public AdaptadorHechoDemo() {
    // Constructor vacío
  }
}