package com.eventplanningsystem.service;

import com.eventplanningsystem.dao.InvitacionDAO;
import com.eventplanningsystem.model.Invitacion;

import java.sql.SQLException;
import java.util.List;

public class InvitacionService {
    private InvitacionDAO invitacionDAO;

    public InvitacionService() {
        this.invitacionDAO = new InvitacionDAO();
    }

    public Invitacion getInvitacionById(int id) throws SQLException {
        return invitacionDAO.getInvitacionById(id);
    }

    public List<Invitacion> getAllInvitaciones() throws SQLException {
        return invitacionDAO.getAllInvitaciones();
    }

    public void createInvitacion(Invitacion invitacion) throws SQLException {
        invitacionDAO.createInvitacion(invitacion);
    }

    public void updateInvitacion(Invitacion invitacion) throws SQLException {
        invitacionDAO.updateInvitacion(invitacion);
    }

    public void deleteInvitacion(int id) throws SQLException {
        invitacionDAO.deleteInvitacion(id);
    }
}