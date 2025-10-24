package ar.edu.utn.frba.dds.model.servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class GeorefAPI {
  public static String getProvincia(double lat, double lon) throws Exception {
    String urlStr = String.format(
        Locale.US,
        "https://apis.datos.gob.ar/georef/api/ubicacion?lat=%f&lon=%f&aplanar=true&campos=provincia",
        lat, lon
    );

    URL url = new URL(urlStr);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");

    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
    String inputLine;
    StringBuilder response = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    JSONObject json = new JSONObject(response.toString());
    return json.getJSONObject("ubicacion")
        .getString("provincia_nombre");
  }
}