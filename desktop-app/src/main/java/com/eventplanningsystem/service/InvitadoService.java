package com.eventplanningsystem.service;

import java.sql.SQLException;
import java.util.List;

import com.eventplanningsystem.dao.InvitadoDAO;
import com.eventplanningsystem.model.Invitado;

public class InvitadoService {
    private InvitadoDAO invitadoDAO;

    public InvitadoService() {
        this.invitadoDAO = new InvitadoDAO();
    }

    public Invitado getInvitadoById(int id) throws SQLException {
        return invitadoDAO.getInvitadoById(id);
    }

    public List<Invitado> getAllInvitados() throws SQLException {
        return invitadoDAO.getAllInvitados();
    }

    public void createInvitado(Invitado invitado) throws SQLException {
        invitadoDAO.createInvitado(invitado);
    }

    public void updateInvitado(Invitado invitado) throws SQLException {
        invitadoDAO.updateInvitado(invitado);
    }

    public void deleteInvitado(int id) throws SQLException {
        invitadoDAO.deleteInvitado(id);
    }
}