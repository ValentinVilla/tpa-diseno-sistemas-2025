package ar.edu.utn.frba.dds.servicios;

import java.net.http.*;
import java.net.URI;
import org.json.JSONObject;

public class GeocodingService {

  private static final HttpClient client = HttpClient.newHttpClient();

  public String obtenerProvincia(double lat, double lon) throws Exception {
    String url = String.format(
        "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=10&addressdetails=1",
        lat, lon
    );

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("User-Agent", "ProyectoAnualDisenioDeSistemas (disenosistemas7@gmail.com)")
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    JSONObject json = new JSONObject(response.body());
    JSONObject address = json.getJSONObject("address");

    String provincia = address.optString(
        "province",
        address.optString("state", address.optString("city", "Desconocido"))
    );

    System.out.println("Provincia obtenida de Nominatim: " + provincia); // log para probar
    return provincia;
  }
}


