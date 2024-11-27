package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.Correo;
import com.eventplanningsystem.service.CorreoService;
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

public class CorreoManagementController {

    @FXML private TextField searchField;
    @FXML private TableView<Correo> correoTable;
    @FXML private TableColumn<Correo, Integer> idColumn;
    @FXML private TableColumn<Correo, String> eventoColumn;
    @FXML private TableColumn<Correo, String> usuarioColumn;
    @FXML private TableColumn<Correo, String> invitadoColumn;
    @FXML private TableColumn<Correo, String> asuntoColumn;
    @FXML private TableColumn<Correo, String> fechaEnvioColumn;
    @FXML private TableColumn<Correo, Void> actionsColumn;

    private CorreoService correoService;
    private ObservableList<Correo> correoList;

    public void initialize() {
        correoService = new CorreoService();
        correoList = FXCollections.observableArrayList();

        setupTableColumns();
        loadCorreos();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idCorreo"));
        eventoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvento().getTitulo()));
        usuarioColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsuario().getNombre()));
        invitadoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInvitado().getNombre()));
        asuntoColumn.setCellValueFactory(new PropertyValueFactory<>("asunto"));
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

                editButton.setOnAction(event -> handleEditCorreo(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeleteCorreo(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadCorreos() {
        try {
            List<Correo> correos = correoService.getAllCorreos();
            correoList.setAll(correos);
            correoTable.setItems(correoList);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los correos: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        ObservableList<Correo> filteredList = correoList.filtered(correo ->
            correo.getEvento().getTitulo().toLowerCase().contains(searchTerm) ||
            correo.getUsuario().getNombre().toLowerCase().contains(searchTerm) ||
            correo.getInvitado().getNombre().toLowerCase().contains(searchTerm) ||
            correo.getAsunto().toLowerCase().contains(searchTerm)
        );
        correoTable.setItems(filteredList);
    }

    @FXML
    private void handleNewCorreo() {
        Correo newCorreo = new Correo();
        boolean saveClicked = showCorreoDialog(newCorreo, "Nuevo Correo");
        if (saveClicked) {
            try {
                correoService.createCorreo(newCorreo);
                loadCorreos();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo crear el correo: " + e.getMessage());
            }
        }
    }

    private void handleEditCorreo(Correo correo) {
        boolean saveClicked = showCorreoDialog(correo, "Editar Correo");

        if (saveClicked) {
            try {
                correoService.updateCorreo(correo);
                correoTable.refresh();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo actualizar el correo: " + e.getMessage());
            }
        }
    }

    private boolean showCorreoDialog(Correo correo, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/correo-dialog-view.fxml"));
            VBox dialogRoot = loader.load();
            
            dialogRoot.getStylesheets().addAll(correoTable.getScene().getStylesheets());
            String themeClass = correoTable.getScene().getRoot().getStyleClass().contains("dark") ? "dark" : "light";
            dialogRoot.getStyleClass().addAll("root", themeClass);

            Scene scene = new Scene(dialogRoot);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(correoTable.getScene().getWindow());
            dialogStage.setScene(scene);

            CorreoDialogController controller = loader.getController();
            controller.setCorreo(correo);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el diálogo de correo: " + e.getMessage());
            return false;
        }
    }

    private void handleDeleteCorreo(Correo correo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro que desea eliminar este correo?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    correoService.deleteCorreo(correo.getIdCorreo());
                    correoList.remove(correo);
                } catch (SQLException e) {
                    showAlert("Error", "No se pudo eliminar el correo: " + e.getMessage());
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