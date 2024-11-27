package com.eventplanningsystem.service;

import com.eventplanningsystem.dao.TipoEventoDAO;
import com.eventplanningsystem.model.TipoEvento;

import java.sql.SQLException;
import java.util.List;

public class TipoEventoService {
    private TipoEventoDAO tipoEventoDAO;

    public TipoEventoService() {
        this.tipoEventoDAO = new TipoEventoDAO();
    }

    public TipoEvento getTipoEventoById(int id) throws SQLException {
        return tipoEventoDAO.getTipoEventoById(id);
    }

    public List<TipoEvento> getAllTiposEvento() throws SQLException {
        return tipoEventoDAO.getAllTiposEvento();
    }

    public void createTipoEvento(TipoEvento tipoEvento) throws SQLException {
        tipoEventoDAO.createTipoEvento(tipoEvento);
    }

    public void updateTipoEvento(TipoEvento tipoEvento) throws SQLException {
        tipoEventoDAO.updateTipoEvento(tipoEvento);
    }

    public void deleteTipoEvento(int id) throws SQLException {
        tipoEventoDAO.deleteTipoEvento(id);
    }
}