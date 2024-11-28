package com.example.event_prueba.repository;

import com.example.event_prueba.models.InvitadoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitadoRepository extends JpaRepository<InvitadoModel, Integer> {
    InvitadoModel findByCorreoElectronico(String correo);
}
