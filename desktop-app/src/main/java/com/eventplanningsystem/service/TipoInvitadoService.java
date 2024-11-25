package com.eventplanningsystem.service;

import com.eventplanningsystem.dao.TipoInvitadoDAO;
import com.eventplanningsystem.model.TipoInvitado;

import java.sql.SQLException;
import java.util.List;

public class TipoInvitadoService {
    private TipoInvitadoDAO tipoInvitadoDAO;

    public TipoInvitadoService() {
        this.tipoInvitadoDAO = new TipoInvitadoDAO();
    }

    public TipoInvitado getTipoInvitadoById(int id) throws SQLException {
        return tipoInvitadoDAO.getTipoInvitadoById(id);
    }

    public List<TipoInvitado> getAllTiposInvitado() throws SQLException {
        return tipoInvitadoDAO.getAllTiposInvitado();
    }

    public void createTipoInvitado(TipoInvitado tipoInvitado) throws SQLException {
        tipoInvitadoDAO.createTipoInvitado(tipoInvitado);
    }

    public void updateTipoInvitado(TipoInvitado tipoInvitado) throws SQLException {
        tipoInvitadoDAO.updateTipoInvitado(tipoInvitado);
    }

    public void deleteTipoInvitado(int id) throws SQLException {
        tipoInvitadoDAO.deleteTipoInvitado(id);
    }
}