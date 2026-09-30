package com.example.heblecollectiondesktop.controller;

import java.sql.SQLException;

import com.example.heblecollectiondesktop.database.FuncionarioDAO;
import com.example.heblecollectiondesktop.database.LogsDAO;
import com.example.heblecollectiondesktop.model.Funcionario;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ControllerDeletarFuncionario {

    @FXML private Label lblFuncionarioInfo;
    @FXML private ComboBox<String> cbMotivo;
    @FXML private TextArea txtObservacao;

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private final LogsDAO logsDAO = new LogsDAO();
    private Funcionario funcionarioParaDeletar;
    private ControllerGerenciarFuncionarios controllerPai;
    private String usuarioLogado = "ADMIN";

    public void setDados(Funcionario funcionario, ControllerGerenciarFuncionarios controllerPai, String usuarioLogado) {
        this.funcionarioParaDeletar = funcionario;
        this.controllerPai = controllerPai;
        if (usuarioLogado != null && !usuarioLogado.isBlank()) {
            this.usuarioLogado = usuarioLogado;
        }

        if (funcionario != null) {
            lblFuncionarioInfo.setText("Excluindo: " + funcionario.getMatricula() + " (" + funcionario.getCargo() + ")");
        }
    }

    @FXML
    public void initialize() {
        cbMotivo.setItems(FXCollections.observableArrayList(
                "Desligamento / Demissão",
                "Fim de Contrato",
                "Solicitação do Próprio Funcionário",
                "Cadastro Duplicado / Incorreto",
                "Violação das Políticas da Empresa",
                "Outro"
        ));
    }

    @FXML
    private void confirmarDelecao(ActionEvent event) {
        String motivo = cbMotivo.getValue();
        String observacao = txtObservacao.getText() != null ? txtObservacao.getText().trim() : "";

        if (motivo == null || motivo.isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Motivo Obrigatório", "Por favor, selecione um motivo para a exclusão.");
            return;
        }

        if (funcionarioParaDeletar == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Nenhum funcionário selecionado.");
            return;
        }

        try {

            boolean sucesso = funcionarioDAO.deletar(funcionarioParaDeletar.getId());

            if (sucesso) {
                // 2. Grava APENAS UM log com o nome/matrícula limpos
                String alvoAfetado = funcionarioParaDeletar.getMatricula();
                String detalhes = "Motivo: " + motivo + (observacao.isEmpty() ? "" : " | Obs: " + observacao);

                logsDAO.registrarLog(
                        usuarioLogado,
                        "EXCLUSAO_FUNCIONARIO",
                        alvoAfetado,
                        detalhes
                );

                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Funcionário removido e ação registrada no log da moderação!");

                if (controllerPai != null) {
                    try {
                        controllerPai.carregarFuncionarios();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                fecharJanela(event);
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível concluir a exclusão do funcionário.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Banco de Dados", "Falha ao deletar funcionário: " + e.getMessage());
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        fecharJanela(event);
    }

    private void fecharJanela(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}