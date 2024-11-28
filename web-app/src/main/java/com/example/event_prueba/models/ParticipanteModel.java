package com.example.event_prueba.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ParticipantesEvento")
public class ParticipanteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idparticipante;
    @ManyToMany
    @JoinColumn(name = "idevento", referencedColumnName = "idevento")
    private EventoModel idevento;
    @ManyToMany
    @JoinColumn(name = "idinvitado", referencedColumnName = "idinvitado")
    private InvitadoModel idinvitado;
    private String estado;

    public ParticipanteModel() {
    }

    public ParticipanteModel(int idparticipante, EventoModel idevento, InvitadoModel idinvitado, String estado) {
        this.idparticipante = idparticipante;
        this.idevento = idevento;
        this.idinvitado = idinvitado;
        this.estado = estado;
    }

    public int getIdparticipante() {
        return idparticipante;
    }

    public void setIdparticipante(int idparticipante) {
        this.idparticipante = idparticipante;
    }

    public EventoModel getIdevento() {
        return idevento;
    }

    public void setIdevento(EventoModel idevento) {
        this.idevento = idevento;
    }

    public InvitadoModel getIdinvitado() {
        return idinvitado;
    }

    public void setIdinvitado(InvitadoModel idinvitado) {
        this.idinvitado = idinvitado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
