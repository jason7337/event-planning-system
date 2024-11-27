package com.eventplanningsystem.model;

public class User {
    private int idUsuario;
    private String nombre;
    private String correoElectronico;
    private String password;
    private String telefono;
    private String tipoUsuario;
    private String fechaRegistro; 

    // Constructor sin parámetros
    public User() {}

    // Constructor con parámetros
    public User(int idUsuario, String nombre, String correoElectronico, String password, String telefono, String tipoUsuario, String fechaRegistro) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.password = password;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}