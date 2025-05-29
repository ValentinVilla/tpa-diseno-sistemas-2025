package ar.edu.utn.frba.dds.dtos;

import java.util.HashMap;
import java.util.Map;

public class ParametrosConsulta {
  private final String categoria;
  private final String desde;
  private final String hasta;

  public ParametrosConsulta(String categoria, String desde, String hasta) {
    this.categoria = categoria;
    this.desde = desde;
    this.hasta = hasta;
  }

  public Map<String, String> comoMapa() {
    Map<String, String> map = new HashMap<>();
    if (categoria != null) map.put("categoria", categoria);
    if (desde != null) map.put("desde", desde);
    if (hasta != null) map.put("hasta", hasta);
    return map;
  }
}
