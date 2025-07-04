package ar.edu.utn.frba.dds.clientes;

import ar.edu.utn.frba.dds.dominio.Hecho;
import ar.edu.utn.frba.dds.dtos.ParametrosConsulta;
import ar.edu.utn.frba.dds.fuentes.AdaptadorHechoMetaMapa;
import ar.edu.utn.frba.dds.solicitudes.SolicitudEliminacion;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClienteMetaMapa {
  private final AdaptadorHechoMetaMapa adaptador;

  public ClienteMetaMapa() {
    this.adaptador = new AdaptadorHechoMetaMapa();
  }

  public List<Hecho> obtenerHechos(String urlBase, ParametrosConsulta parametros) {
    String urlConParametros = construirUrl(urlBase, parametros);
    return ejecutarConsultaGET(urlConParametros);
  }

  public List<Hecho> obtenerHechosColeccion(String urlBase, String idColeccion, ParametrosConsulta parametros) {
    String endpoint = urlBase + "/colecciones/" + idColeccion + "/hechos";
    String urlConParametros = construirUrl(endpoint, parametros);
    return ejecutarConsultaGET(urlConParametros);
  }

  public void enviarSolicitudEliminacion(String urlBase, SolicitudEliminacion solicitud) {
    try {
      String endpoint = urlBase + "/solicitudes";
      HttpURLConnection conn = getHttpURLConnection(solicitud, endpoint);

      int responseCode = conn.getResponseCode();
      if (responseCode != 200 && responseCode != 201) {
        throw new RuntimeException("Error al enviar solicitud: HTTP " + responseCode);
      }
      conn.disconnect();
    } catch (Exception e) {
      throw new RuntimeException("Error al enviar solicitud de eliminación a MetaMapa", e);
    }
  }

  private static HttpURLConnection getHttpURLConnection(SolicitudEliminacion solicitud, String endpoint) throws IOException {
    HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
    conn.setRequestMethod("POST");
    conn.setDoOutput(true);
    conn.setRequestProperty("Content-Type", "application/json");

    JSONObject json = new JSONObject();
    json.put("idHecho", solicitud.getHecho().getId());
    json.put("fundamento", solicitud.getTextoFundamentacion());

    try (OutputStream os = conn.getOutputStream()) {
      byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
      os.write(input, 0, input.length);
    }
    return conn;
  }

  private List<Hecho> ejecutarConsultaGET(String urlConParametros) {
    List<Hecho> hechos = new ArrayList<>();
    try {
      HttpURLConnection conn = (HttpURLConnection) new URL(urlConParametros).openConnection();
      conn.setRequestMethod("GET");

      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Error HTTP: " + conn.getResponseCode());
      }

      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder respuesta = new StringBuilder();
      String linea;
      while ((linea = reader.readLine()) != null) {
        respuesta.append(linea);
      }

      JSONArray array = new JSONArray(respuesta.toString());
      for (int i = 0; i < array.length(); i++) {
        hechos.add(adaptador.desdeJson(array.getJSONObject(i)));
      }

      reader.close();
      conn.disconnect();
    } catch (Exception e) {
      throw new RuntimeException("Error al obtener hechos desde MetaMapa", e);
    }
    return hechos;
  }

  private String construirUrl(String base, ParametrosConsulta params) {
    StringBuilder url = new StringBuilder(base);
    Map<String, String> mapa = params.comoMapa();
    if (!mapa.isEmpty()) {
      url.append("?");
      url.append(mapa.entrySet().stream()
          .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
          .collect(Collectors.joining("&")));
    }
    return url.toString();
  }
}
