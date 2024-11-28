package com.eventplanningsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.eventplanningsystem.model.Invitado;
import com.eventplanningsystem.model.TipoInvitado;
import com.eventplanningsystem.util.DatabaseUtil;

public class InvitadoDAO {
    public Invitado getInvitadoById(int id) throws SQLException {
        String sql = "SELECT i.*, ti.nombreTipo, ti.descripcion FROM Invitados i " +
                     "JOIN TipoInvitado ti ON i.idTipoInvitado = ti.idTipoInvitado " +
                     "WHERE i.idInvitado = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvitado(rs);
                }
            }
        }
        return null;
    }

    public Invitado getInvitadoByCorreo(String correo) throws SQLException {
        String sql = "SELECT i.*, ti.nombreTipo, ti.descripcion FROM Invitados i " +
                     "JOIN TipoInvitado ti ON i.idTipoInvitado = ti.idTipoInvitado " +
                     "WHERE i.correoElectronico = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvitado(rs);
                }
            }
        }
        return null;
    }

    public void createInvitado(Invitado invitado) throws SQLException {
        String sql = "INSERT INTO Invitados (nombre, correoElectronico, telefono, idTipoInvitado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, invitado.getNombre());
            pstmt.setString(2, invitado.getCorreoElectronico());
            pstmt.setString(4, invitado.getTelefono());
            pstmt.setInt(5, invitado.getTipoInvitado().getIdTipoInvitado());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    invitado.setIdInvitado(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateInvitado(Invitado invitado) throws SQLException {
        String sql = "UPDATE Invitados SET nombre = ?, correoElectronico = ?, telefono = ?, idTipoInvitado = ? WHERE idInvitado = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, invitado.getNombre());
            pstmt.setString(2, invitado.getCorreoElectronico());
            pstmt.setString(3, invitado.getTelefono());
            pstmt.setInt(4, invitado.getTipoInvitado().getIdTipoInvitado());
            pstmt.setInt(5, invitado.getIdInvitado());
            pstmt.executeUpdate();
        }
    }

    public void deleteInvitado(int id) throws SQLException {
        String sql = "DELETE FROM Invitados WHERE idInvitado = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Invitado> getAllInvitados() throws SQLException {
        List<Invitado> invitados = new ArrayList<>();
        String sql = "SELECT i.*, ti.nombreTipo, ti.descripcion FROM Invitados i " +
                     "JOIN TipoInvitado ti ON i.idTipoInvitado = ti.idTipoInvitado";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                invitados.add(mapResultSetToInvitado(rs));
            }
        }
        return invitados;
    }

    private Invitado mapResultSetToInvitado(ResultSet rs) throws SQLException {
        Invitado invitado = new Invitado();
        invitado.setIdInvitado(rs.getInt("idInvitado"));
        invitado.setNombre(rs.getString("nombre"));
        invitado.setCorreoElectronico(rs.getString("correoElectronico"));
        invitado.setTelefono(rs.getString("telefono"));
        
        TipoInvitado tipoInvitado = new TipoInvitado();
        tipoInvitado.setIdTipoInvitado(rs.getInt("idTipoInvitado"));
        tipoInvitado.setNombreTipo(rs.getString("nombreTipo"));
        tipoInvitado.setDescripcion(rs.getString("descripcion"));
        
        invitado.setTipoInvitado(tipoInvitado);
        
        return invitado;
    }
}