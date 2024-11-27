package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.Evento;
import com.eventplanningsystem.model.TipoEvento;
import com.eventplanningsystem.model.User;
import com.eventplanningsystem.service.TipoEventoService;
import com.eventplanningsystem.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;

public class EventoDialogController {
    @FXML private TextField tituloField;
    @FXML private TextArea descripcionArea;
    @FXML private DatePicker fechaInicioPicker;
    @FXML private ComboBox<Integer> horaInicioPicker;
    @FXML private ComboBox<Integer> minutoInicioPicker;
    @FXML private DatePicker fechaFinPicker;
    @FXML private ComboBox<Integer> horaFinPicker;
    @FXML private ComboBox<Integer> minutoFinPicker;
    @FXML private TextField ubicacionField;
    @FXML private ComboBox<User> organizadorComboBox;
    @FXML private ComboBox<TipoEvento> tipoEventoComboBox;
    @FXML private ComboBox<String> estadoComboBox;

    private TipoEventoService tipoEventoService;
    private UserService userService;
    private Evento evento;
    private boolean saveClicked = false;

    @FXML
    private void initialize() {
        tipoEventoService = new TipoEventoService();
        userService = new UserService();
        loadTiposEvento();
        loadOrganizadores();
        setupEstadoComboBox();
        setupTimePickers();
        
        tipoEventoComboBox.setConverter(new StringConverter<TipoEvento>() {
            @Override
            public String toString(TipoEvento tipoEvento) {
                return tipoEvento != null ? tipoEvento.getNombreTipo() : "";
            }

            @Override
            public TipoEvento fromString(String string) {
                return tipoEventoComboBox.getItems().stream()
                        .filter(tipoEvento -> tipoEvento.getNombreTipo().equals(string))
                        .findFirst().orElse(null);
            }
        });

        organizadorComboBox.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                return user != null ? user.getNombre() : "";
            }

            @Override
            public User fromString(String string) {
                return organizadorComboBox.getItems().stream()
                        .filter(user -> user.getNombre().equals(string))
                        .findFirst().orElse(null);
            }
        });
    }

    private void loadTiposEvento() {
        try {
            List<TipoEvento> tiposEvento = tipoEventoService.getAllTiposEvento();
            tipoEventoComboBox.getItems().addAll(tiposEvento);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los tipos de evento: " + e.getMessage());
        }
    }

    private void loadOrganizadores() {
        try {
            List<User> allUsers = userService.getAllUsers();
            List<User> organizadores = allUsers.stream()
                .filter(user -> "organizador".equals(user.getTipoUsuario()))
                .toList();
            organizadorComboBox.getItems().addAll(organizadores);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los organizadores: " + e.getMessage());
        }
    }

    private void setupEstadoComboBox() {
        estadoComboBox.getItems().addAll("borrador", "publicado", "activo", "en_progreso", "finalizado", "pospuesto", "cancelado");
    }

    private void setupTimePickers() {
        horaInicioPicker.getItems().addAll(IntStream.rangeClosed(0, 23).boxed().toList());
        minutoInicioPicker.getItems().addAll(IntStream.rangeClosed(0, 59).boxed().toList());
        horaFinPicker.getItems().addAll(IntStream.rangeClosed(0, 23).boxed().toList());
        minutoFinPicker.getItems().addAll(IntStream.rangeClosed(0, 59).boxed().toList());
    }

    public void setEvento(Evento evento) {
        this.evento = evento;

        if (evento != null) {
            tituloField.setText(evento.getTitulo());
            descripcionArea.setText(evento.getDescripcion());
            if (evento.getFechaInicio() != null) {
                fechaInicioPicker.setValue(evento.getFechaInicio().toLocalDate());
                horaInicioPicker.setValue(evento.getFechaInicio().getHour());
                minutoInicioPicker.setValue(evento.getFechaInicio().getMinute());
            }
            if (evento.getFechaFin() != null) {
                fechaFinPicker.setValue(evento.getFechaFin().toLocalDate());
                horaFinPicker.setValue(evento.getFechaFin().getHour());
                minutoFinPicker.setValue(evento.getFechaFin().getMinute());
            }
            ubicacionField.setText(evento.getUbicacion());
            organizadorComboBox.setValue(evento.getOrganizador());
            tipoEventoComboBox.setValue(evento.getTipoEvento());
            estadoComboBox.setValue(evento.getEstado());
        } else {
            // Si es un nuevo evento, inicializamos con valores por defecto
            LocalDateTime now = LocalDateTime.now();
            fechaInicioPicker.setValue(now.toLocalDate());
            horaInicioPicker.setValue(now.getHour());
            minutoInicioPicker.setValue(now.getMinute());
            estadoComboBox.setValue("borrador");
        }
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (evento == null) {
                evento = new Evento();
            }
            evento.setTitulo(tituloField.getText());
            evento.setDescripcion(descripcionArea.getText());
            evento.setFechaInicio(LocalDateTime.of(
                fechaInicioPicker.getValue(),
                LocalTime.of(horaInicioPicker.getValue(), minutoInicioPicker.getValue())
            ));
            if (fechaFinPicker.getValue() != null) {
                evento.setFechaFin(LocalDateTime.of(
                    fechaFinPicker.getValue(),
                    LocalTime.of(horaFinPicker.getValue(), minutoFinPicker.getValue())
                ));
            }
            evento.setUbicacion(ubicacionField.getText());
            evento.setOrganizador(organizadorComboBox.getValue());
            evento.setTipoEvento(tipoEventoComboBox.getValue());
            evento.setEstado(estadoComboBox.getValue());

            saveClicked = true;
            closeDialog();
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        ((Stage) tituloField.getScene().getWindow()).close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (tituloField.getText() == null || tituloField.getText().trim().isEmpty()) {
            errorMessage += "Título no válido\n";
        }
        if (fechaInicioPicker.getValue() == null || horaInicioPicker.getValue() == null || minutoInicioPicker.getValue() == null) {
            errorMessage += "Fecha y hora de inicio no válidas\n";
        }
        if (organizadorComboBox.getValue() == null) {
            errorMessage += "Organizador no seleccionado\n";
        }
        if (tipoEventoComboBox.getValue() == null) {
            errorMessage += "Tipo de evento no seleccionado\n";
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

    public Evento getEvento() {
        return evento;
    }
}