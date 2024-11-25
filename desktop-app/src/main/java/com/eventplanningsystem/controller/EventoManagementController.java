package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.Evento;
import com.eventplanningsystem.service.EventoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventoManagementController {

    @FXML private TextField searchField;
    @FXML private TableView<Evento> eventoTable;
    @FXML private TableColumn<Evento, Integer> idColumn;
    @FXML private TableColumn<Evento, String> tituloColumn;
    @FXML private TableColumn<Evento, String> fechaInicioColumn;
    @FXML private TableColumn<Evento, String> ubicacionColumn;
    @FXML private TableColumn<Evento, String> organizadorColumn;
    @FXML private TableColumn<Evento, String> tipoEventoColumn;
    @FXML private TableColumn<Evento, String> estadoColumn;
    @FXML private TableColumn<Evento, Void> actionsColumn;

    private EventoService eventoService;
    private ObservableList<Evento> eventoList;

    public void initialize() {
        eventoService = new EventoService();
        eventoList = FXCollections.observableArrayList();

        setupTableColumns();
        loadEventos();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idEvento"));
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        fechaInicioColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty(cellData.getValue().getFechaInicio().format(formatter));
        });
        ubicacionColumn.setCellValueFactory(new PropertyValueFactory<>("ubicacion"));
        organizadorColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getOrganizador().getNombre()));
        tipoEventoColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTipoEvento().getNombreTipo()));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        
        setupActionsColumn();
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Editar");
            private final Button deleteButton = new Button("Eliminar");
            private final HBox pane = new HBox(5, editButton, deleteButton);

            {
                editButton.getStyleClass().add("login-button");
                deleteButton.getStyleClass().add("login-button");
                
                editButton.setMaxWidth(Double.MAX_VALUE);
                deleteButton.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(editButton, javafx.scene.layout.Priority.ALWAYS);
                HBox.setHgrow(deleteButton, javafx.scene.layout.Priority.ALWAYS);

                editButton.setOnAction(event -> handleEditEvento(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeleteEvento(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadEventos() {
        try {
            List<Evento> eventos = eventoService.getAllEventos();
            eventoList.setAll(eventos);
            eventoTable.setItems(eventoList);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los eventos: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        ObservableList<Evento> filteredList = eventoList.filtered(evento ->
            evento.getTitulo().toLowerCase().contains(searchTerm) ||
            evento.getUbicacion().toLowerCase().contains(searchTerm) ||
            evento.getOrganizador().getNombre().toLowerCase().contains(searchTerm)
        );
        eventoTable.setItems(filteredList);
    }

    @FXML
    private void handleNewEvento() {
        Evento newEvento = new Evento();
        boolean saveClicked = showEventoDialog(newEvento, "Nuevo Evento");
        if (saveClicked) {
            try {
                eventoService.createEvento(newEvento);
                loadEventos();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo crear el evento: " + e.getMessage());
            }
        }
    }

    private void handleEditEvento(Evento evento) {
        boolean saveClicked = showEventoDialog(evento, "Editar Evento");

        if (saveClicked) {
            try {
                eventoService.updateEvento(evento);
                eventoTable.refresh();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo actualizar el evento: " + e.getMessage());
            }
        }
    }

    private boolean showEventoDialog(Evento evento, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/evento-dialog-view.fxml"));
            VBox dialogRoot = loader.load();
            
            dialogRoot.getStylesheets().addAll(eventoTable.getScene().getStylesheets());
            String themeClass = eventoTable.getScene().getRoot().getStyleClass().contains("dark") ? "dark" : "light";
            dialogRoot.getStyleClass().addAll("root", themeClass);

            Scene scene = new Scene(dialogRoot);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(eventoTable.getScene().getWindow());
            dialogStage.setScene(scene);

            EventoDialogController controller = loader.getController();
            controller.setEvento(evento);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el diálogo de evento: " + e.getMessage());
            return false;
        }
    }

    private void handleDeleteEvento(Evento evento) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro que desea eliminar este evento?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    eventoService.deleteEvento(evento.getIdEvento());
                    eventoList.remove(evento);
                } catch (SQLException e) {
                    showAlert("Error", "No se pudo eliminar el evento: " + e.getMessage());
                }
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}