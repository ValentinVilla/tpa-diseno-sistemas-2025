package ar.edu.utn.frba.dds.clientes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.fuentes.AdaptadorHechoDemo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

public class ClienteDemo {
  private final AdaptadorHechoDemo adaptador; //esto nc si va

  public ClienteDemo() {
    this.adaptador = new AdaptadorHechoDemo();
  }

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
  public Map<String, Object> siguienteHecho(String url, DateTime fechaUltimaConsulta) {
    // Simula la obtención de un hecho desde una fuente externa.
    // En un caso real, aquí se haría una llamada a una API o base de datos.
    //Map<String, Object> datos = obtenerDatosDesdeFuente(url, fechaUltimaConsulta);

    //if (datos == null) {
    //  return null; // No hay más hechos disponibles
    //}

    //Hecho hecho = adaptador.desdeMapa(datos);
   // return hecho.toMap(); // Convertir el hecho a un mapa para devolverlo
  }
}

//tomamos que esta clase conexion es una api externa que nos va a brindar los datos de los hechos, nosotros no tenemos que implementar lo que hace este metodo siguiente hecho, sino como nuestro sistema va a manejar la info que le da esta api externa, verdad?
