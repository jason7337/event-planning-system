package com.example.event_prueba.service;

import com.example.event_prueba.models.EventoModel;
import com.example.event_prueba.models.TipoEventoModel;
import com.example.event_prueba.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService {
    @Autowired
    private EventoRepository eventoRepository;
    public List<EventoModel> getEventos(){
        return eventoRepository.findAll();
    }

    public EventoModel guardarEvento(EventoModel eventoModel){
        return eventoRepository.save(eventoModel);
    }

    public EventoModel buscarId(int idEvento){
        return eventoRepository.findById(idEvento).orElse(null);
    }
}
