package com.eventplanningsystem.model;

import java.time.LocalDateTime;

public class Correo {
    private int idCorreo;
    private Evento evento;
    private User usuario;
    private Invitado invitado;
    private String asunto;
    private String mensaje;
    private LocalDateTime fechaEnvio;

    // Constructor sin parámetros
    public Correo() {}

    // Constructor con parámetros
    public Correo(int idCorreo, Evento evento, User usuario, Invitado invitado, String asunto, String mensaje, LocalDateTime fechaEnvio) {
        this.idCorreo = idCorreo;
        this.evento = evento;
        this.usuario = usuario;
        this.invitado = invitado;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
    }

    // Getters y setters
    public int getIdCorreo() {
        return idCorreo;
    }

    public void setIdCorreo(int idCorreo) {
        this.idCorreo = idCorreo;
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

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
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