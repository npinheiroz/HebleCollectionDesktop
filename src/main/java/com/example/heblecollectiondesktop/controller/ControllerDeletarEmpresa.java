package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.database.EmpresaDAO;
import com.example.heblecollectiondesktop.model.Empresa;
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

    @FXML
    private Label lblFuncionarioInfo;
    @FXML private ComboBox<String> cbMotivo;
    @FXML private TextArea txtObservacao;

    private final EmpresaDAO empresaDAO = new EmpresaDAO();
    private Empresa empresaParaDeletar;
    private ControllerGerenciarEmpresas controllerPai;

    public void setDados(Empresa empresa, ControllerGerenciarEmpresas controllerPai, String usuarioLogado) {
        this.empresaParaDeletar = empresa;
        this.controllerPai = controllerPai;
        if (empresa != null && lblFuncionarioInfo != null) {
            lblFuncionarioInfo.setText("Excluindo: " + empresa.getNome() + " (CNPJ: " + empresa.getCnpj() + ")");
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
                    "Violação das Políticas da Empresa",
                    "Outro"
            ));
        }
    }

    @FXML
    private void confirmarDelecao(ActionEvent event) {
        String motivo = cbMotivo.getValue();
        String observacao = txtObservacao.getText() != null ? txtObservacao.getText().trim() : "";

        if (motivo == null || motivo.isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Motivo Obrigatório", "Por favor, selecione um motivo para a exclusão.");
            return;
        }

        if (empresaParaDeletar == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Nenhuma empresa selecionada para exclusão.");
            return;
        }

        try {
            boolean sucesso = empresaDAO.deletar(empresaParaDeletar.getId());

            if (sucesso) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Empresa removida com sucesso!");

                if (controllerPai != null) {
                    try {
                        controllerPai.carregarEmpresas(); // Atualiza a tabela principal
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
                mostrarAlerta(Alert.AlertType.ERROR, "Violação de Integridade",
                        "Não é possível excluir a empresa '" + empresaParaDeletar.getNome() + "' pois existem funcionários, produtos ou registros vinculados a ela no sistema.");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Banco de Dados", "Falha ao deletar empresa: " + mensagemErro);
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