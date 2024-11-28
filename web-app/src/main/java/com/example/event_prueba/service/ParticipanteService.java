package com.example.event_prueba.service;

import com.example.event_prueba.models.ParticipanteModel;
import com.example.event_prueba.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipanteService {
    @Autowired
    private ParticipanteRepository participanteRepository;

    public List<ParticipanteModel> getParticipantes(){
        return participanteRepository.findAll();
    }

    public ParticipanteModel guardarParticipante(ParticipanteModel participanteModel){
        return participanteRepository.save(participanteModel);
    }

    public ParticipanteModel buscarId(int idParticipante){
        return participanteRepository.findById(idParticipante).orElse(null);
    }
}
