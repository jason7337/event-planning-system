package com.example.event_prueba.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tipoinvitado")
public class TipoInvitadoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idtipoinvitado;
    private String nombretipo;
    private String descripcion;

    public TipoInvitadoModel() {
    }

    public TipoInvitadoModel(int idtipoinvitado, String nombretipo, String descripcion) {
        this.idtipoinvitado = idtipoinvitado;
        this.nombretipo = nombretipo;
        this.descripcion = descripcion;
    }

    public int getIdtipoinvitado() {
        return idtipoinvitado;
    }

    public void setIdtipoinvitado(int idtipoinvitado) {
        this.idtipoinvitado = idtipoinvitado;
    }

    public String getNombretipo() {
        return nombretipo;
    }

    public void setNombretipo(String nombretipo) {
        this.nombretipo = nombretipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

