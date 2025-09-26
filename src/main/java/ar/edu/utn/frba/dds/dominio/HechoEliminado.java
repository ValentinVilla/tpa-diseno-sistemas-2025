package ar.edu.utn.frba.dds.dominio;

public class HechoEliminado {
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

    HechoEliminado(String titulo, String descripcion, String categoria) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }
}