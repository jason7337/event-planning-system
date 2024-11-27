package com.eventplanningsystem.service;

import com.eventplanningsystem.dao.CorreoDAO;
import com.eventplanningsystem.model.Correo;

import java.sql.SQLException;
import java.util.List;

public class CorreoService {
    private CorreoDAO correoDAO;

    public CorreoService() {
        this.correoDAO = new CorreoDAO();
    }

    public Correo getCorreoById(int id) throws SQLException {
        return correoDAO.getCorreoById(id);
    }

    public List<Correo> getAllCorreos() throws SQLException {
        return correoDAO.getAllCorreos();
    }

    public void createCorreo(Correo correo) throws SQLException {
        correoDAO.createCorreo(correo);
    }

    public void updateCorreo(Correo correo) throws SQLException {
        correoDAO.updateCorreo(correo);
    }

    public void deleteCorreo(int id) throws SQLException {
        correoDAO.deleteCorreo(id);
    }
}