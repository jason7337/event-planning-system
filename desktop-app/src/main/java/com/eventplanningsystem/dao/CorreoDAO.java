package com.eventplanningsystem.dao;

import com.eventplanningsystem.model.Correo;
import com.eventplanningsystem.model.Evento;
import com.eventplanningsystem.model.User;
import com.eventplanningsystem.model.Invitado;
import com.eventplanningsystem.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CorreoDAO {
    public Correo getCorreoById(int id) throws SQLException {
        String sql = "SELECT c.*, e.titulo AS eventoTitulo, u.nombre AS usuarioNombre, i.nombre AS invitadoNombre " +
                     "FROM Correos c " +
                     "JOIN Eventos e ON c.idEvento = e.idEvento " +
                     "JOIN Usuarios u ON c.idUsuario = u.idUsuario " +
                     "JOIN Invitados i ON c.idInvitado = i.idInvitado " +
                     "WHERE c.idCorreo = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCorreo(rs);
                }
            }
        }
        return null;
    }

    public void createCorreo(Correo correo) throws SQLException {
        String sql = "INSERT INTO Correos (idEvento, idUsuario, idInvitado, asunto, mensaje) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, correo.getEvento().getIdEvento());
            pstmt.setInt(2, correo.getUsuario().getIdUsuario());
            pstmt.setInt(3, correo.getInvitado().getIdInvitado());
            pstmt.setString(4, correo.getAsunto());
            pstmt.setString(5, correo.getMensaje());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    correo.setIdCorreo(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateCorreo(Correo correo) throws SQLException {
        String sql = "UPDATE Correos SET idEvento = ?, idUsuario = ?, idInvitado = ?, asunto = ?, mensaje = ? WHERE idCorreo = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, correo.getEvento().getIdEvento());
            pstmt.setInt(2, correo.getUsuario().getIdUsuario());
            pstmt.setInt(3, correo.getInvitado().getIdInvitado());
            pstmt.setString(4, correo.getAsunto());
            pstmt.setString(5, correo.getMensaje());
            pstmt.setInt(6, correo.getIdCorreo());
            pstmt.executeUpdate();
        }
    }

    public void deleteCorreo(int id) throws SQLException {
        String sql = "DELETE FROM Correos WHERE idCorreo = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Correo> getAllCorreos() throws SQLException {
        List<Correo> correos = new ArrayList<>();
        String sql = "SELECT c.*, e.titulo AS eventoTitulo, u.nombre AS usuarioNombre, i.nombre AS invitadoNombre " +
                     "FROM Correos c " +
                     "JOIN Eventos e ON c.idEvento = e.idEvento " +
                     "JOIN Usuarios u ON c.idUsuario = u.idUsuario " +
                     "JOIN Invitados i ON c.idInvitado = i.idInvitado";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                correos.add(mapResultSetToCorreo(rs));
            }
        }
        return correos;
    }

    private Correo mapResultSetToCorreo(ResultSet rs) throws SQLException {
        Correo correo = new Correo();
        correo.setIdCorreo(rs.getInt("idCorreo"));
        
        Evento evento = new Evento();
        evento.setIdEvento(rs.getInt("idEvento"));
        evento.setTitulo(rs.getString("eventoTitulo"));
        correo.setEvento(evento);
        
        User usuario = new User();
        usuario.setIdUsuario(rs.getInt("idUsuario"));
        usuario.setNombre(rs.getString("usuarioNombre"));
        correo.setUsuario(usuario);
        
        Invitado invitado = new Invitado();
        invitado.setIdInvitado(rs.getInt("idInvitado"));
        invitado.setNombre(rs.getString("invitadoNombre"));
        correo.setInvitado(invitado);
        
        correo.setAsunto(rs.getString("asunto"));
        correo.setMensaje(rs.getString("mensaje"));
        correo.setFechaEnvio(rs.getTimestamp("fechaEnvio").toLocalDateTime());
        
        return correo;
    }
}