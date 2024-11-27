package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.Correo;
import com.eventplanningsystem.model.Evento;
import com.eventplanningsystem.model.User;
import com.eventplanningsystem.model.Invitado;
import com.eventplanningsystem.service.EventoService;
import com.eventplanningsystem.service.UserService;
import com.eventplanningsystem.service.InvitadoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class CorreoDialogController {
    @FXML private ComboBox<Evento> eventoComboBox;
    @FXML private ComboBox<User> usuarioComboBox;
    @FXML private ComboBox<Invitado> invitadoComboBox;
    @FXML private TextField asuntoField;
    @FXML private TextArea mensajeArea;

    private EventoService eventoService;
    private UserService userService;
    private InvitadoService invitadoService;
    private Correo correo;
    private boolean saveClicked = false;

    @FXML
    private void initialize() {
        eventoService = new EventoService();
        userService = new UserService();
        invitadoService = new InvitadoService();
        loadEventos();
        loadUsuarios();
        loadInvitados();
        
        setupComboBoxConverters();
    }

    private void loadEventos() {
        try {
            List<Evento> eventos = eventoService.getAllEventos();
            eventoComboBox.getItems().addAll(eventos);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los eventos: " + e.getMessage());
        }
    }

    private void loadUsuarios() {
        try {
            List<User> usuarios = userService.getAllUsers();
            usuarioComboBox.getItems().addAll(usuarios);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los usuarios: " + e.getMessage());
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

    private void setupComboBoxConverters() {
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

        usuarioComboBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user != null ? user.getNombre() : "";
            }

            @Override
            public User fromString(String string) {
                return usuarioComboBox.getItems().stream()
                        .filter(user -> user.getNombre().equals(string))
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

    public void setCorreo(Correo correo) {
        this.correo = correo;

        if (correo != null) {
            eventoComboBox.setValue(correo.getEvento());
            usuarioComboBox.setValue(correo.getUsuario());
            invitadoComboBox.setValue(correo.getInvitado());
            asuntoField.setText(correo.getAsunto());
            mensajeArea.setText(correo.getMensaje());
        }
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (correo == null) {
                correo = new Correo();
            }
            correo.setEvento(eventoComboBox.getValue());
            correo.setUsuario(usuarioComboBox.getValue());
            correo.setInvitado(invitadoComboBox.getValue());
            correo.setAsunto(asuntoField.getText());
            correo.setMensaje(mensajeArea.getText());
            correo.setFechaEnvio(LocalDateTime.now());

            saveClicked = true;
            closeDialog();
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        ((Stage) asuntoField.getScene().getWindow()).close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (eventoComboBox.getValue() == null) {
            errorMessage += "Evento no seleccionado\n";
        }
        if (usuarioComboBox.getValue() == null) {
            errorMessage += "Usuario no seleccionado\n";
        }
        if (invitadoComboBox.getValue() == null) {
            errorMessage += "Invitado no seleccionado\n";
        }
        if (asuntoField.getText() == null || asuntoField.getText().trim().isEmpty()) {
            errorMessage += "Asunto no válido\n";
        }
        if (mensajeArea.getText() == null || mensajeArea.getText().trim().isEmpty()) {
            errorMessage += "Mensaje no válido\n";
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

    public Correo getCorreo() {
        return correo;
    }
}