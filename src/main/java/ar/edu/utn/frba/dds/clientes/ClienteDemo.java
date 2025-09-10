package ar.edu.utn.frba.dds.clientes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.fuenteProxy.AdapterOParser.AdaptadorHechoDemo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class ClienteDemo {
  private final AdaptadorHechoDemo adaptador; // esto nc si va

  public ClienteDemo(AdaptadorHechoDemo adaptador) {
    this.adaptador = adaptador;
  }

  public Map<String, Object> siguienteHecho(String url, DateTime fechaUltimaConsulta) {
    Map<String, Object> hechoEjemplo = new HashMap<>();
    hechoEjemplo.put("titulo", "Incidente en la vía pública");
    hechoEjemplo.put("descripcion", "Se reportó un bache en la calle principal.");
    hechoEjemplo.put("fecha", new org.joda.time.DateTime(2024, 6, 10, 0, 0));
    hechoEjemplo.put("latitud", -34.6037);
    hechoEjemplo.put("longitud", -58.3816);
    return hechoEjemplo; // esto es un ejemplo, en teoria deberia ser un llamado a una API externa
    // que nos retorne un hecho
  }

  public List<Hecho> traerHechos(String urlBase) {
    List<Hecho> hechos = new ArrayList<>();
    Map<String, Object> objHecho = siguienteHecho(urlBase, DateTime.now().minusDays(1));
    while (objHecho != null) {
      hechos.add(adaptador.desdeMapa(objHecho));
      objHecho = siguienteHecho(urlBase, DateTime.now().minusDays(1));
    }
    return hechos;
  }

}