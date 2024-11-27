package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class UserDialogController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> userTypeComboBox;
    @FXML private PasswordField passwordField;

    private User user;
    private boolean saveClicked = false;

    @FXML
    private void initialize() {
        userTypeComboBox.getItems().addAll("organizador", "gestor");
    }

    public void setUser(User user) {
        this.user = user;

        nameField.setText(user.getNombre());
        emailField.setText(user.getCorreoElectronico());
        phoneField.setText(user.getTelefono());
        userTypeComboBox.setValue(user.getTipoUsuario());
        // No establecemos la contraseña por razones de seguridad
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            user.setNombre(nameField.getText());
            user.setCorreoElectronico(emailField.getText());
            user.setTelefono(phoneField.getText());
            user.setTipoUsuario(userTypeComboBox.getValue());
            if (!passwordField.getText().isEmpty()) {
                user.setPassword(passwordField.getText());
            }

            saveClicked = true;
            closeDialog();
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        ((Stage) nameField.getScene().getWindow()).close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().isEmpty()) {
            errorMessage += "Nombre no válido\n";
        }
        if (emailField.getText() == null || emailField.getText().isEmpty()) {
            errorMessage += "Correo electrónico no válido\n";
        }
        if (userTypeComboBox.getValue() == null) {
            errorMessage += "Tipo de usuario no seleccionado\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campos Inválidos");
            alert.setHeaderText("Por favor, corrija los campos inválidos");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public User getUser() {
        return user;
    }
}