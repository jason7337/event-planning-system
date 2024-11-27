package com.eventplanningsystem.dao;

import com.eventplanningsystem.model.Evento;
import com.eventplanningsystem.model.User;
import com.eventplanningsystem.model.TipoEvento;
import com.eventplanningsystem.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {
    public Evento getEventoById(int id) throws SQLException {
        String sql = "SELECT e.*, u.nombre AS nombreOrganizador, u.correoElectronico, " +
                     "te.nombreTipo AS nombreTipoEvento, te.descripcion AS descripcionTipoEvento " +
                     "FROM Eventos e " +
                     "JOIN Usuarios u ON e.idOrganizador = u.idUsuario " +
                     "JOIN TiposEvento te ON e.idTipoEvento = te.idTipoEvento " +
                     "WHERE e.idEvento = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEvento(rs);
                }
            }
        }
        return null;
    }

    public void createEvento(Evento evento) throws SQLException {
        String sql = "INSERT INTO Eventos (titulo, descripcion, fechaInicio, fechaFin, ubicacion, idOrganizador, idTipoEvento, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, evento.getTitulo());
            pstmt.setString(2, evento.getDescripcion());
            pstmt.setTimestamp(3, Timestamp.valueOf(evento.getFechaInicio()));
            pstmt.setTimestamp(4, evento.getFechaFin() != null ? Timestamp.valueOf(evento.getFechaFin()) : null);
            pstmt.setString(5, evento.getUbicacion());
            pstmt.setInt(6, evento.getOrganizador().getIdUsuario());
            pstmt.setInt(7, evento.getTipoEvento().getIdTipoEvento());
            pstmt.setString(8, evento.getEstado());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    evento.setIdEvento(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateEvento(Evento evento) throws SQLException {
        String sql = "UPDATE Eventos SET titulo = ?, descripcion = ?, fechaInicio = ?, fechaFin = ?, " +
                     "ubicacion = ?, idOrganizador = ?, idTipoEvento = ?, estado = ? WHERE idEvento = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, evento.getTitulo());
            pstmt.setString(2, evento.getDescripcion());
            pstmt.setTimestamp(3, Timestamp.valueOf(evento.getFechaInicio()));
            pstmt.setTimestamp(4, evento.getFechaFin() != null ? Timestamp.valueOf(evento.getFechaFin()) : null);
            pstmt.setString(5, evento.getUbicacion());
            pstmt.setInt(6, evento.getOrganizador().getIdUsuario());
            pstmt.setInt(7, evento.getTipoEvento().getIdTipoEvento());
            pstmt.setString(8, evento.getEstado());
            pstmt.setInt(9, evento.getIdEvento());
            pstmt.executeUpdate();
        }
    }

    public void deleteEvento(int id) throws SQLException {
        String sql = "DELETE FROM Eventos WHERE idEvento = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Evento> getAllEventos() throws SQLException {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT e.*, u.nombre AS nombreOrganizador, u.correoElectronico, " +
                     "te.nombreTipo AS nombreTipoEvento, te.descripcion AS descripcionTipoEvento " +
                     "FROM Eventos e " +
                     "JOIN Usuarios u ON e.idOrganizador = u.idUsuario " +
                     "JOIN TiposEvento te ON e.idTipoEvento = te.idTipoEvento";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                eventos.add(mapResultSetToEvento(rs));
            }
        }
        return eventos;
    }

    private Evento mapResultSetToEvento(ResultSet rs) throws SQLException {
        Evento evento = new Evento();
        evento.setIdEvento(rs.getInt("idEvento"));
        evento.setTitulo(rs.getString("titulo"));
        evento.setDescripcion(rs.getString("descripcion"));
        evento.setFechaInicio(rs.getTimestamp("fechaInicio").toLocalDateTime());
        Timestamp fechaFin = rs.getTimestamp("fechaFin");
        if (fechaFin != null) {
            evento.setFechaFin(fechaFin.toLocalDateTime());
        }
        evento.setUbicacion(rs.getString("ubicacion"));
        evento.setEstado(rs.getString("estado"));
        
        User organizador = new User();
        organizador.setIdUsuario(rs.getInt("idOrganizador"));
        organizador.setNombre(rs.getString("nombreOrganizador"));
        organizador.setCorreoElectronico(rs.getString("correoElectronico"));
        evento.setOrganizador(organizador);
        
        TipoEvento tipoEvento = new TipoEvento();
        tipoEvento.setIdTipoEvento(rs.getInt("idTipoEvento"));
        tipoEvento.setNombreTipo(rs.getString("nombreTipoEvento"));
        tipoEvento.setDescripcion(rs.getString("descripcionTipoEvento"));
        evento.setTipoEvento(tipoEvento);
        
        return evento;
    }
}