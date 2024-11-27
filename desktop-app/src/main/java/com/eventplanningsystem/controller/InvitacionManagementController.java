package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.Invitacion;
import com.eventplanningsystem.service.InvitacionService;
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

public class InvitacionManagementController {

    @FXML private TextField searchField;
    @FXML private TableView<Invitacion> invitacionTable;
    @FXML private TableColumn<Invitacion, Integer> idColumn;
    @FXML private TableColumn<Invitacion, String> eventoColumn;
    @FXML private TableColumn<Invitacion, String> invitadoColumn;
    @FXML private TableColumn<Invitacion, String> estadoColumn;
    @FXML private TableColumn<Invitacion, String> fechaRespuestaColumn;
    @FXML private TableColumn<Invitacion, Void> actionsColumn;

    private InvitacionService invitacionService;
    private ObservableList<Invitacion> invitacionList;

    public void initialize() {
        invitacionService = new InvitacionService();
        invitacionList = FXCollections.observableArrayList();

        setupTableColumns();
        loadInvitaciones();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idInvitacion"));
        eventoColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEvento().getTitulo()));
        invitadoColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getInvitado().getNombre()));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        fechaRespuestaColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getFechaRespuesta() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                return new SimpleStringProperty(cellData.getValue().getFechaRespuesta().format(formatter));
            } else {
                return new SimpleStringProperty("");
            }
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

                editButton.setOnAction(event -> handleEditInvitacion(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeleteInvitacion(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadInvitaciones() {
        try {
            List<Invitacion> invitaciones = invitacionService.getAllInvitaciones();
            invitacionList.setAll(invitaciones);
            invitacionTable.setItems(invitacionList);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar las invitaciones: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        ObservableList<Invitacion> filteredList = invitacionList.filtered(invitacion ->
            invitacion.getEvento().getTitulo().toLowerCase().contains(searchTerm) ||
            invitacion.getInvitado().getNombre().toLowerCase().contains(searchTerm) ||
            invitacion.getEstado().toLowerCase().contains(searchTerm)
        );
        invitacionTable.setItems(filteredList);
    }

    @FXML
    private void handleNewInvitacion() {
        Invitacion newInvitacion = new Invitacion();
        boolean saveClicked = showInvitacionDialog(newInvitacion, "Nueva Invitación");
        if (saveClicked) {
            try {
                invitacionService.createInvitacion(newInvitacion);
                loadInvitaciones();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo crear la invitación: " + e.getMessage());
            }
        }
    }

    private void handleEditInvitacion(Invitacion invitacion) {
        boolean saveClicked = showInvitacionDialog(invitacion, "Editar Invitación");

        if (saveClicked) {
            try {
                invitacionService.updateInvitacion(invitacion);
                invitacionTable.refresh();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo actualizar la invitación: " + e.getMessage());
            }
        }
    }

    private boolean showInvitacionDialog(Invitacion invitacion, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/invitacion-dialog-view.fxml"));
            VBox dialogRoot = loader.load();
            
            dialogRoot.getStylesheets().addAll(invitacionTable.getScene().getStylesheets());
            String themeClass = invitacionTable.getScene().getRoot().getStyleClass().contains("dark") ? "dark" : "light";
            dialogRoot.getStyleClass().addAll("root", themeClass);

            Scene scene = new Scene(dialogRoot);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(invitacionTable.getScene().getWindow());
            dialogStage.setScene(scene);

            InvitacionDialogController controller = loader.getController();
            controller.setInvitacion(invitacion);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el diálogo de invitación: " + e.getMessage());
            return false;
        }
    }

    private void handleDeleteInvitacion(Invitacion invitacion) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro que desea eliminar esta invitación?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    invitacionService.deleteInvitacion(invitacion.getIdInvitacion());
                    invitacionList.remove(invitacion);
                } catch (SQLException e) {
                    showAlert("Error", "No se pudo eliminar la invitación: " + e.getMessage());
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