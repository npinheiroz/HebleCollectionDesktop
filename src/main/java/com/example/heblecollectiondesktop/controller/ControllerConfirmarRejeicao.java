package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.model.Ticket;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ControllerConfirmarRejeicao {
    @FXML
    private ComboBox <String> cbMotivoRejeicao;
    @FXML private TextArea txtObservacao;
    private Ticket TicketRejeitar;
    private ControllerTicketsPendentes controllerPai;

    private TicketDAO ticketDAO = new TicketDAO();

    public void initialize(){
        if (cbMotivoRejeicao != null) {
            cbMotivoRejeicao.setItems(FXCollections.observableArrayList(
                    "",
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
            mostrarAlerta( "Motivo Obrigatório", "Por favor, selecione um motivo para a rejeição.", Alert.AlertType.WARNING);
            return;
        }

        if ( TicketRejeitar== null) {
            mostrarAlerta( "Erro", "Nenhuma Ticket selecionado para exclusão.", Alert.AlertType.ERROR);
            return;
        }

        try {
            boolean sucesso = ticketDAO.deletar(TicketRejeitar.getId());

            if (sucesso) {
                mostrarAlerta( "Sucesso", "Empresa removida com sucesso!",Alert.AlertType.INFORMATION);

                if (controllerPai != null) {
                    try {
                        controllerPai.carregarTickets();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                fecharJanela(event);
            } else {
                mostrarAlerta( "Erro", "Não foi possível concluir a exclusão da empresa.", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();

            String mensagemErro = e.getMessage();
            if (mensagemErro != null && (mensagemErro.contains("foreign key") || mensagemErro.contains("1451"))) {
                mostrarAlerta( "Violação de Integridade",
                        "Não é possível excluir a empresa '"+TicketRejeitar.getAssunto() + "' pois existem funcionários, produtos ou registros vinculados a ela no sistema.", Alert.AlertType.ERROR);
            } else {
                mostrarAlerta( "Erro de Banco de Dados", "Falha ao deletar empresa: " + mensagemErro, Alert.AlertType.ERROR);
            }
        }
    }

    public void SetTicket (Ticket ticket){
        this.TicketRejeitar = ticket;
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
}
