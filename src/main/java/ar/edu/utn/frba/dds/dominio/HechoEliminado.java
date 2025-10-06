package ar.edu.utn.frba.dds.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class HechoEliminado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String detalleConcatenado;

    public HechoEliminado(Hecho hecho) {
        this.detalleConcatenado =
            hecho.getTitulo() + " | " + hecho.getDescripcion() + " | " + hecho.getCategoria();
    }

    public HechoEliminado() {
    }

    public boolean correspondeA(Hecho hecho) {
        String comparacion = hecho.getTitulo() + " | " + hecho.getDescripcion() + " | " + hecho.getCategoria();
        return this.detalleConcatenado.equals(comparacion);
    }
}
