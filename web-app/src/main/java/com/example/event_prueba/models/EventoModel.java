package com.example.event_prueba.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Eventos")
public class EventoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idevento;
    private String titulo;
    private String descripcion;
    private String fechainicio;
    private String fechafin;
    private String ubicacion;
    @ManyToOne
    @JoinColumn(name = "idorganizador", referencedColumnName = "idusuario")
    private UsuarioModel idorganizador;
    @ManyToOne
    @JoinColumn(name = "idtipoevento", referencedColumnName = "idtipoevento")
    private TipoEventoModel idtipoevento;
    private String estado;

    public EventoModel() {
    }

    public EventoModel(int idevento, String titulo, String descripcion, String fechainicio, String fechafin, String ubicacion, UsuarioModel idorganizador, TipoEventoModel idtipoevento, String estado) {
        this.idevento = idevento;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechainicio = fechainicio;
        this.fechafin = fechafin;
        this.ubicacion = ubicacion;
        this.idorganizador = idorganizador;
        this.idtipoevento = idtipoevento;
        this.estado = estado;
    }

    public int getIdevento() {
        return idevento;
    }

    public void setIdevento(int idevento) {
        this.idevento = idevento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(String fechainicio) {
        this.fechainicio = fechainicio;
    }

    public String getFechafin() {
        return fechafin;
    }

    public void setFechafin(String fechafin) {
        this.fechafin = fechafin;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public UsuarioModel getIdorganizador() {
        return idorganizador;
    }

    public void setIdorganizador(UsuarioModel idorganizador) {
        this.idorganizador = idorganizador;
    }

    public TipoEventoModel getIdtipoevento() {
        return idtipoevento;
    }

    public void setIdtipoevento(TipoEventoModel idtipoevento) {
        this.idtipoevento = idtipoevento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
