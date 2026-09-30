package com.example.heblecollectiondesktop;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) {
        try {
            // Alterado para carregar o arquivo FXML de login
            URL fxmlLocation = getClass().getResource("/com/example/heblecollectiondesktop/view/login.fxml");
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getResource("/view/login.fxml");
            }
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getResource("view/login.fxml");
            }

            if (fxmlLocation == null) {
                System.err.println("Erro: Arquivo FXML de login não encontrado!");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 1200, 760);
            stage.setTitle("Heble Collection - Login");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}