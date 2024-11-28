package com.example.event_prueba.repository;

import com.example.event_prueba.models.ParticipanteModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipanteRepository extends JpaRepository<ParticipanteModel, Integer> {
}
