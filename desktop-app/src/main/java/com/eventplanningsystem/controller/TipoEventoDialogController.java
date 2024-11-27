package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.TipoEvento;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class TipoEventoDialogController {
    @FXML private TextField nombreTipoField;
    @FXML private TextArea descripcionArea;

    private TipoEvento tipoEvento;
    private boolean saveClicked = false;

    @FXML
    private void initialize() {
        // Inicialización si es necesaria
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;

        nombreTipoField.setText(tipoEvento.getNombreTipo());
        descripcionArea.setText(tipoEvento.getDescripcion());
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            tipoEvento.setNombreTipo(nombreTipoField.getText());
            tipoEvento.setDescripcion(descripcionArea.getText());

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
            errorMessage += "Nombre de tipo de evento no válido\n";
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

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }
}