package com.example.event_prueba.repository;

import com.example.event_prueba.models.EventoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<EventoModel, Integer> {
}
