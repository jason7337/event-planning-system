package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.TipoInvitado;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class TipoInvitadoDialogController {
    @FXML private TextField nombreTipoField;
    @FXML private TextArea descripcionArea;

    private TipoInvitado tipoInvitado;
    private boolean saveClicked = false;

    @FXML
    private void initialize() {
        // Inicialización si es necesaria
    }

    public void setTipoInvitado(TipoInvitado tipoInvitado) {
        this.tipoInvitado = tipoInvitado;

        nombreTipoField.setText(tipoInvitado.getNombreTipo());
        descripcionArea.setText(tipoInvitado.getDescripcion());
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            tipoInvitado.setNombreTipo(nombreTipoField.getText());
            tipoInvitado.setDescripcion(descripcionArea.getText());

            saveClicked = true;
            closeDialog();
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        ((Stage) nombreTipoField.getScene().getWindow()).close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nombreTipoField.getText() == null || nombreTipoField.getText().isEmpty()) {
            errorMessage += "Nombre de tipo de invitado no válido\n";
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

    public TipoInvitado getTipoInvitado() {
        return tipoInvitado;
    }
}