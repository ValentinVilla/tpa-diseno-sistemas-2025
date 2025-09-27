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

    protected String titulo;
    protected String descripcion;
    protected String categoria;
    //digamos que estos son los campos que identifican que un hecho es identico a otro

    public boolean correspondeA(Hecho hecho){
        return this.titulo.equals(hecho.getTitulo()) &&
               this.descripcion.equals(hecho.getDescripcion()) &&
               this.categoria.equals(hecho.getCategoria());
    }

    public boolean correspondeA(HechoDinamico hecho){
        return this.titulo.equals(hecho.getTitulo()) &&
            this.descripcion.equals(hecho.getDescripcion()) &&
            this.categoria.equals(hecho.getCategoria());
    }

    public HechoEliminado(String titulo, String descripcion, String categoria) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public HechoEliminado() {
    }

    public Long getId() {
        return id;
    }
}