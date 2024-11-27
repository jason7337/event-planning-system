package com.eventplanningsystem.model;

public class TipoInvitado {
    private int idTipoInvitado;
    private String nombreTipo;
    private String descripcion;

    // Constructor sin parámetros
    public TipoInvitado() {}

    // Constructor con parámetros
    public TipoInvitado(int idTipoInvitado, String nombreTipo, String descripcion) {
        this.idTipoInvitado = idTipoInvitado;
        this.nombreTipo = nombreTipo;
        this.descripcion = descripcion;
    }

    // Getters y setters
    public int getIdTipoInvitado() {
        return idTipoInvitado;
    }

    public void setIdTipoInvitado(int idTipoInvitado) {
        this.idTipoInvitado = idTipoInvitado;
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