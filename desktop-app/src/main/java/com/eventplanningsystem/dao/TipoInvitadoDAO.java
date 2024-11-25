package com.eventplanningsystem.dao;

import com.eventplanningsystem.model.TipoInvitado;
import com.eventplanningsystem.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoInvitadoDAO {
    public TipoInvitado getTipoInvitadoById(int id) throws SQLException {
        String sql = "SELECT * FROM TipoInvitado WHERE idTipoInvitado = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTipoInvitado(rs);
                }
            }
        }
        return null;
    }

    public void createTipoInvitado(TipoInvitado tipoInvitado) throws SQLException {
        String sql = "INSERT INTO TipoInvitado (nombreTipo, descripcion) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, tipoInvitado.getNombreTipo());
            pstmt.setString(2, tipoInvitado.getDescripcion());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tipoInvitado.setIdTipoInvitado(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateTipoInvitado(TipoInvitado tipoInvitado) throws SQLException {
        String sql = "UPDATE TipoInvitado SET nombreTipo = ?, descripcion = ? WHERE idTipoInvitado = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipoInvitado.getNombreTipo());
            pstmt.setString(2, tipoInvitado.getDescripcion());
            pstmt.setInt(3, tipoInvitado.getIdTipoInvitado());
            pstmt.executeUpdate();
        }
    }

    public void deleteTipoInvitado(int id) throws SQLException {
        String sql = "DELETE FROM TipoInvitado WHERE idTipoInvitado = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<TipoInvitado> getAllTiposInvitado() throws SQLException {
        List<TipoInvitado> tiposInvitado = new ArrayList<>();
        String sql = "SELECT * FROM TipoInvitado";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tiposInvitado.add(mapResultSetToTipoInvitado(rs));
            }
        }
        return tiposInvitado;
    }

    private TipoInvitado mapResultSetToTipoInvitado(ResultSet rs) throws SQLException {
        TipoInvitado tipoInvitado = new TipoInvitado();
        tipoInvitado.setIdTipoInvitado(rs.getInt("idTipoInvitado"));
        tipoInvitado.setNombreTipo(rs.getString("nombreTipo"));
        tipoInvitado.setDescripcion(rs.getString("descripcion"));
        return tipoInvitado;
    }
}