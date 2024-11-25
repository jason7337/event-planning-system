package com.eventplanningsystem;

import com.eventplanningsystem.config.DatabaseConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void init() throws Exception {
        super.init();
        DatabaseConfig.initializeDatabase();
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sistema de Gestión de Eventos - Login");
            stage.setResizable(false);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Puedes agregar aquí un manejo más específico del error si lo deseas
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}