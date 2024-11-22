package com.eventplanningsystem.controller;

import com.eventplanningsystem.model.SidebarItem;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

            @Override
            protected void updateItem(SidebarItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    icon.setIconLiteral(item.getIcon());
                    setText(item.getText());
                    setGraphic(icon);
                }
            }
        });
    }

    @FXML
    private void handleLogout() {
        // Implement logout functionality here
        System.out.println("Logout clicked");
    }
}