package ar.edu.utn.frba.dds.servicios;
import javax.persistence.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportadorCSV {

  private final EntityManagerFactory emf;

  public ExportadorCSV() {
    this.emf = Persistence.createEntityManagerFactory("simple-persistence-unit");
  }
  @SuppressWarnings("unchecked")
  public void exportarVistaACSV(String vista, String archivoSalida) throws IOException {
    EntityManager em = emf.createEntityManager();
    try (FileWriter csvWriter = new FileWriter(archivoSalida)) {
      List<Tuple> resultados = em.createNativeQuery("SELECT * FROM " + vista, Tuple.class)
          .getResultList();

      if (resultados.isEmpty()) return;

      Tuple primeraFila = resultados.get(0);
      String encabezado = String.join(",", primeraFila.getElements().stream()
          .map(TupleElement::getAlias)
          .toArray(String[]::new));
      csvWriter.append(encabezado).append("\n");

      for (Tuple fila : resultados) {
        StringBuilder row = new StringBuilder();
        int i = 0;
        for (TupleElement<?> col : fila.getElements()) {
          Object valor = fila.get(col);
          row.append(valor != null ? valor.toString() : "");
          if (++i < fila.getElements().size()) row.append(",");
        }
        csvWriter.append(row.toString()).append("\n");
      }

      csvWriter.flush();
    } finally {
      em.close();
    }
  }

  public void cerrar() {
    emf.close();
  }
}
