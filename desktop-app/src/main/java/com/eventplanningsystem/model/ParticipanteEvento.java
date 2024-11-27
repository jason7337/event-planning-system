package com.eventplanningsystem.model;

public class ParticipanteEvento {
    private int idParticipante;
    private Evento evento;
    private Invitado invitado;
    private String estado;

    // Constructor sin parámetros
    public ParticipanteEvento() {}

    // Constructor con parámetros
    public ParticipanteEvento(int idParticipante, Evento evento, Invitado invitado, String estado) {
        this.idParticipante = idParticipante;
        this.evento = evento;
        this.invitado = invitado;
        this.estado = estado;
    }

    // Getters y setters
    public int getIdParticipante() {
        return idParticipante;
    }

    public void setIdParticipante(int idParticipante) {
        this.idParticipante = idParticipante;
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
}