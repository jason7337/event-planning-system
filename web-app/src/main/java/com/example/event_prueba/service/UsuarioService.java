package com.example.event_prueba.service;

import com.example.event_prueba.models.UsuarioModel;
import com.example.event_prueba.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioModel> getUsuarios(){
        return usuarioRepository.findAll();
    }

    public UsuarioModel guardarUsuario(UsuarioModel usuarioModel){
        return usuarioRepository.save(usuarioModel);
    }

    public UsuarioModel buscarId(int idUsuario){
        return usuarioRepository.findById(idUsuario).orElse(null);
    }

    public UsuarioModel buscarPorCorreo(String correo){
        return usuarioRepository.findByCorreoElectronico(correo);
    }
}
