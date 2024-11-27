package com.eventplanningsystem.dao;

import com.eventplanningsystem.model.Mensaje;
import com.eventplanningsystem.model.Evento;
import com.eventplanningsystem.model.User;
import com.eventplanningsystem.model.Invitado;
import com.eventplanningsystem.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MensajeDAO {

    public void createMensaje(Mensaje mensaje) throws SQLException {
        String sql = "INSERT INTO Mensajes (idEvento, idUsuario, idInvitado, mensaje) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, mensaje.getEvento().getIdEvento());
            pstmt.setInt(2, mensaje.getUsuario().getIdUsuario());
            pstmt.setInt(3, mensaje.getInvitado().getIdInvitado());
            pstmt.setString(4, mensaje.getMensaje());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating message failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mensaje.setIdMensaje(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating message failed, no ID obtained.");
                }
            }
        }
    }

    public Mensaje getMensajeById(int id) throws SQLException {
        String sql = "SELECT m.*, e.titulo AS eventoTitulo, u.nombre AS usuarioNombre, i.nombre AS invitadoNombre " +
                     "FROM Mensajes m " +
                     "JOIN Eventos e ON m.idEvento = e.idEvento " +
                     "JOIN Usuarios u ON m.idUsuario = u.idUsuario " +
                     "JOIN Invitados i ON m.idInvitado = i.idInvitado " +
                     "WHERE m.idMensaje = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMensaje(rs);
                }
            }
        }
        return null;
    }

    public List<Mensaje> getAllMensajes() throws SQLException {
        List<Mensaje> mensajes = new ArrayList<>();
        String sql = "SELECT m.*, e.titulo AS eventoTitulo, u.nombre AS usuarioNombre, i.nombre AS invitadoNombre " +
                     "FROM Mensajes m " +
                     "JOIN Eventos e ON m.idEvento = e.idEvento " +
                     "JOIN Usuarios u ON m.idUsuario = u.idUsuario " +
                     "JOIN Invitados i ON m.idInvitado = i.idInvitado";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                mensajes.add(mapResultSetToMensaje(rs));
            }
        }
        return mensajes;
    }

    public void updateMensaje(Mensaje mensaje) throws SQLException {
        String sql = "UPDATE Mensajes SET idEvento = ?, idUsuario = ?, idInvitado = ?, mensaje = ? WHERE idMensaje = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, mensaje.getEvento().getIdEvento());
            pstmt.setInt(2, mensaje.getUsuario().getIdUsuario());
            pstmt.setInt(3, mensaje.getInvitado().getIdInvitado());
            pstmt.setString(4, mensaje.getMensaje());
            pstmt.setInt(5, mensaje.getIdMensaje());
            pstmt.executeUpdate();
        }
    }

    public void deleteMensaje(int id) throws SQLException {
        String sql = "DELETE FROM Mensajes WHERE idMensaje = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Mensaje mapResultSetToMensaje(ResultSet rs) throws SQLException {
        Mensaje mensaje = new Mensaje();
        mensaje.setIdMensaje(rs.getInt("idMensaje"));
        mensaje.setMensaje(rs.getString("mensaje"));
        mensaje.setFechaEnvio(rs.getTimestamp("fechaEnvio").toLocalDateTime());

        Evento evento = new Evento();
        evento.setIdEvento(rs.getInt("idEvento"));
        evento.setTitulo(rs.getString("eventoTitulo"));
        mensaje.setEvento(evento);

        User usuario = new User();
        usuario.setIdUsuario(rs.getInt("idUsuario"));
        usuario.setNombre(rs.getString("usuarioNombre"));
        mensaje.setUsuario(usuario);

        Invitado invitado = new Invitado();
        invitado.setIdInvitado(rs.getInt("idInvitado"));
        invitado.setNombre(rs.getString("invitadoNombre"));
        mensaje.setInvitado(invitado);

        return mensaje;
    }
}