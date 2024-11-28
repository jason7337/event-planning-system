package com.eventplanningsystem.model;

public class Invitado {
    private int idInvitado;
    private String nombre;
    private String correoElectronico;
    private String telefono;
    private TipoInvitado tipoInvitado;

    // Constructor sin parámetros
    public Invitado() {}

    // Constructor con parámetros
    public Invitado(int idInvitado, String nombre, String correoElectronico, String telefono, TipoInvitado tipoInvitado) {
        this.idInvitado = idInvitado;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.tipoInvitado = tipoInvitado;
    }

    // Getters y setters
    public int getIdInvitado() {
        return idInvitado;
    }

    public void setIdInvitado(int idInvitado) {
        this.idInvitado = idInvitado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoInvitado getTipoInvitado() {
        return tipoInvitado;
    }

    public void setTipoInvitado(TipoInvitado tipoInvitado) {
        this.tipoInvitado = tipoInvitado;
    }
}