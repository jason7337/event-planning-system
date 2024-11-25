package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.TipoInvitado;
import com.eventplanningsystem.service.TipoInvitadoService;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TipoInvitadoManagementController {

    @FXML private TextField searchField;
    @FXML private TableView<TipoInvitado> tipoInvitadoTable;
    @FXML private TableColumn<TipoInvitado, Integer> idColumn;
    @FXML private TableColumn<TipoInvitado, String> nombreTipoColumn;
    @FXML private TableColumn<TipoInvitado, String> descripcionColumn;
    @FXML private TableColumn<TipoInvitado, Void> actionsColumn;

    private TipoInvitadoService tipoInvitadoService;
    private ObservableList<TipoInvitado> tipoInvitadoList;

    public void initialize() {
        tipoInvitadoService = new TipoInvitadoService();
        tipoInvitadoList = FXCollections.observableArrayList();

        setupTableColumns();
        loadTiposInvitado();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idTipoInvitado"));
        nombreTipoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreTipo"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        
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

                editButton.setOnAction(event -> handleEditTipoInvitado(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeleteTipoInvitado(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadTiposInvitado() {
        try {
            List<TipoInvitado> tiposInvitado = tipoInvitadoService.getAllTiposInvitado();
            tipoInvitadoList.setAll(tiposInvitado);
            tipoInvitadoTable.setItems(tipoInvitadoList);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los tipos de invitado: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        ObservableList<TipoInvitado> filteredList = tipoInvitadoList.filtered(tipoInvitado ->
            tipoInvitado.getNombreTipo().toLowerCase().contains(searchTerm) ||
            tipoInvitado.getDescripcion().toLowerCase().contains(searchTerm)
        );
        tipoInvitadoTable.setItems(filteredList);
    }

    @FXML
    private void handleNewTipoInvitado() {
        TipoInvitado newTipoInvitado = new TipoInvitado();
        boolean saveClicked = showTipoInvitadoDialog(newTipoInvitado, "Nuevo Tipo de Invitado");

        if (saveClicked) {
            try {
                tipoInvitadoService.createTipoInvitado(newTipoInvitado);
                loadTiposInvitado();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo crear el tipo de invitado: " + e.getMessage());
            }
        }
    }

    private void handleEditTipoInvitado(TipoInvitado tipoInvitado) {
        boolean saveClicked = showTipoInvitadoDialog(tipoInvitado, "Editar Tipo de Invitado");

        if (saveClicked) {
            try {
                tipoInvitadoService.updateTipoInvitado(tipoInvitado);
                tipoInvitadoTable.refresh();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo actualizar el tipo de invitado: " + e.getMessage());
            }
        }
    }

    private boolean showTipoInvitadoDialog(TipoInvitado tipoInvitado, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tipo-invitado-dialog-view.fxml"));
            VBox dialogRoot = loader.load();
            
            dialogRoot.getStylesheets().addAll(tipoInvitadoTable.getScene().getStylesheets());
            String themeClass = tipoInvitadoTable.getScene().getRoot().getStyleClass().contains("dark") ? "dark" : "light";
            dialogRoot.getStyleClass().addAll("root", themeClass);

            Scene scene = new Scene(dialogRoot);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tipoInvitadoTable.getScene().getWindow());
            dialogStage.setScene(scene);

            TipoInvitadoDialogController controller = loader.getController();
            controller.setTipoInvitado(tipoInvitado);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el diálogo de tipo de invitado: " + e.getMessage());
            return false;
        }
    }

    private void handleDeleteTipoInvitado(TipoInvitado tipoInvitado) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro que desea eliminar este tipo de invitado?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    tipoInvitadoService.deleteTipoInvitado(tipoInvitado.getIdTipoInvitado());
                    tipoInvitadoList.remove(tipoInvitado);
                } catch (SQLException e) {
                    showAlert("Error", "No se pudo eliminar el tipo de invitado: " + e.getMessage());
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