package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.User;
import com.eventplanningsystem.service.UserService;
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

public class UserManagementController {

    @FXML private TextField searchField;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> phoneColumn;
    @FXML private TableColumn<User, String> userTypeColumn;
    @FXML private TableColumn<User, String> registrationDateColumn;
    @FXML private TableColumn<User, Void> actionsColumn;

    private UserService userService;
    private ObservableList<User> userList;

    public void initialize() {
        userService = new UserService();
        userList = FXCollections.observableArrayList();

        setupTableColumns();
        loadUsers();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("correoElectronico"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("tipoUsuario"));
        registrationDateColumn.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));
        
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
                
                // Ajustar el tamaño de los botones
                editButton.setMaxWidth(Double.MAX_VALUE);
                deleteButton.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(editButton, javafx.scene.layout.Priority.ALWAYS);
                HBox.setHgrow(deleteButton, javafx.scene.layout.Priority.ALWAYS);

                editButton.setOnAction(event -> handleEditUser(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeleteUser(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadUsers() {
        try {
            List<User> users = userService.getAllUsers();
            userList.setAll(users);
            userTable.setItems(userList);
        } catch (SQLException e) {
            showAlert("Error", "No se pudieron cargar los usuarios: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        ObservableList<User> filteredList = userList.filtered(user ->
            user.getNombre().toLowerCase().contains(searchTerm) ||
            user.getCorreoElectronico().toLowerCase().contains(searchTerm)
        );
        userTable.setItems(filteredList);
    }

    @FXML
    private void handleNewUser() {
        User newUser = new User();
        boolean saveClicked = showUserDialog(newUser, "Nuevo Usuario");

        if (saveClicked) {
            try {
                userService.createUser(newUser);
                loadUsers();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo crear el usuario: " + e.getMessage());
            }
        }
    }

    private void handleEditUser(User user) {
        boolean saveClicked = showUserDialog(user, "Editar Usuario");

        if (saveClicked) {
            try {
                userService.updateUser(user);
                userTable.refresh();
            } catch (SQLException e) {
                showAlert("Error", "No se pudo actualizar el usuario: " + e.getMessage());
            }
        }
    }

    private boolean showUserDialog(User user, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user-dialog-view.fxml"));
            VBox dialogRoot = loader.load();
            
            // Aplicar estilos y tema
            dialogRoot.getStylesheets().addAll(userTable.getScene().getStylesheets());
            String themeClass = userTable.getScene().getRoot().getStyleClass().contains("dark") ? "dark" : "light";
            dialogRoot.getStyleClass().addAll("root", themeClass);

            Scene scene = new Scene(dialogRoot);
            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(userTable.getScene().getWindow());
            dialogStage.setScene(scene);

            UserDialogController controller = loader.getController();
            controller.setUser(user);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el diálogo de usuario: " + e.getMessage());
            return false;
        }
    }

    private void handleDeleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Está seguro que desea eliminar este usuario?");
        alert.setContentText("Esta acción no se puede deshacer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    userService.deleteUser(user.getIdUsuario());
                    userList.remove(user);
                } catch (SQLException e) {
                    showAlert("Error", "No se pudo eliminar el usuario: " + e.getMessage());
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