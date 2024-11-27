package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.Mensaje;
import com.eventplanningsystem.service.MensajeService;
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

public class MensajeManagementController {

    @FXML private TextField searchField;
    @FXML private TableView<Mensaje> mensajeTable;
    @FXML private TableColumn<Mensaje, Integer> idColumn;
    @FXML private TableColumn<Mensaje, String> eventoColumn;
    @FXML private TableColumn<Mensaje, String> usuarioColumn;
    @FXML private TableColumn<Mensaje, String> invitadoColumn;
    @FXML private TableColumn<Mensaje, String> mensajeColumn;
    @FXML private TableColumn<Mensaje, String> fechaEnvioColumn;
    @FXML private TableColumn<Mensaje, Void> actionsColumn;

    private MensajeService mensajeService;
    private ObservableList<Mensaje> mensajeList;

    public void initialize() {
        mensajeService = new MensajeService();
        mensajeList = FXCollections.observableArrayList();

        setupTableColumns();
        loadMensajes();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idMensaje"));
        eventoColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEvento().getTitulo()));
        usuarioColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getUsuario().getNombre()));
        invitadoColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getInvitado().getNombre()));
        mensajeColumn.setCellValueFactory(new PropertyValueFactory<>("mensaje"));
        fechaEnvioColumn.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new SimpleStringProperty(cellData.getValue().getFechaEnvio().format(formatter));
        });
        
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

                editButton.setOnAction(event -> handleEditMensaje(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeleteMensaje(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadMensajes() {
        try {
            List<Mensaje> mensajes = mensajeService.getAllMensajes();
            mensajeList.setAll(mensajes);
            mensajeTable.setItems(mensajeList);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los mensajes: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        ObservableList<Mensaje> filteredList = mensajeList.filtered(mensaje ->
            mensaje.getMensaje().toLowerCase().contains(searchTerm) ||
            mensaje.getEvento().getTitulo().toLowerCase().contains(searchTerm) ||
            mensaje.getUsuario().getNombre().toLowerCase().contains(searchTerm) ||
            mensaje.getInvitado().getNombre().toLowerCase().contains(searchTerm)
        );
        mensajeTable.setItems(filteredList);
    }

    @FXML
    private void handleNewMensaje() {
        Mensaje newMensaje = new Mensaje();
        boolean saveClicked = showMensajeDialog(newMensaje, "Nuevo Mensaje");
        if (saveClicked) {
            try {
                mensajeService.createMensaje(newMensaje);
                loadMensajes();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo crear el mensaje: " + e.getMessage());
            }
        }
    }

    private void handleEditMensaje(Mensaje mensaje) {
        boolean saveClicked = showMensajeDialog(mensaje, "Editar Mensaje");

        if (saveClicked) {
            try {
                mensajeService.updateMensaje(mensaje);
                mensajeTable.refresh();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo actualizar el mensaje: " + e.getMessage());
            }
        }
    }

    private void handleDeleteMensaje(Mensaje mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro que desea eliminar este mensaje?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    mensajeService.deleteMensaje(mensaje.getIdMensaje());
                    mensajeList.remove(mensaje);
                } catch (SQLException e) {
                    showAlert("Error", "No se pudo eliminar el mensaje: " + e.getMessage());
                }
            }
        });
    }

    private boolean showMensajeDialog(Mensaje mensaje, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mensaje-dialog-view.fxml"));
            VBox dialogRoot = loader.load();
            
            dialogRoot.getStylesheets().addAll(mensajeTable.getScene().getStylesheets());
            String themeClass = mensajeTable.getScene().getRoot().getStyleClass().contains("dark") ? "dark" : "light";
            dialogRoot.getStyleClass().addAll("root", themeClass);

            Scene scene = new Scene(dialogRoot);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mensajeTable.getScene().getWindow());
            dialogStage.setScene(scene);

            MensajeDialogController controller = loader.getController();
            controller.setMensaje(mensaje);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el diálogo de mensaje: " + e.getMessage());
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}