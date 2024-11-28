package com.example.event_prueba.repository;

import com.example.event_prueba.models.TipoInvitadoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface TipoInvitadoRepository extends JpaRepository<TipoInvitadoModel, Integer> {
}
