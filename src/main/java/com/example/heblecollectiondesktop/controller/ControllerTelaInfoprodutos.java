package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.model.Produto;
import com.example.heblecollectiondesktop.model.Ticket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.nio.charset.StandardCharsets;


public class ControllerTelaInfoprodutos {

    @FXML
    private Label lblpreco;
    @FXML
    private Label lblId;
    @FXML
    private Label lblStatus;
    @FXML
    private TextArea txtDescricao;
    @FXML
    private String Status;
    @FXML
    private Label lblnomemepresa;
    @FXML
    private Label lblquantidadeEstoque;
    @FXML
    private Label lblnomedoproduto;

    public void setInfoprodutos(Produto infoprodutos) {
        if (infoprodutos == null) return;

        lblId.setText("ID: " + lblId.getId());

        lblpreco.setText("Preço: " + infoprodutos.getPreco());

        String textoStatus = infoprodutos.getStatus( ) ? "Resolvido / Fechado" : "Pendente";
        lblStatus.setText("Estado: " + textoStatus);

         lblquantidadeEstoque.setText("quantidade em estoque:" + infoprodutos.getQuantidadeEstoque());

         lblnomemepresa.setText("nome da Empresa: " + infoprodutos.getNomeEmpresa());

         lblnomedoproduto.setText("nome do produto:"+ infoprodutos.getNome());


}

    }