package com.eventplanningsystem.service;

import com.eventplanningsystem.dao.UserDAO;
import com.eventplanningsystem.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public User authenticateUser(String correo, String password) throws SQLException {
        User user = userDAO.getUserByCorreo(correo);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User getUserById(int id) throws SQLException {
        return userDAO.getUserById(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }
}