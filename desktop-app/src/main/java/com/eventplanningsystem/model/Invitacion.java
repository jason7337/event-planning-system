package com.eventplanningsystem.model;

import java.time.LocalDateTime;

public class Invitacion {
    private int idInvitacion;
    private Evento evento;
    private Invitado invitado;
    private String estado;
    private LocalDateTime fechaRespuesta;

    // Constructor sin parámetros
    public Invitacion() {}

    // Constructor con parámetros
    public Invitacion(int idInvitacion, Evento evento, Invitado invitado, String estado, LocalDateTime fechaRespuesta) {
        this.idInvitacion = idInvitacion;
        this.evento = evento;
        this.invitado = invitado;
        this.estado = estado;
        this.fechaRespuesta = fechaRespuesta;
    }

    // Getters y setters
    public int getIdInvitacion() {
        return idInvitacion;
    }

    public void setIdInvitacion(int idInvitacion) {
        this.idInvitacion = idInvitacion;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Invitado getInvitado() {
        return invitado;
    }

    public void setInvitado(Invitado invitado) {
        this.invitado = invitado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(LocalDateTime fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }
}