package com.eventplanningsystem.model;

public class TipoEvento {
    private int idTipoEvento;
    private String nombreTipo;
    private String descripcion;

    // Constructor sin parámetros
    public TipoEvento() {}

    // Constructor con parámetros
    public TipoEvento(int idTipoEvento, String nombreTipo, String descripcion) {
        this.idTipoEvento = idTipoEvento;
        this.nombreTipo = nombreTipo;
        this.descripcion = descripcion;
    }

    // Getters y setters
    public int getIdTipoEvento() {
        return idTipoEvento;
    }

    public void setIdTipoEvento(int idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}