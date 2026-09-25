package com.example.heblecollectiondesktop.controller;

import java.io.IOException;
import java.net.URL;

import com.example.heblecollectiondesktop.model.Funcionario;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;

public class ControllerModeracaoHub {

    private Pane containerCentral;
    private Funcionario funcionarioLogado;

    public void setContainerCentral(Pane containerCentral) {
        this.containerCentral = containerCentral;
    }

    public void setFuncionarioLogado(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
    }

    private void carregarSubVisao(String fxmlPath) {
        if (containerCentral == null) {
            mostrarAlerta("Erro de Navegação", "O container central não foi configurado corretamente.");
            return;
        }

        try {
            URL url = getClass().getResource(fxmlPath);
            if (url == null) {
                // Tenta fallback para caminho relativo simples caso a estrutura de pastas varie
                url = getClass().getResource("/view/" + fxmlPath.substring(fxmlPath.lastIndexOf('/') + 1));
            }

            if (url == null) {
                mostrarAlerta("Erro FXML", "Não foi possível encontrar o arquivo: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent novaVisao = loader.load();

            // Passagem de contexto/sessão para a controller filha, se aplicável
            Object controller = loader.getController();
            if (controller instanceof ControllerGerenciarFuncionarios) {
                ControllerGerenciarFuncionarios cgf = (ControllerGerenciarFuncionarios) controller;
                cgf.setContainerCentral(containerCentral);
                cgf.setFuncionarioLogado(funcionarioLogado);
            }

            // Injeta a sub-visão no container central
            containerCentral.getChildren().setAll(novaVisao);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro de Carregamento", "Falha ao carregar a sub-tela: " + e.getMessage());
        }
    }

    // --- AÇÕES MAPEADAS NO MODERACAOHUB.FXML ---

    @FXML
    private void abrirGerenciarFuncionarios(ActionEvent event) {
        carregarSubVisao("/com/example/heblecollectiondesktop/view/gerenciarFuncionarios.fxml");
    }

    @FXML
    private void handleGerenciarFuncionarios(ActionEvent event) {
        abrirGerenciarFuncionarios(event);
    }

    @FXML
    private void abrirGerenciarEmpresas(ActionEvent event) {
        // Ajuste o caminho se a sua view de empresas usar outro nome
        carregarSubVisao("/com/example/heblecollectiondesktop/view/gerenciar_empresas.fxml");
    }

    @FXML
    private void handleGerenciarEmpresas(ActionEvent event) {
        abrirGerenciarEmpresas(event);
    }

    @FXML
    private void abrirLogsAuditoria(ActionEvent event) {
        carregarSubVisao("/com/example/heblecollectiondesktop/view/moderacaoLogs.fxml");
    }

    @FXML
    private void handleLogsAuditoria(ActionEvent event) {
        abrirLogsAuditoria(event);
    }

    @FXML
    private void voltarDashboard(ActionEvent event) {
        carregarSubVisao("/com/example/heblecollectiondesktop/view/dashboardContent.fxml");
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}