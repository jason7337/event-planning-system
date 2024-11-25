package com.eventplanningsystem.dao;

import com.eventplanningsystem.model.TipoEvento;
import com.eventplanningsystem.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoEventoDAO {
    public TipoEvento getTipoEventoById(int id) throws SQLException {
        String sql = "SELECT * FROM TiposEvento WHERE idTipoEvento = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTipoEvento(rs);
                }
            }
        }
        return null;
    }

    public void createTipoEvento(TipoEvento tipoEvento) throws SQLException {
        String sql = "INSERT INTO TiposEvento (nombreTipo, descripcion) VALUES (?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, tipoEvento.getNombreTipo());
            pstmt.setString(2, tipoEvento.getDescripcion());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tipoEvento.setIdTipoEvento(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateTipoEvento(TipoEvento tipoEvento) throws SQLException {
        String sql = "UPDATE TiposEvento SET nombreTipo = ?, descripcion = ? WHERE idTipoEvento = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipoEvento.getNombreTipo());
            pstmt.setString(2, tipoEvento.getDescripcion());
            pstmt.setInt(3, tipoEvento.getIdTipoEvento());
            pstmt.executeUpdate();
        }
    }

    public void deleteTipoEvento(int id) throws SQLException {
        String sql = "DELETE FROM TiposEvento WHERE idTipoEvento = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<TipoEvento> getAllTiposEvento() throws SQLException {
        List<TipoEvento> tiposEvento = new ArrayList<>();
        String sql = "SELECT * FROM TiposEvento";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tiposEvento.add(mapResultSetToTipoEvento(rs));
            }
        }
        return tiposEvento;
    }

    private TipoEvento mapResultSetToTipoEvento(ResultSet rs) throws SQLException {
        TipoEvento tipoEvento = new TipoEvento();
        tipoEvento.setIdTipoEvento(rs.getInt("idTipoEvento"));
        tipoEvento.setNombreTipo(rs.getString("nombreTipo"));
        tipoEvento.setDescripcion(rs.getString("descripcion"));
        return tipoEvento;
    }
}