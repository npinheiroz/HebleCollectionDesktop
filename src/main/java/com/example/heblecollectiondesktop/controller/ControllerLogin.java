package com.example.heblecollectiondesktop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerLogin implements Initializable {

    @FXML
    private ImageView imgLogo;

    @FXML
    private TextField txMatricula;

    @FXML
    private PasswordField txSenha;

    @FXML
    private Button btnLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarLogo();
    }

    private void carregarLogo() {
        if (imgLogo == null) return;

        String[] caminhos = {
                "/com/example/heblecollectiondesktop/images/logo.png",
                "/images/logo.png",
                "/logo.png"
        };

        for (String caminho : caminhos) {
            InputStream stream = getClass().getResourceAsStream(caminho);
            if (stream != null) {
                imgLogo.setImage(new Image(stream));
                return;
            }
        }
    }

    @FXML
    void fazerLogin(ActionEvent event) {
        String matricula = txMatricula.getText() != null ? txMatricula.getText().trim() : "";
        String senha = txSenha.getText() != null ? txSenha.getText().trim() : "";

        if (matricula.isEmpty() || senha.isEmpty()) {
            exibirAlerta(Alert.AlertType.WARNING, "Campos Obrigatórios", "Por favor, preencha a matrícula e a senha.");
            return;
        }

        if (autenticarFuncionario(matricula, senha)) {
            abrirDashboard();
        } else {
            exibirAlerta(Alert.AlertType.ERROR, "Acesso Negado", "Matrícula ou senha incorretos.");
        }
    }

    private boolean autenticarFuncionario(String matricula, String senha) {
        String url = "jdbc:mysql://localhost:3306/login_schema?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String usuarioDb = "root";
        String senhaDb = "heblecollection@_2026-2027";

        String sql = "SELECT 1 FROM funcionarios WHERE matricula = ? AND senha = ? LIMIT 1";

        try (Connection conexao = DriverManager.getConnection(url, usuarioDb, senhaDb);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, matricula);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            exibirAlerta(Alert.AlertType.ERROR, "Erro de Conexão", "Não foi possível conectar ao banco de dados.");
            return false;
        }
    }

    private void abrirDashboard() {
        try {
            Stage stageAtual = (Stage) btnLogin.getScene().getWindow();
            stageAtual.close();

            URL dashboardLocation = getClass().getResource("/com/example/heblecollectiondesktop/view/dashboard.fxml");
            if (dashboardLocation == null) {
                dashboardLocation = getClass().getResource("/view/dashboard.fxml");
            }
            if (dashboardLocation == null) {
                dashboardLocation = getClass().getResource("view/dashboard.fxml");
            }

            if (dashboardLocation == null) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", "Arquivo dashboard.fxml não foi encontrado.");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(dashboardLocation);
            Parent root = fxmlLoader.load();

            Stage stageDashboard = new Stage();
            stageDashboard.setTitle("Heble Collection - Dashboard");
            stageDashboard.setScene(new Scene(root, 1200, 760));
            stageDashboard.setResizable(true);
            stageDashboard.centerOnScreen();
            stageDashboard.show();

        } catch (IOException e) {
            e.printStackTrace();
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Falha ao abrir o Dashboard.");
        }
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}