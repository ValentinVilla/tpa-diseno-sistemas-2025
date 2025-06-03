package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import org.joda.time.DateTime;

import java.time.LocalDate;
import java.util.Map;

public class AdaptadorHechoDemo {
  public Hecho desdeMapa(Map<String, Object> dato) {
    return new HechoBuilder()
        .titulo((String) dato.getOrDefault("titulo", ""))
        .descripcion((String) dato.getOrDefault("descripcion", ""))
        .latitud((Double) dato.getOrDefault("latitud", null))
        .longitud((Double) dato.getOrDefault("longitud", null))
        .fechaAcontecimiento(
            dato.get("fecha") instanceof DateTime ? ((LocalDate) dato.get("fecha")) : null
        ).fechaCarga(LocalDate.now())
        .origen(Origen.SERVICIOEXTERNO)
        .build();
  }
}