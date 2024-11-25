package com.eventplanningsystem.service;

import com.eventplanningsystem.dao.InvitadoDAO;
import com.eventplanningsystem.model.Invitado;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;

public class InvitadoService {
    private InvitadoDAO invitadoDAO;

    public InvitadoService() {
        this.invitadoDAO = new InvitadoDAO();
    }

    public Invitado authenticateInvitado(String correo, String password) throws SQLException {
        Invitado invitado = invitadoDAO.getInvitadoByCorreo(correo);
        if (invitado != null) {
            String storedHash = invitado.getPassword();
            if (BCrypt.checkpw(password, storedHash)) {
                return invitado;
            }
        }
        return null;
    }

    public Invitado getInvitadoById(int id) throws SQLException {
        return invitadoDAO.getInvitadoById(id);
    }

    public List<Invitado> getAllInvitados() throws SQLException {
        return invitadoDAO.getAllInvitados();
    }

    public void createInvitado(Invitado invitado) throws SQLException {
        invitado.setPassword(hashPassword(invitado.getPassword()));
        invitadoDAO.createInvitado(invitado);
    }

    public void updateInvitado(Invitado invitado) throws SQLException {
        if (invitado.getPassword() != null && !invitado.getPassword().isEmpty()) {
            invitado.setPassword(hashPassword(invitado.getPassword()));
        }
        invitadoDAO.updateInvitado(invitado);
    }

    public void deleteInvitado(int id) throws SQLException {
        invitadoDAO.deleteInvitado(id);
    }

    private String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }
}