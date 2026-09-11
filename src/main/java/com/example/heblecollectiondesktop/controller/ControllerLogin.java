package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.database.UsuarioDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ControllerLogin {

    @FXML
    private TextField txMatricula;

    @FXML
    private PasswordField txSenha;

    @FXML
    private Button btnLogin;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void fazerLogin(ActionEvent event) {
        String matricula = txMatricula.getText().trim();
        String senha = txSenha.getText().trim();

        if (matricula.isEmpty() || senha.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Preencha todos os campos!");
            return;
        }

        boolean sucesso = usuarioDAO.autenticar(matricula, senha);

        if (sucesso) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Login realizado com sucesso!");
            // Próximo passo: carregar a tela principal aqui
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Matrícula ou senha incorretos.");
            txSenha.clear();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}