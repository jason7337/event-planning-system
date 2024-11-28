package com.example.event_prueba.service;

import com.example.event_prueba.models.EventoModel;
import com.example.event_prueba.models.TipoEventoModel;
import com.example.event_prueba.models.TipoInvitadoModel;
import com.example.event_prueba.repository.TipoEventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoEventoService {
    @Autowired
    private TipoEventoRepository tipoEventoRepository;
    public List<TipoEventoModel> getTiposEventos(){
        return tipoEventoRepository.findAll();
    }

    public TipoEventoModel guardarTipoEvento(TipoEventoModel tipoEventoModel){
        return tipoEventoRepository.save(tipoEventoModel);
    }

    public TipoEventoModel buscarId(int idTipoEventoModel){
        return tipoEventoRepository.findById(idTipoEventoModel).orElse(null);
    }
}
