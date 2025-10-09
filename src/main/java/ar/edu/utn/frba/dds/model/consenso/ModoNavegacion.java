package ar.edu.utn.frba.dds.model.consenso;

import ar.edu.utn.frba.dds.model.dominio.Hecho;

import java.util.List;

public enum ModoNavegacion {
  IRRESTRICTA {
    @Override
    public List<Hecho> mostrarHechos(List<Hecho> listaOriginal) {
      return listaOriginal;
    }
  },
  CURADA {
    @Override
    public List<Hecho> mostrarHechos(List<Hecho> listaOriginal) {
      return listaOriginal.stream()
          .filter(Hecho::estaConsensuado)
          .toList();
    }
  };

  public abstract List<Hecho> mostrarHechos(List<Hecho> listaOriginal);
}
