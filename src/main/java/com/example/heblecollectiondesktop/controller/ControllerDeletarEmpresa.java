package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.database.EmpresaDAO;
import com.example.heblecollectiondesktop.database.LogsDAO;
import com.example.heblecollectiondesktop.model.Empresa;
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

public class ControllerDeletarEmpresa {

    @FXML private Label lblEmpresaInfo;
    @FXML private ComboBox<String> cbMotivo;
    @FXML private TextArea txtObservacao;

    private final EmpresaDAO empresaDAO = new EmpresaDAO();
    private final LogsDAO logsDAO = new LogsDAO();
    private Empresa empresaParaDeletar;
    private ControllerGerenciarEmpresas controllerPai;
    private Funcionario funcionarioLogado;

    public void setDados(Empresa empresa, ControllerGerenciarEmpresas controllerPai, Funcionario funcionarioLogado) {
        this.empresaParaDeletar = empresa;
        this.controllerPai = controllerPai;
        this.funcionarioLogado = funcionarioLogado;

        if (empresa != null && lblEmpresaInfo != null) {
            lblEmpresaInfo.setText("Excluindo Empresa: " + empresa.getNome() + " (CNPJ: " + empresa.getCnpj() + ")");
        }
    }

    @FXML
    public void initialize() {
        if (cbMotivo != null) {
            cbMotivo.setItems(FXCollections.observableArrayList(
                    "Fechamento da empresa",
                    "Denúncias e tickets excederam o limite",
                    "Solicitação da própria empresa",
                    "Cadastro Duplicado / Incorreto",
                    "Violação das Políticas do Sistema",
                    "Inatividade prolongada",
                    "Outro"
            ));
        }
    }

    @FXML
    private void confirmarDelecao(ActionEvent event) {
        String motivo = cbMotivo.getValue();
        String observacao = txtObservacao.getText() != null ? txtObservacao.getText().trim() : "";

        if (motivo == null || motivo.isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Motivo Obrigatório", "Por favor, selecione um motivo para a exclusão da empresa.");
            return;
        }

        if (empresaParaDeletar == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Nenhuma empresa selecionada para exclusão.");
            return;
        }

        try {
            boolean sucesso = empresaDAO.deletar(empresaParaDeletar.getId());

            if (sucesso) {
                String detalhesLog = "Excluiu a empresa '" + empresaParaDeletar.getNome() + "' (CNPJ: " + empresaParaDeletar.getCnpj() + "). Motivo: " + motivo;
                if (!observacao.isBlank()) {
                    detalhesLog += " | Obs: " + observacao;
                }

                logsDAO.registrarLog(funcionarioLogado, "DELECAO_EMPRESA", detalhesLog);

                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso",
                        "A empresa '" + empresaParaDeletar.getNome() + "' foi removida do sistema com sucesso!");
                if (controllerPai != null) {
                    try {
                        controllerPai.carregarEmpresas();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                fecharJanela(event);
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível concluir a exclusão da empresa.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            String mensagemErro = e.getMessage();
            if (mensagemErro != null && (mensagemErro.contains("foreign key") || mensagemErro.contains("1451"))) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Integridade Relacional",
                        "Não é possível excluir a empresa '" + empresaParaDeletar.getNome() +
                                "' pois existem registros vinculados a ela (funcionários, produtos, marcas ou coleções).");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Banco de Dados",
                        "Falha ao excluir a empresa: " + mensagemErro);
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void fecharJanela(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        fecharJanela(event);
    }
}