package com.example.event_prueba.service;

import com.example.event_prueba.models.TipoInvitadoModel;
import com.example.event_prueba.repository.TipoInvitadoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TipoInvitadoService {
    @Autowired
    private TipoInvitadoRepository tipoInvitadoRepository;

    public List<TipoInvitadoModel> getTiposInvitados(){
        return tipoInvitadoRepository.findAll();
    }

}
