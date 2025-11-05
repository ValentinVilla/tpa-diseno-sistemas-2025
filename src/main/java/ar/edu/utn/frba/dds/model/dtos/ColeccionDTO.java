package ar.edu.utn.frba.dds.model.dtos;

import ar.edu.utn.frba.dds.model.mappers.MapperColeccion;

import java.util.Objects;

public class ColeccionDTO {

    private MapperColeccion mapper = new MapperColeccion();
    private Long id;
    private String titulo;
    private String descripcion;
    private String algoritmo;
    private Long fuente;
    private String criterio;
    private Long criterioId; // seleccionar un filtro existente
    private String criterioTipo; // tipo de filtro si se crea nuevo (ej: 'TEXTO','CATEGORIA','FECHA','UBICACION')
    private String navegacion;

    public ColeccionDTO() {
    }

    public ColeccionDTO(Long id, String titulo, String descripcion, String algoritmo, Long fuente, String criterio, Long criterioId, String criterioTipo, String navegacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.algoritmo = algoritmo;
        this.fuente = fuente;
        this.criterio = criterio;
        this.criterioId = criterioId;
        this.criterioTipo = criterioTipo;
        this.navegacion = navegacion;
    }

    public Long getId() {
        return id;
    }

    public ColeccionDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitulo() {
        return titulo;
    }

    public ColeccionDTO setTitulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public ColeccionDTO setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public ColeccionDTO setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
        return this;
    }

    public Long getFuente() {
        return fuente;
    }

    public ColeccionDTO setFuente(Long fuente) {
        this.fuente = fuente;
        return this;
    }

    public String getCriterio() {
        return criterio;
    }

    public ColeccionDTO setCriterio(String criterio) {
        this.criterio = criterio;
        return this;
    }

    public Long getCriterioId() { return criterioId; }
    public ColeccionDTO setCriterioId(Long criterioId) { this.criterioId = criterioId; return this; }

    public String getCriterioTipo() { return criterioTipo; }
    public ColeccionDTO setCriterioTipo(String criterioTipo) { this.criterioTipo = criterioTipo; return this; }

    public String getNavegacion() {
        return navegacion;
    }

    public ColeccionDTO setNavegacion(String navegacion) {
        this.navegacion = navegacion;
        return this;
    }

}
