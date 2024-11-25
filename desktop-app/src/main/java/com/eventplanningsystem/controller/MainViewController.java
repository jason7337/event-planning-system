package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.User;
import com.eventplanningsystem.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.SQLException;

public class MainViewController {
    @FXML private BorderPane rootPane;
    @FXML private Button menuToggle;
    @FXML private Button themeToggle;
    @FXML private Label userNameLabel;
    @FXML private GridPane userInfoGrid;
    @FXML private VBox sidebar;

    private UserService userService;
    private boolean isDarkTheme = false;

    public void initialize() {
        userService = new UserService();
        setupMenuToggle();
        setupThemeToggle();
        loadUserInfo();
    }

    private void setupMenuToggle() {
        menuToggle.setOnAction(event -> {
            double sidebarWidth = sidebar.getPrefWidth();
            if (sidebar.getTranslateX() == 0) {
                sidebar.setTranslateX(-sidebarWidth);
            } else {
                sidebar.setTranslateX(0);
            }
        });
    }

    private void setupThemeToggle() {
        themeToggle.setOnAction(event -> {
            isDarkTheme = !isDarkTheme;
            setTheme(isDarkTheme);
            FontIcon icon = (FontIcon) themeToggle.getGraphic();
            icon.setIconLiteral(isDarkTheme ? "fas-sun" : "fas-moon");
        });
    }

    private void loadUserInfo() {
        try {
            // Asume que el ID del usuario está almacenado en algún lugar después del inicio de sesión
            int userId = 1; // Esto debería venir de la sesión del usuario
            User user = userService.getUserById(userId);
            if (user != null) {
                userNameLabel.setText(user.getNombre());
                displayUserInfo(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }

    private void displayUserInfo(User user) {
        userInfoGrid.getChildren().clear();
        addInfoRow("Nombre", user.getNombre(), 0);
        addInfoRow("Correo", user.getCorreoElectronico(), 1);
        addInfoRow("Teléfono", user.getTelefono(), 2);
        addInfoRow("Tipo de Usuario", user.getTipoUsuario(), 3);
        addInfoRow("Fecha de Registro", user.getFechaRegistro(), 4);
    }

    private void addInfoRow(String label, String value, int row) {
        userInfoGrid.add(new Label(label + ":"), 0, row);
        userInfoGrid.add(new Label(value), 1, row);
    }

    public void setTheme(boolean isDark) {
        if (isDark) {
            rootPane.getStyleClass().add("dark");
        } else {
            rootPane.getStyleClass().remove("dark");
        }
    }
}