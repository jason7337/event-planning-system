package com.eventplanningsystem.service;

import com.eventplanningsystem.dao.ParticipanteEventoDAO;
import com.eventplanningsystem.model.ParticipanteEvento;

import java.sql.SQLException;
import java.util.List;

public class ParticipanteEventoService {
    private ParticipanteEventoDAO participanteEventoDAO;

    public ParticipanteEventoService() {
        this.participanteEventoDAO = new ParticipanteEventoDAO();
    }

    public ParticipanteEvento getParticipanteEventoById(int id) throws SQLException {
        return participanteEventoDAO.getParticipanteEventoById(id);
    }

    public List<ParticipanteEvento> getAllParticipantesEvento() throws SQLException {
        return participanteEventoDAO.getAllParticipantesEvento();
    }

    public void createParticipanteEvento(ParticipanteEvento participanteEvento) throws SQLException {
        participanteEventoDAO.createParticipanteEvento(participanteEvento);
    }

    public void updateParticipanteEvento(ParticipanteEvento participanteEvento) throws SQLException {
        participanteEventoDAO.updateParticipanteEvento(participanteEvento);
    }

    public void deleteParticipanteEvento(int id) throws SQLException {
        participanteEventoDAO.deleteParticipanteEvento(id);
    }
}