package ar.edu.utn.frba.dds.model.DetectorSpam;

public class ImplementadorSpam implements DetectorDeSpam {
  Integer idSpam;

    @Override
    public boolean esSpam(String texto) {
      return false;
    }

    public ImplementadorSpam(Integer idSpam) {
      this.idSpam = idSpam;// Constructor vacío
    }
}
