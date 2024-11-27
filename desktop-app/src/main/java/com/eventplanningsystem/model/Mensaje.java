package com.eventplanningsystem.model;

import java.time.LocalDateTime;

public class Mensaje {
    private int idMensaje;
    private Evento evento;
    private User usuario;
    private Invitado invitado;
    private String mensaje;
    private LocalDateTime fechaEnvio;

    public Mensaje() {}

    public Mensaje(int idMensaje, Evento evento, User usuario, Invitado invitado, String mensaje, LocalDateTime fechaEnvio) {
        this.idMensaje = idMensaje;
        this.evento = evento;
        this.usuario = usuario;
        this.invitado = invitado;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
    }

    // Getters y setters
    public int getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(int idMensaje) {
        this.idMensaje = idMensaje;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Invitado getInvitado() {
        return invitado;
    }

    public void setInvitado(Invitado invitado) {
        this.invitado = invitado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}