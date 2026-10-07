package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.database.CupomDAO;
import com.example.heblecollectiondesktop.database.LogsDAO;
import com.example.heblecollectiondesktop.model.Cupom;
import com.example.heblecollectiondesktop.model.Funcionario;
import com.example.heblecollectiondesktop.model.Ticket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ControllerDeletarCupons {
    CupomDAO cupomDAO = new CupomDAO();



    @FXML
    private Label lblvalidade;
    @FXML
    private Label lblId;
    @FXML
    private Label lblStatus;
    private Cupom cupomparadeletar;
    private ControllerGerenciarCupons controllerPai;
    private Funcionario funcionarioLogado;
    private LogsDAO logsDAO = new LogsDAO();
    @FXML
    private TextArea txtObservacao;
    public void Setdados(Cupom cupomparadeletar,ControllerGerenciarCupons controllerPai,Funcionario funcionarioLogado ){
        this.cupomparadeletar = cupomparadeletar;
        this.controllerPai = controllerPai;
        this.funcionarioLogado = funcionarioLogado;
    }
    public void setcupom(Cupom cupom) {

        if (cupom == null) return;

        lblId.setText("ID: " + cupom.getId());


        String textoStatus = cupom.isAtivo() ? "Ativo / Suspenso" : "Pendente";
        lblStatus.setText("Estado: " + textoStatus);
        lblvalidade.setText("Validade:"+String.valueOf(cupom.getValidade()));


    }
    @FXML
    public void Cancelar() {
        Stage janelaAtual = (Stage) lblId.getScene().getWindow();
        janelaAtual.close();
    }
    @FXML
    void confirmarDelecao(ActionEvent event) {

            try {
                boolean removido = cupomDAO.deletar(cupomparadeletar);
                logsDAO.registrarLog(funcionarioLogado,"DELETAR_CUPOM", String.valueOf(cupomparadeletar), String.valueOf(txtObservacao));


                if (removido) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Cupom removido com sucesso!");

                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível remover o cupom.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Banco de Dados", "Falha ao remover cupom: " + e.getMessage());
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
