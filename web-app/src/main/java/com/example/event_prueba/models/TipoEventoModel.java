package com.example.event_prueba.models;

import jakarta.persistence.*;

@Entity
@Table(name = "TiposEvento")
public class TipoEventoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idtipoevento;
    private String nombretipo;
    private String descripcion;

    public TipoEventoModel() {
    }

    public TipoEventoModel(int idtipoevento, String nombretipo, String descripcion) {
        this.idtipoevento = idtipoevento;
        this.nombretipo = nombretipo;
        this.descripcion = descripcion;
    }

    public int getIdtipoevento() {
        return idtipoevento;
    }

    public void setIdtipoevento(int idtipoevento) {
        this.idtipoevento = idtipoevento;
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
