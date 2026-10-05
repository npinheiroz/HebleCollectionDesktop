package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.controller.ControllerTicketsAndamento;
import com.example.heblecollectiondesktop.controller.ControllerTicketsPendentes;
import com.example.heblecollectiondesktop.database.LogsDAO;
import com.example.heblecollectiondesktop.database.TicketDAO;
import com.example.heblecollectiondesktop.model.Funcionario;
import com.example.heblecollectiondesktop.model.Ticket;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ControllerConfirmarRejeicao {

    @FXML private ComboBox<String> cbMotivoRejeicao;
    @FXML private TextArea txtObservacao;

    private Ticket ticketRejeitar;
    private ControllerTicketsPendentes controllerPai;
    private Funcionario funcionarioLogado;

    private final LogsDAO rejeitarLog = new LogsDAO();
    private final TicketDAO ticketDAO = new TicketDAO();

    public void setTicketSelecionado(Ticket ticket) {
        this.ticketRejeitar = ticket;
    }

    public void setControllerPai(ControllerTicketsPendentes controllerPai) {
        this.controllerPai = controllerPai;
    }

    public void setFuncionarioLogado(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
    }

    @FXML
    public void initialize() {
        if (cbMotivoRejeicao != null) {
            cbMotivoRejeicao.setItems(FXCollections.observableArrayList(
                    "Denúncias e tickets excederam o limite",
                    "Solicitação da própria empresa",
                    "Cadastro Duplicado / Incorreto",
                    "Violação das Políticas da Empresa",
                    "Outro"
            ));
        }
    }

    @FXML
    private void confirmarRejeicao(ActionEvent event) {
        String motivo = cbMotivoRejeicao.getValue();
        String observacao = txtObservacao.getText() != null ? txtObservacao.getText().trim() : "";

        if (motivo == null || motivo.isBlank()) {
            mostrarAlerta("Motivo Obrigatório", "Por favor, selecione um motivo para a rejeição.", Alert.AlertType.WARNING);
            return;
        }

        if (ticketRejeitar == null) {
            mostrarAlerta("Erro", "Nenhum Ticket selecionado para rejeição.", Alert.AlertType.ERROR);
            return;
        }

        try {
            boolean sucesso = ticketDAO.deletar(ticketRejeitar.getId());

            if (sucesso) {
                mostrarAlerta("Sucesso", "Ticket removido com sucesso!", Alert.AlertType.INFORMATION);

                String idModerador = (funcionarioLogado != null && funcionarioLogado.getMatricula() != null)
                        ? funcionarioLogado.getMatricula()
                        : "SISTEMA";

                String detalheLog = "Motivo: " + motivo + (observacao.isEmpty() ? "" : " | Obs: " + observacao);


                rejeitarLog.registrarLog(idModerador, "REJEITAR_TICKET", String.valueOf(ticketRejeitar.getId()), detalheLog);

                if (controllerPai != null) {
                    try {
                        controllerPai.carregarTickets();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                fecharJanela(event);
            } else {
                mostrarAlerta("Erro", "Não foi possível concluir a exclusão do ticket.", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();

            String mensagemErro = e.getMessage();
            if (mensagemErro != null && (mensagemErro.contains("foreign key") || mensagemErro.contains("1451"))) {
                mostrarAlerta("Violação de Integridade",
                        "Não é possível excluir o ticket '" + ticketRejeitar.getAssunto() + "' pois existem registros vinculados a ele.", Alert.AlertType.ERROR);
            } else {
                mostrarAlerta("Erro de Banco de Dados", "Falha ao deletar ticket: " + mensagemErro, Alert.AlertType.ERROR);
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
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
    public void cancelar() {
        Stage janelaAtual = (Stage) txtObservacao.getScene().getWindow();
        if (janelaAtual != null) {
            janelaAtual.close();
        }
    }
}