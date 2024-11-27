package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.Invitado;
import com.eventplanningsystem.model.TipoInvitado;
import com.eventplanningsystem.service.TipoInvitadoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.List;

public class InvitadoDialogController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<TipoInvitado> tipoInvitadoComboBox;
    private TipoInvitadoService tipoInvitadoService;
    private Invitado invitado;
    private boolean saveClicked = false;

    @FXML
    private void initialize() {
        tipoInvitadoService = new TipoInvitadoService();
        loadTiposInvitado();
        
        tipoInvitadoComboBox.setConverter(new StringConverter<TipoInvitado>() {
            @Override
            public String toString(TipoInvitado tipoInvitado) {
                return tipoInvitado != null ? tipoInvitado.getNombreTipo() : "";
            }

            @Override
            public TipoInvitado fromString(String string) {
                return tipoInvitadoComboBox.getItems().stream()
                        .filter(tipoInvitado -> tipoInvitado.getNombreTipo().equals(string))
                        .findFirst().orElse(null);
            }
        });
    }

    private void loadTiposInvitado() {
        try {
            List<TipoInvitado> tiposInvitado = tipoInvitadoService.getAllTiposInvitado();
            tipoInvitadoComboBox.getItems().addAll(tiposInvitado);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los tipos de invitado: " + e.getMessage());
        }
    }

    public void setInvitado(Invitado invitado) {
        this.invitado = invitado;

        nameField.setText(invitado.getNombre());
        emailField.setText(invitado.getCorreoElectronico());
        phoneField.setText(invitado.getTelefono());
        tipoInvitadoComboBox.setValue(invitado.getTipoInvitado());
        // No establecemos la contraseña por razones de seguridad
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            invitado.setNombre(nameField.getText());
            invitado.setCorreoElectronico(emailField.getText());
            invitado.setTelefono(phoneField.getText());
            invitado.setTipoInvitado(tipoInvitadoComboBox.getValue());
            if (!passwordField.getText().isEmpty()) {
                invitado.setPassword(passwordField.getText());
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
        if (tipoInvitadoComboBox.getValue() == null) {
            errorMessage += "Tipo de invitado no seleccionado\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert("Campos Inválidos", errorMessage);
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Por favor, corrija los campos inválidos");
        alert.setContentText(content);
        alert.showAndWait();
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public Invitado getInvitado() {
        return invitado;
    }
}