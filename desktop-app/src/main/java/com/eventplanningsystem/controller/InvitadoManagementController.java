package com.eventplanningsystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.eventplanningsystem.model.Invitado;
import com.eventplanningsystem.service.InvitadoService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InvitadoManagementController {

    @FXML private TextField searchField;
    @FXML private TableView<Invitado> invitadoTable;
    @FXML private TableColumn<Invitado, Integer> idColumn;
    @FXML private TableColumn<Invitado, String> nameColumn;
    @FXML private TableColumn<Invitado, String> emailColumn;
    @FXML private TableColumn<Invitado, String> phoneColumn;
    @FXML private TableColumn<Invitado, String> tipoInvitadoColumn;
    @FXML private TableColumn<Invitado, Void> actionsColumn;

    private InvitadoService invitadoService;
    private ObservableList<Invitado> invitadoList;

    public void initialize() {
        invitadoService = new InvitadoService();
        invitadoList = FXCollections.observableArrayList();

        setupTableColumns();
        loadInvitados();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idInvitado"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("correoElectronico"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        tipoInvitadoColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTipoInvitado().getNombreTipo()));
        
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

                editButton.setOnAction(event -> handleEditInvitado(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeleteInvitado(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadInvitados() {
        try {
            List<Invitado> invitados = invitadoService.getAllInvitados();
            invitadoList.setAll(invitados);
            invitadoTable.setItems(invitadoList);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los invitados: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        ObservableList<Invitado> filteredList = invitadoList.filtered(invitado ->
            invitado.getNombre().toLowerCase().contains(searchTerm) ||
            invitado.getCorreoElectronico().toLowerCase().contains(searchTerm)
        );
        invitadoTable.setItems(filteredList);
    }

    @FXML
    private void handleNewInvitado() {
        Invitado newInvitado = new Invitado();
        boolean saveClicked = showInvitadoDialog(newInvitado, "Nuevo Invitado");
        if (saveClicked) {
            try {
                invitadoService.createInvitado(newInvitado);
                loadInvitados();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo crear el invitado: " + e.getMessage());
            }
        }
    }

    private void handleEditInvitado(Invitado invitado) {
        boolean saveClicked = showInvitadoDialog(invitado, "Editar Invitado");

        if (saveClicked) {
            try {
                invitadoService.updateInvitado(invitado);
                invitadoTable.refresh();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo actualizar el invitado: " + e.getMessage());
            }
        }
    }

    private boolean showInvitadoDialog(Invitado invitado, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/invitado-dialog-view.fxml"));
            VBox dialogRoot = loader.load();
            
            dialogRoot.getStylesheets().addAll(invitadoTable.getScene().getStylesheets());
            String themeClass = invitadoTable.getScene().getRoot().getStyleClass().contains("dark") ? "dark" : "light";
            dialogRoot.getStyleClass().addAll("root", themeClass);

            Scene scene = new Scene(dialogRoot);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(invitadoTable.getScene().getWindow());
            dialogStage.setScene(scene);

            InvitadoDialogController controller = loader.getController();
            controller.setInvitado(invitado);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el diálogo de invitado: " + e.getMessage());
            return false;
        }
    }

    private void handleDeleteInvitado(Invitado invitado) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro que desea eliminar este invitado?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    invitadoService.deleteInvitado(invitado.getIdInvitado());
                    invitadoList.remove(invitado);
                } catch (SQLException e) {
                    showAlert("Error", "No se pudo eliminar el invitado: " + e.getMessage());
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