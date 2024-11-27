package com.eventplanningsystem.service;

import com.eventplanningsystem.dao.MensajeDAO;
import com.eventplanningsystem.model.Mensaje;

import java.sql.SQLException;
import java.util.List;

public class MensajeService {
    private MensajeDAO mensajeDAO;

    public MensajeService() {
        this.mensajeDAO = new MensajeDAO();
    }

    public void createMensaje(Mensaje mensaje) throws SQLException {
        mensajeDAO.createMensaje(mensaje);
    }

    public Mensaje getMensajeById(int id) throws SQLException {
        return mensajeDAO.getMensajeById(id);
    }

    public List<Mensaje> getAllMensajes() throws SQLException {
        return mensajeDAO.getAllMensajes();
    }

    public void updateMensaje(Mensaje mensaje) throws SQLException {
        mensajeDAO.updateMensaje(mensaje);
    }

    public void deleteMensaje(int id) throws SQLException {
        mensajeDAO.deleteMensaje(id);
    }
}