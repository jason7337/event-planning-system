package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.ParticipanteEvento;
import com.eventplanningsystem.model.Evento;
import com.eventplanningsystem.model.Invitado;
import com.eventplanningsystem.service.EventoService;
import com.eventplanningsystem.service.InvitadoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.List;

public class ParticipanteEventoDialogController {
    @FXML private ComboBox<Evento> eventoComboBox;
    @FXML private ComboBox<Invitado> invitadoComboBox;
    @FXML private ComboBox<String> estadoComboBox;

    private EventoService eventoService;
    private InvitadoService invitadoService;
    private ParticipanteEvento participanteEvento;
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
        estadoComboBox.getItems().addAll("confirmado", "pendiente", "rechazado");
    }

    public void setParticipanteEvento(ParticipanteEvento participanteEvento) {
        this.participanteEvento = participanteEvento;

        if (participanteEvento != null) {
            eventoComboBox.setValue(participanteEvento.getEvento());
            invitadoComboBox.setValue(participanteEvento.getInvitado());
            estadoComboBox.setValue(participanteEvento.getEstado());
        } else {
            estadoComboBox.setValue("pendiente");
        }
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (participanteEvento == null) {
                participanteEvento = new ParticipanteEvento();
            }
            participanteEvento.setEvento(eventoComboBox.getValue());
            participanteEvento.setInvitado(invitadoComboBox.getValue());
            participanteEvento.setEstado(estadoComboBox.getValue());

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

    public ParticipanteEvento getParticipanteEvento() {
        return participanteEvento;
    }
}