package com.example.event_prueba.repository;

import com.example.event_prueba.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    UsuarioModel findByCorreoElectronico(String correo);
}
