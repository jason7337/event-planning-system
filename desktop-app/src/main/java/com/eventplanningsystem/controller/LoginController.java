package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.User;
import com.eventplanningsystem.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;
    @FXML private StackPane rootPane;

    private UserService userService;
    private boolean isDarkTheme = false;

    public LoginController() {
        this.userService = new UserService();
    }

    @FXML
    private void handleLogin() {
        String correo = txtCorreo.getText();
        String password = txtPassword.getText();

        try {
            User user = userService.authenticateUser(correo, password);
            if (user != null) {
                System.out.println("Login successful for user: " + user.getNombre());
                loadMainView();
            } else {
                lblError.setText("Correo o contraseña incorrectos");
                System.out.println("Login failed for email: " + correo);
            }
        } catch (SQLException e) {
            lblError.setText("Error al iniciar sesión: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) txtCorreo.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Sistema de Gestión de Eventos");
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            lblError.setText("Error al cargar la vista principal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        if (isDarkTheme) {
            rootPane.getStyleClass().add("dark");
        } else {
            rootPane.getStyleClass().remove("dark");
        }
    }
}