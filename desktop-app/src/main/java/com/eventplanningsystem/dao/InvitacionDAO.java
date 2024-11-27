package com.eventplanningsystem.dao;

import com.eventplanningsystem.model.Invitacion;
import com.eventplanningsystem.model.Evento;
import com.eventplanningsystem.model.Invitado;
import com.eventplanningsystem.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvitacionDAO {
    public Invitacion getInvitacionById(int id) throws SQLException {
        String sql = "SELECT i.*, e.titulo AS tituloEvento, inv.nombre AS nombreInvitado " +
                     "FROM Invitaciones i " +
                     "JOIN Eventos e ON i.idEvento = e.idEvento " +
                     "JOIN Invitados inv ON i.idInvitado = inv.idInvitado " +
                     "WHERE i.idInvitacion = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvitacion(rs);
                }
            }
        }
        return null;
    }

    public void createInvitacion(Invitacion invitacion) throws SQLException {
        String sql = "INSERT INTO Invitaciones (idEvento, idInvitado, estado, fechaRespuesta) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, invitacion.getEvento().getIdEvento());
            pstmt.setInt(2, invitacion.getInvitado().getIdInvitado());
            pstmt.setString(3, invitacion.getEstado());
            pstmt.setTimestamp(4, invitacion.getFechaRespuesta() != null ? Timestamp.valueOf(invitacion.getFechaRespuesta()) : null);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invitacion.setIdInvitacion(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateInvitacion(Invitacion invitacion) throws SQLException {
        String sql = "UPDATE Invitaciones SET idEvento = ?, idInvitado = ?, estado = ?, fechaRespuesta = ? WHERE idInvitacion = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, invitacion.getEvento().getIdEvento());
            pstmt.setInt(2, invitacion.getInvitado().getIdInvitado());
            pstmt.setString(3, invitacion.getEstado());
            pstmt.setTimestamp(4, invitacion.getFechaRespuesta() != null ? Timestamp.valueOf(invitacion.getFechaRespuesta()) : null);
            pstmt.setInt(5, invitacion.getIdInvitacion());
            pstmt.executeUpdate();
        }
    }

    public void deleteInvitacion(int id) throws SQLException {
        String sql = "DELETE FROM Invitaciones WHERE idInvitacion = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Invitacion> getAllInvitaciones() throws SQLException {
        List<Invitacion> invitaciones = new ArrayList<>();
        String sql = "SELECT i.*, e.titulo AS tituloEvento, inv.nombre AS nombreInvitado " +
                     "FROM Invitaciones i " +
                     "JOIN Eventos e ON i.idEvento = e.idEvento " +
                     "JOIN Invitados inv ON i.idInvitado = inv.idInvitado";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                invitaciones.add(mapResultSetToInvitacion(rs));
            }
        }
        return invitaciones;
    }

    private Invitacion mapResultSetToInvitacion(ResultSet rs) throws SQLException {
        Invitacion invitacion = new Invitacion();
        invitacion.setIdInvitacion(rs.getInt("idInvitacion"));
        
        Evento evento = new Evento();
        evento.setIdEvento(rs.getInt("idEvento"));
        evento.setTitulo(rs.getString("tituloEvento"));
        invitacion.setEvento(evento);
        
        Invitado invitado = new Invitado();
        invitado.setIdInvitado(rs.getInt("idInvitado"));
        invitado.setNombre(rs.getString("nombreInvitado"));
        invitacion.setInvitado(invitado);
        
        invitacion.setEstado(rs.getString("estado"));
        Timestamp fechaRespuesta = rs.getTimestamp("fechaRespuesta");
        if (fechaRespuesta != null) {
            invitacion.setFechaRespuesta(fechaRespuesta.toLocalDateTime());
        }
        
        return invitacion;
    }
}