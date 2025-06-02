package ar.edu.utn.frba.dds.fuentes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dominio.Origen;
import ar.edu.utn.frba.dds.dominio.builders.HechoBuilder;
import org.joda.time.DateTime;

import java.time.LocalDate;
import java.util.Map;

public class AdaptadorHechoDemo {
  public Hecho desdeMapa(Map<String, Object> datos) {
    return new HechoBuilder()
        .titulo((String) datos.getOrDefault("titulo", ""))
        .descripcion((String) datos.getOrDefault("descripcion", ""))
        .latitud((Double) datos.getOrDefault("latitud", null))
        .longitud((Double) datos.getOrDefault("longitud", null))
        .fechaAcontecimiento(
            datos.get("fecha") instanceof DateTime ? ((LocalDate) datos.get("fecha")) : null
        ).fechaCarga(LocalDate.now())
        .origen(Origen.SERVICIOEXTERNO)
        .build();
  }
}