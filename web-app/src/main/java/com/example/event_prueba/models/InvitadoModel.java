package com.example.event_prueba.models;

import jakarta.persistence.*;

@Entity
@Table(name = "invitados")
public class InvitadoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idinvitado;
    private String nombre;
    private String correoElectronico;
    private String contraseña;
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "idtipoinvitado", referencedColumnName = "idtipoinvitado")
    private TipoInvitadoModel tipoinvitado;

    public InvitadoModel() {
    }

    public InvitadoModel(int idinvitado, String nombre, String correoElectronico, String contraseña, String telefono, TipoInvitadoModel tipoinvitado) {
        this.idinvitado = idinvitado;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.tipoinvitado = tipoinvitado;
    }

    public int getIdinvitado() {
        return idinvitado;
    }

    public void setIdinvitado(int idinvitado) {
        this.idinvitado = idinvitado;
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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoInvitadoModel getTipoinvitado() {
        return tipoinvitado;
    }

    public void setTipoinvitado(TipoInvitadoModel tipoinvitado) {
        this.tipoinvitado = tipoinvitado;
    }
}
