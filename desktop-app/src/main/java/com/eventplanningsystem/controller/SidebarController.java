package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.SidebarItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.kordamp.ikonli.javafx.FontIcon;

public class SidebarController {
    @FXML
    private ListView<SidebarItem> sidebarMenu;

    public void initialize() {
        ObservableList<SidebarItem> items = FXCollections.observableArrayList(
            new SidebarItem("Dashboard", "fas-tachometer-alt"),
            new SidebarItem("Usuarios", "fas-users"),
            new SidebarItem("Tipos de Invitado", "fas-user-tag"),
            new SidebarItem("Invitados", "fas-user-friends"),
            new SidebarItem("Tipos de Evento", "fas-calendar-alt"),
            new SidebarItem("Eventos", "fas-calendar-check"),
            new SidebarItem("Participantes", "fas-user-check"),
            new SidebarItem("Invitaciones", "fas-envelope"),
            new SidebarItem("Mensajes", "fas-comments"),
            new SidebarItem("Correos", "fas-at")
        );

        sidebarMenu.setItems(items);

        sidebarMenu.setCellFactory(param -> new ListCell<SidebarItem>() {
            private final FontIcon icon = new FontIcon();
            private final HBox hbox = new HBox();
            private final Button button = new Button();

            {
                hbox.getChildren().addAll(icon, button);
                HBox.setHgrow(button, Priority.ALWAYS);
                button.setMaxWidth(Double.MAX_VALUE);
            }

            @Override
            protected void updateItem(SidebarItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    icon.setIconLiteral(item.getIcon());
                    icon.getStyleClass().add("sidebar-menu-icon");
                    button.setText(item.getText());
                    button.getStyleClass().add("sidebar-menu-button");
                    button.setOnAction(event -> handleMenuItemClick(item));
                    setGraphic(hbox);
                }
            }
        });
    }

    private void handleMenuItemClick(SidebarItem item) {
        switch (item.getText()) {
            case "Usuarios":
                loadView("/fxml/user-management-view.fxml");
                break;
            case "Tipos de Invitado":
                loadView("/fxml/tipo-invitado-management-view.fxml");
                break;
            case "Invitados":
                loadView("/fxml/invitado-management-view.fxml");
                break;
            case "Tipos de Evento":
                loadView("/fxml/tipo-evento-management-view.fxml");
                break;
            case "Eventos":
                loadView("/fxml/evento-management-view.fxml");
                break;
            case "Participantes":
                loadView("/fxml/participante-evento-management-view.fxml");
                break;
            case "Invitaciones":
                loadView("/fxml/invitacion-management-view.fxml");
                break;
            // Agrega más casos para otros ítems del menú según sea necesario
            default:
                System.out.println("Clicked on: " + item.getText());
                // Lógica para otros ítems del menú
        }
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            BorderPane mainView = (BorderPane) sidebarMenu.getScene().getRoot();
            mainView.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            // Manejo de errores
            System.out.println("Error loading view: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logout clicked");
        // Implementa la lógica de cierre de sesión aquí
    }
}