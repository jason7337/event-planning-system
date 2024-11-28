package com.example.event_prueba.service;

import com.example.event_prueba.models.InvitadoModel;
import com.example.event_prueba.repository.InvitadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitadoService {
    @Autowired
    private InvitadoRepository invitadoRepository;

    public List<InvitadoModel> getInvitados(){
        return invitadoRepository.findAll();
    }

    public InvitadoModel guardarInvitado(InvitadoModel invitadoModel){
        return invitadoRepository.save(invitadoModel);
    }

    public InvitadoModel buscarId(int idInvitado){
        return invitadoRepository.findById(idInvitado).orElse(null);
    }

    public InvitadoModel buscarPorCorreo(String correo){
        return invitadoRepository.findByCorreoElectronico(correo);
    }
}
