package com.example.event_prueba.models;

public class LoginForm {
    private String correoElectronico;
    private String contraseña;

    public LoginForm() {
    }

    public LoginForm(String correoElectronico, String contraseña) {
        this.correoElectronico = correoElectronico;
        this.contraseña = contraseña;
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
}
