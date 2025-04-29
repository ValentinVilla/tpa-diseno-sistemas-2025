package ar.edu.utn.frba.dds.dominio;

import java.util.ArrayList;
import java.util.List;

public class HechosAlmacenados {
  private static final HechosAlmacenados INSTANCE = new HechosAlmacenados();

  private List<Hecho> hechosAlmacenados;

  private HechosAlmacenados() {
    this.hechosAlmacenados = new ArrayList<>();
  }

  public static HechosAlmacenados instance() {
    return INSTANCE;
  }

  public List<Hecho> getHechosAlmacenados() {return hechosAlmacenados;}

  public static void agregarHecho(Hecho hecho) {
    HechosAlmacenados.instance().hechosAlmacenados.add(hecho);
  }
}
