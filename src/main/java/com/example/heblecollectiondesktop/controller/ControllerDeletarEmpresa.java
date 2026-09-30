package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.database.EmpresaDAO;
import com.example.heblecollectiondesktop.database.LogsDAO;
import com.example.heblecollectiondesktop.model.Empresa;
import com.example.heblecollectiondesktop.model.Funcionario;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ControllerDeletarEmpresa {

    @FXML private Label lblEmpresaInfo;
    @FXML private ComboBox<String> cbMotivo;
    @FXML private TextArea txtObservacao;

    private Empresa empresaParaDeletar;
    private ControllerGerenciarEmpresas controllerPai;
    private Funcionario funcionarioLogado;

    private final EmpresaDAO empresaDAO = new EmpresaDAO();
    private final LogsDAO logsDAO = new LogsDAO();

    @FXML
    public void initialize() {
        if (cbMotivo != null) {
            cbMotivo.setItems(FXCollections.observableArrayList(
                    "Encerramento de Atividades",
                    "Cadastro Duplicado / Incorreto",
                    "Solicitação do Cliente / Empresa",
                    "Fraude / Irregularidade",
                    "Outro"
            ));
        }
    }

    public void setDados(Empresa empresa, ControllerGerenciarEmpresas controllerPai, Funcionario funcionario) {
        this.empresaParaDeletar = empresa;
        this.controllerPai = controllerPai;
        this.funcionarioLogado = funcionario;

        if (lblEmpresaInfo != null && empresa != null) {
            lblEmpresaInfo.setText("Excluindo Empresa: " + empresa.getNome());
        }
    }

    @FXML
    private void confirmarExclusao(ActionEvent event) {
        if (empresaParaDeletar == null) {
            mostrarAlerta("Erro", "Nenhuma empresa selecionada para exclusão.", Alert.AlertType.ERROR);
            return;
        }

        String motivo = cbMotivo.getValue();
        String observacao = txtObservacao.getText() != null ? txtObservacao.getText().trim() : "";

        if (motivo == null || motivo.isBlank()) {
            mostrarAlerta("Motivo Obrigatório", "Por favor, selecione um motivo para a exclusão.", Alert.AlertType.WARNING);
            return;
        }

        try {
            boolean sucesso = empresaDAO.deletar(empresaParaDeletar.getId());

            if (sucesso) {
                String idFuncionario = (funcionarioLogado != null && funcionarioLogado.getMatricula() != null)
                        ? funcionarioLogado.getMatricula()
                        : "SISTEMA";

                String alvo = empresaParaDeletar.getNome();
                String detalhes = "Motivo: " + motivo + (observacao.isEmpty() ? "" : " | Obs: " + observacao);

                logsDAO.registrarLog(
                        idFuncionario,
                        "EXCLUSAO_EMPRESA",
                        alvo,
                        detalhes
                );

                mostrarAlerta("Sucesso", "Empresa excluída com sucesso!", Alert.AlertType.INFORMATION);

                if (controllerPai != null) {
                    controllerPai.carregarEmpresas();
                }

                fecharJanela();

            } else {
                mostrarAlerta("Erro de Banco", "Não foi possível confirmar a exclusão no banco de dados.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Ocorreu uma exceção ao tentar excluir: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        fecharJanela();
    }

    private void fecharJanela() {
        if (lblEmpresaInfo != null && lblEmpresaInfo.getScene() != null) {
            Stage stage = (Stage) lblEmpresaInfo.getScene().getWindow();
            stage.close();
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}