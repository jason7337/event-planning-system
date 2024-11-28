package com.example.event_prueba.repository;

import com.example.event_prueba.models.TipoEventoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoEventoRepository extends JpaRepository<TipoEventoModel, Integer> {
}
