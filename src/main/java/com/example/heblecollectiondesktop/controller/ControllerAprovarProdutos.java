package com.example.heblecollectiondesktop.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.example.heblecollectiondesktop.database.ProdutoDAO;
import com.example.heblecollectiondesktop.model.Funcionario;
import com.example.heblecollectiondesktop.model.Produto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class ControllerAprovarProdutos implements Initializable {

    @FXML private Button btnAprovar;
    @FXML private Button btnRejeitar;

    @FXML private TableView<Produto> tvProdutosPendentes;
    @FXML private TableColumn<Produto, Integer> colId;
    @FXML private TableColumn<Produto, String> colEmpresa;
    @FXML private TableColumn<Produto, String> colNome;
    @FXML private TableColumn<Produto, String> colCategoria;
    @FXML private TableColumn<Produto, Double> colPreco;
    @FXML private TableColumn<Produto, Integer> colEstoque;
    @FXML private TableColumn<Produto, String> colStatus;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private ObservableList<Produto> listaPendentes = FXCollections.observableArrayList();

    private Pane containerCentral;
    private Funcionario funcionarioLogado;
    private Runnable acaoVoltar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabela();
        carregarProdutosPendentes();
    }

    public void setContainerCentral(Pane containerCentral) {
        this.containerCentral = containerCentral;
    }

    public void setFuncionarioLogado(Funcionario funcionario) {
        this.funcionarioLogado = funcionario;
    }

    public void setAcaoVoltar(Runnable acaoVoltar) {
        this.acaoVoltar = acaoVoltar;
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmpresa.setCellValueFactory(new PropertyValueFactory<>("nomeEmpresa"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tvProdutosPendentes.setItems(listaPendentes);
    }

    public void carregarProdutosPendentes() {
        try {
            List<Produto> pendentes = produtoDAO.listarPendentes();
            listaPendentes.setAll(pendentes);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Falha ao carregar lista de pendentes: " + e.getMessage());
        }
    }

    @FXML
    void acaoAprovarProduto(ActionEvent event) {
        Produto selecionado = tvProdutosPendentes.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um produto para aprovar.");
            return;
        }

        try {
            if (produtoDAO.atualizarStatus(selecionado.getId(), "APROVADO")) {
                listaPendentes.remove(selecionado);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Produto aprovado com sucesso!");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    void acaoRejeitarProduto(ActionEvent event) {
        Produto selecionado = tvProdutosPendentes.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Atenção", "Selecione um produto para rejeitar.");
            return;
        }

        try {
            if (produtoDAO.atualizarStatus(selecionado.getId(), "REJEITADO")) {
                listaPendentes.remove(selecionado);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Submissão rejeitada.");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML
    void voltarAoHub(ActionEvent event) {
        if (acaoVoltar != null) {
            acaoVoltar.run();
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