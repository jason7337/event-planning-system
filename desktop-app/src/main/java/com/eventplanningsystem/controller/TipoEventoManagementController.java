package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.TipoEvento;
import com.eventplanningsystem.service.TipoEventoService;
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

public class TipoEventoManagementController {

    @FXML private TextField searchField;
    @FXML private TableView<TipoEvento> tipoEventoTable;
    @FXML private TableColumn<TipoEvento, Integer> idColumn;
    @FXML private TableColumn<TipoEvento, String> nombreTipoColumn;
    @FXML private TableColumn<TipoEvento, String> descripcionColumn;
    @FXML private TableColumn<TipoEvento, Void> actionsColumn;

    private TipoEventoService tipoEventoService;
    private ObservableList<TipoEvento> tipoEventoList;

    public void initialize() {
        tipoEventoService = new TipoEventoService();
        tipoEventoList = FXCollections.observableArrayList();

        setupTableColumns();
        loadTiposEvento();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idTipoEvento"));
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

                editButton.setOnAction(event -> handleEditTipoEvento(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeleteTipoEvento(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadTiposEvento() {
        try {
            List<TipoEvento> tiposEvento = tipoEventoService.getAllTiposEvento();
            tipoEventoList.setAll(tiposEvento);
            tipoEventoTable.setItems(tipoEventoList);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los tipos de evento: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        ObservableList<TipoEvento> filteredList = tipoEventoList.filtered(tipoEvento ->
            tipoEvento.getNombreTipo().toLowerCase().contains(searchTerm) ||
            tipoEvento.getDescripcion().toLowerCase().contains(searchTerm)
        );
        tipoEventoTable.setItems(filteredList);
    }

    @FXML
    private void handleNewTipoEvento() {
        TipoEvento newTipoEvento = new TipoEvento();
        boolean saveClicked = showTipoEventoDialog(newTipoEvento, "Nuevo Tipo de Evento");

        if (saveClicked) {
            try {
                tipoEventoService.createTipoEvento(newTipoEvento);
                loadTiposEvento();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo crear el tipo de evento: " + e.getMessage());
            }
        }
    }

    private void handleEditTipoEvento(TipoEvento tipoEvento) {
        boolean saveClicked = showTipoEventoDialog(tipoEvento, "Editar Tipo de Evento");

        if (saveClicked) {
            try {
                tipoEventoService.updateTipoEvento(tipoEvento);
                tipoEventoTable.refresh();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo actualizar el tipo de evento: " + e.getMessage());
            }
        }
    }

    private boolean showTipoEventoDialog(TipoEvento tipoEvento, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tipo-evento-dialog-view.fxml"));
            VBox dialogRoot = loader.load();
            
            dialogRoot.getStylesheets().addAll(tipoEventoTable.getScene().getStylesheets());
            String themeClass = tipoEventoTable.getScene().getRoot().getStyleClass().contains("dark") ? "dark" : "light";
            dialogRoot.getStyleClass().addAll("root", themeClass);

            Scene scene = new Scene(dialogRoot);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tipoEventoTable.getScene().getWindow());
            dialogStage.setScene(scene);

            TipoEventoDialogController controller = loader.getController();
            controller.setTipoEvento(tipoEvento);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el diálogo de tipo de evento: " + e.getMessage());
            return false;
        }
    }

    private void handleDeleteTipoEvento(TipoEvento tipoEvento) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro que desea eliminar este tipo de evento?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    tipoEventoService.deleteTipoEvento(tipoEvento.getIdTipoEvento());
                    tipoEventoList.remove(tipoEvento);
                } catch (SQLException e) {
                    showAlert("Error", "No se pudo eliminar el tipo de evento: " + e.getMessage());
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