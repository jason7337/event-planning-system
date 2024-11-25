package com.eventplanningsystem.dao;

import com.eventplanningsystem.model.ParticipanteEvento;
import com.eventplanningsystem.model.Evento;
import com.eventplanningsystem.model.Invitado;
import com.eventplanningsystem.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParticipanteEventoDAO {
    public ParticipanteEvento getParticipanteEventoById(int id) throws SQLException {
        String sql = "SELECT pe.*, e.titulo AS tituloEvento, i.nombre AS nombreInvitado " +
                     "FROM ParticipantesEvento pe " +
                     "JOIN Eventos e ON pe.idEvento = e.idEvento " +
                     "JOIN Invitados i ON pe.idInvitado = i.idInvitado " +
                     "WHERE pe.idParticipante = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToParticipanteEvento(rs);
                }
            }
        }
        return null;
    }

    public void createParticipanteEvento(ParticipanteEvento participanteEvento) throws SQLException {
        String sql = "INSERT INTO ParticipantesEvento (idEvento, idInvitado, estado) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, participanteEvento.getEvento().getIdEvento());
            pstmt.setInt(2, participanteEvento.getInvitado().getIdInvitado());
            pstmt.setString(3, participanteEvento.getEstado());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    participanteEvento.setIdParticipante(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateParticipanteEvento(ParticipanteEvento participanteEvento) throws SQLException {
        String sql = "UPDATE ParticipantesEvento SET idEvento = ?, idInvitado = ?, estado = ? WHERE idParticipante = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, participanteEvento.getEvento().getIdEvento());
            pstmt.setInt(2, participanteEvento.getInvitado().getIdInvitado());
            pstmt.setString(3, participanteEvento.getEstado());
            pstmt.setInt(4, participanteEvento.getIdParticipante());
            pstmt.executeUpdate();
        }
    }

    public void deleteParticipanteEvento(int id) throws SQLException {
        String sql = "DELETE FROM ParticipantesEvento WHERE idParticipante = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<ParticipanteEvento> getAllParticipantesEvento() throws SQLException {
        List<ParticipanteEvento> participantesEvento = new ArrayList<>();
        String sql = "SELECT pe.*, e.titulo AS tituloEvento, i.nombre AS nombreInvitado " +
                     "FROM ParticipantesEvento pe " +
                     "JOIN Eventos e ON pe.idEvento = e.idEvento " +
                     "JOIN Invitados i ON pe.idInvitado = i.idInvitado";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                participantesEvento.add(mapResultSetToParticipanteEvento(rs));
            }
        }
        return participantesEvento;
    }

    private ParticipanteEvento mapResultSetToParticipanteEvento(ResultSet rs) throws SQLException {
        ParticipanteEvento participanteEvento = new ParticipanteEvento();
        participanteEvento.setIdParticipante(rs.getInt("idParticipante"));
        participanteEvento.setEstado(rs.getString("estado"));
        
        Evento evento = new Evento();
        evento.setIdEvento(rs.getInt("idEvento"));
        evento.setTitulo(rs.getString("tituloEvento"));
        participanteEvento.setEvento(evento);
        
        Invitado invitado = new Invitado();
        invitado.setIdInvitado(rs.getInt("idInvitado"));
        invitado.setNombre(rs.getString("nombreInvitado"));
        participanteEvento.setInvitado(invitado);
        
        return participanteEvento;
    }
}