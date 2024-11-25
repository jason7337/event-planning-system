package com.eventplanningsystem.dao;

import com.eventplanningsystem.model.User;
import com.eventplanningsystem.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM Usuarios WHERE idUsuario = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null;
    }

    public User getUserByCorreo(String correo) throws SQLException {
        String sql = "SELECT * FROM Usuarios WHERE correoElectronico = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, correo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null;
    }

    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO Usuarios (nombre, correoElectronico, contraseña, telefono, tipoUsuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getNombre());
            pstmt.setString(2, user.getCorreoElectronico());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getTelefono());
            pstmt.setString(5, user.getTipoUsuario());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setIdUsuario(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE Usuarios SET nombre = ?, correoElectronico = ?, telefono = ?, tipoUsuario = ? WHERE idUsuario = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getNombre());
            pstmt.setString(2, user.getCorreoElectronico());
            pstmt.setString(3, user.getTelefono());
            pstmt.setString(4, user.getTipoUsuario());
            pstmt.setInt(5, user.getIdUsuario());
            pstmt.executeUpdate();
        }
    }

    public void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM Usuarios WHERE idUsuario = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        }
        return users;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setIdUsuario(rs.getInt("idUsuario"));
        user.setNombre(rs.getString("nombre"));
        user.setCorreoElectronico(rs.getString("correoElectronico"));
        user.setPassword(rs.getString("contraseña"));
        user.setTelefono(rs.getString("telefono"));
        user.setTipoUsuario(rs.getString("tipoUsuario"));
        user.setFechaRegistro(rs.getString("fechaRegistro"));
        return user;
    }
}