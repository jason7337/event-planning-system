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
        if (user != null) {
            String storedHash = user.getPassword();
            System.out.println("Stored hash: " + storedHash);
            System.out.println("Provided password: " + password);
            
            // Extraer el salt del hash almacenado
            String salt = storedHash.substring(0, 29);
            System.out.println("Extracted salt: " + salt);
            
            // Generar un nuevo hash con la contraseña proporcionada y el salt extraído
            String newHash = BCrypt.hashpw(password, salt);
            System.out.println("Generated hash: " + newHash);
            
            if (storedHash.equals(newHash)) {
                System.out.println("Password verified successfully");
                return user;
            } else {
                System.out.println("Password verification failed");
            }
        } else {
            System.out.println("User not found for email: " + correo);
        }
        return null;
    }

    public User getUserById(int id) throws SQLException {
        return userDAO.getUserById(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    public void createUser(User user) throws SQLException {
        user.setPassword(hashPassword(user.getPassword()));
        userDAO.createUser(user);
    }

    public void updateUser(User user) throws SQLException {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(hashPassword(user.getPassword()));
        }
        userDAO.updateUser(user);
    }

    public void deleteUser(int id) throws SQLException {
        userDAO.deleteUser(id);
    }

    // Este método se puede usar para generar nuevos hashes si es necesario
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }
}