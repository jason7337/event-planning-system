package com.eventplanningsystem.service;

import com.eventplanningsystem.dao.EventoDAO;
import com.eventplanningsystem.model.Evento;

import java.sql.SQLException;
import java.util.List;

public class EventoService {
    private EventoDAO eventoDAO;

    public EventoService() {
        this.eventoDAO = new EventoDAO();
    }

    public Evento getEventoById(int id) throws SQLException {
        return eventoDAO.getEventoById(id);
    }

    public List<Evento> getAllEventos() throws SQLException {
        return eventoDAO.getAllEventos();
    }

    public void createEvento(Evento evento) throws SQLException {
        eventoDAO.createEvento(evento);
    }

    public void updateEvento(Evento evento) throws SQLException {
        eventoDAO.updateEvento(evento);
    }

    public void deleteEvento(int id) throws SQLException {
        eventoDAO.deleteEvento(id);
    }
}