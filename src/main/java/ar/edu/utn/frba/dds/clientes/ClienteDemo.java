package ar.edu.utn.frba.dds.clientes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.fuentes.AdaptadorHechoDemo;
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
    // ESTO SERIA LO QUE RETORNA:
    /*
    Devuelve un mapa con los atributos de un hecho, indexados por nombre de
     atributo. Si el metodo retorna null, significa que no hay nuevos hechos
    por ahora. La fecha es opcional

    siguienteHecho(...), devuelve esto:
    * "titulo": "Incidente en la vía pública",
    * "descripcion": "Se reportó un bache en la calle principal.",
    * "fecha": new org.joda.time.DateTime(2024, 6, 10, 0, 0),
    * "latitud": -34.6037,
    * "longitud": -58.3816
    */
    Map<String, Object> hechoEjemplo = new HashMap<>();
    hechoEjemplo.put("titulo", "Incidente en la vía pública");
    hechoEjemplo.put("descripcion", "Se reportó un bache en la calle principal.");
    hechoEjemplo.put("fecha", new org.joda.time.DateTime(2024, 6, 10, 0, 0));
    hechoEjemplo.put("latitud", -34.6037);
    hechoEjemplo.put("longitud", -58.3816);
    return hechoEjemplo; // esto es un ejemplo, en teoria deberia ser un llamado a una API externa
    // que nos retorne un hecho
  } // EN TEORIA ESTO LO DEBE PROVEER UNA BIBLIOTECA O API EXTERNA

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

// tomamos que esta clase conexion es una api externa que nos va a brindar los datos de los hechos,
// nosotros no tenemos que implementar lo que hace este metodo siguiente hecho, sino como nuestro
// sistema va a manejar la info que le da esta api externa, verdad?
