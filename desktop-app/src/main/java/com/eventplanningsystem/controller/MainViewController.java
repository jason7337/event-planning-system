package com.eventplanningsystem.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class MainViewController {
    @FXML
    private HBox rootPane;

    public void initialize() {
        // Initialization code if needed
    }

    public void setTheme(boolean isDark) {
        if (isDark) {
            rootPane.getStyleClass().add("dark");
        } else {
            rootPane.getStyleClass().remove("dark");
        }
    }
}