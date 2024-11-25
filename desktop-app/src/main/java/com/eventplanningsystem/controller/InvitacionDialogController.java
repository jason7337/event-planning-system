package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.Invitacion;
import com.eventplanningsystem.model.Evento;
import com.eventplanningsystem.model.Invitado;
import com.eventplanningsystem.service.EventoService;
import com.eventplanningsystem.service.InvitadoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class InvitacionDialogController {
    @FXML private ComboBox<Evento> eventoComboBox;
    @FXML private ComboBox<Invitado> invitadoComboBox;
    @FXML private ComboBox<String> estadoComboBox;
    @FXML private DatePicker fechaRespuestaPicker;

    private EventoService eventoService;
    private InvitadoService invitadoService;
    private Invitacion invitacion;
    private boolean saveClicked = false;

    @FXML
    private void initialize() {
        eventoService = new EventoService();
        invitadoService = new InvitadoService();
        loadEventos();
        loadInvitados();
        setupEstadoComboBox();
        
        eventoComboBox.setConverter(new StringConverter<Evento>() {
            @Override
            public String toString(Evento evento) {
                return evento != null ? evento.getTitulo() : "";
            }

            @Override
            public Evento fromString(String string) {
                return eventoComboBox.getItems().stream()
                        .filter(evento -> evento.getTitulo().equals(string))
                        .findFirst().orElse(null);
            }
        });

        invitadoComboBox.setConverter(new StringConverter<Invitado>() {
            @Override
            public String toString(Invitado invitado) {
                return invitado != null ? invitado.getNombre() : "";
            }

            @Override
            public Invitado fromString(String string) {
                return invitadoComboBox.getItems().stream()
                        .filter(invitado -> invitado.getNombre().equals(string))
                        .findFirst().orElse(null);
            }
        });
    }

    private void loadEventos() {
        try {
            List<Evento> eventos = eventoService.getAllEventos();
            eventoComboBox.getItems().addAll(eventos);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los eventos: " + e.getMessage());
        }
    }

    private void loadInvitados() {
        try {
            List<Invitado> invitados = invitadoService.getAllInvitados();
            invitadoComboBox.getItems().addAll(invitados);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los invitados: " + e.getMessage());
        }
    }

    private void setupEstadoComboBox() {
        estadoComboBox.getItems().addAll("pendiente", "aceptada", "rechazada");
    }

    public void setInvitacion(Invitacion invitacion) {
        this.invitacion = invitacion;

        if (invitacion != null) {
            eventoComboBox.setValue(invitacion.getEvento());
            invitadoComboBox.setValue(invitacion.getInvitado());
            estadoComboBox.setValue(invitacion.getEstado());
            if (invitacion.getFechaRespuesta() != null) {
                fechaRespuestaPicker.setValue(invitacion.getFechaRespuesta().toLocalDate());
            }
        } else {
            estadoComboBox.setValue("pendiente");
        }
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (invitacion == null) {
                invitacion = new Invitacion();
            }
            invitacion.setEvento(eventoComboBox.getValue());
            invitacion.setInvitado(invitadoComboBox.getValue());
            invitacion.setEstado(estadoComboBox.getValue());
            if (fechaRespuestaPicker.getValue() != null) {
                invitacion.setFechaRespuesta(LocalDateTime.of(fechaRespuestaPicker.getValue(), LocalDateTime.now().toLocalTime()));
            } else {
                invitacion.setFechaRespuesta(null);
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
        ((Stage) eventoComboBox.getScene().getWindow()).close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (eventoComboBox.getValue() == null) {
            errorMessage += "Evento no seleccionado\n";
        }
        if (invitadoComboBox.getValue() == null) {
            errorMessage += "Invitado no seleccionado\n";
        }
        if (estadoComboBox.getValue() == null) {
            errorMessage += "Estado no seleccionado\n";
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

    public Invitacion getInvitacion() {
        return invitacion;
    }
}