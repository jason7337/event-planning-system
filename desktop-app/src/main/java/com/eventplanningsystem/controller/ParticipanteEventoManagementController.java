package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.ParticipanteEvento;
import com.eventplanningsystem.service.ParticipanteEventoService;
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
import java.util.List;

public class ParticipanteEventoManagementController {

    @FXML private TextField searchField;
    @FXML private TableView<ParticipanteEvento> participanteEventoTable;
    @FXML private TableColumn<ParticipanteEvento, Integer> idColumn;
    @FXML private TableColumn<ParticipanteEvento, String> eventoColumn;
    @FXML private TableColumn<ParticipanteEvento, String> invitadoColumn;
    @FXML private TableColumn<ParticipanteEvento, String> estadoColumn;
    @FXML private TableColumn<ParticipanteEvento, Void> actionsColumn;

    private ParticipanteEventoService participanteEventoService;
    private ObservableList<ParticipanteEvento> participanteEventoList;

    public void initialize() {
        participanteEventoService = new ParticipanteEventoService();
        participanteEventoList = FXCollections.observableArrayList();

        setupTableColumns();
        loadParticipantesEvento();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idParticipante"));
        eventoColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEvento().getTitulo()));
        invitadoColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getInvitado().getNombre()));
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

                editButton.setOnAction(event -> handleEditParticipanteEvento(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeleteParticipanteEvento(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadParticipantesEvento() {
        try {
            List<ParticipanteEvento> participantesEvento = participanteEventoService.getAllParticipantesEvento();
            participanteEventoList.setAll(participantesEvento);
            participanteEventoTable.setItems(participanteEventoList);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los participantes del evento: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        ObservableList<ParticipanteEvento> filteredList = participanteEventoList.filtered(participanteEvento ->
            participanteEvento.getEvento().getTitulo().toLowerCase().contains(searchTerm) ||
            participanteEvento.getInvitado().getNombre().toLowerCase().contains(searchTerm) ||
            participanteEvento.getEstado().toLowerCase().contains(searchTerm)
        );
        participanteEventoTable.setItems(filteredList);
    }

    @FXML
    private void handleNewParticipanteEvento() {
        ParticipanteEvento newParticipanteEvento = new ParticipanteEvento();
        boolean saveClicked = showParticipanteEventoDialog(newParticipanteEvento, "Nuevo Participante de Evento");
        if (saveClicked) {
            try {
                participanteEventoService.createParticipanteEvento(newParticipanteEvento);
                loadParticipantesEvento();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo crear el participante del evento: " + e.getMessage());
            }
        }
    }

    private void handleEditParticipanteEvento(ParticipanteEvento participanteEvento) {
        boolean saveClicked = showParticipanteEventoDialog(participanteEvento, "Editar Participante de Evento");

        if (saveClicked) {
            try {
                participanteEventoService.updateParticipanteEvento(participanteEvento);
                participanteEventoTable.refresh();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo actualizar el participante del evento: " + e.getMessage());
            }
        }
    }

    private boolean showParticipanteEventoDialog(ParticipanteEvento participanteEvento, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/participante-evento-dialog-view.fxml"));
            VBox dialogRoot = loader.load();
            
            dialogRoot.getStylesheets().addAll(participanteEventoTable.getScene().getStylesheets());
            String themeClass = participanteEventoTable.getScene().getRoot().getStyleClass().contains("dark") ? "dark" : "light";
            dialogRoot.getStyleClass().addAll("root", themeClass);

            Scene scene = new Scene(dialogRoot);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(participanteEventoTable.getScene().getWindow());
            dialogStage.setScene(scene);

            ParticipanteEventoDialogController controller = loader.getController();
            controller.setParticipanteEvento(participanteEvento);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el diálogo de participante de evento: " + e.getMessage());
            return false;
        }
    }

    private void handleDeleteParticipanteEvento(ParticipanteEvento participanteEvento) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro que desea eliminar este participante del evento?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    participanteEventoService.deleteParticipanteEvento(participanteEvento.getIdParticipante());
                    participanteEventoList.remove(participanteEvento);
                } catch (SQLException e) {
                    showAlert("Error", "No se pudo eliminar el participante del evento: " + e.getMessage());
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