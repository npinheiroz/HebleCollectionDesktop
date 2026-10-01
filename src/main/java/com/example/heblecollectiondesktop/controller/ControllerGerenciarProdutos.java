package com.example.heblecollectiondesktop.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class ControllerGerenciarProdutos implements Initializable {

    @FXML private Button btnAprovar;
    @FXML private Button btnEditarProduto;
    @FXML private Button btnRejeitar;
    @FXML private Button btnDeletarProduto;
    @FXML private Button btnBuscar;

    @FXML private TextField txtPesquisaProduto;

    @FXML private TableView<Produto> tvProdutos;
    @FXML private TableColumn<Produto, Integer> colId;
    @FXML private TableColumn<Produto, String> colNome;
    @FXML private TableColumn<Produto, String> colCategoria;
    @FXML private TableColumn<Produto, Double> colPreco;
    @FXML private TableColumn<Produto, Integer> colEstoque;
    @FXML private TableColumn<Produto, String> colStatus;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

    private Pane containerCentral;
    private Funcionario funcionarioLogado;
    private Runnable acaoVoltar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabela();
        carregarProdutos();
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
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tvProdutos.setItems(listaProdutos);
    }

    public void carregarProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            listaProdutos.setAll(produtos);
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Banco", "Falha ao carregar produtos: " + e.getMessage());
        }
    }

    @FXML
    void acaoAprovarProduto(ActionEvent event) {
        Produto selecionado = tvProdutos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleção Necessária", "Selecione um produto para aprovar.");
            return;
        }

        try {
            boolean atualizado = produtoDAO.atualizarStatus(selecionado.getId(), "APROVADO");
            if (atualizado) {
                selecionado.setStatus("APROVADO");
                tvProdutos.refresh();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Produto aprovado com sucesso!");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível aprovar o produto: " + e.getMessage());
        }
    }

    @FXML
    void abrirModalEdicao(ActionEvent event) {
        Produto selecionado = tvProdutos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleção Necessária", "Selecione um produto para editar.");
            return;
        }

        // Lógica para abrir o modal de edição passando o objeto 'selecionado'
        mostrarAlerta(Alert.AlertType.INFORMATION, "Editar Produto", "Iniciando edição do produto ID " + selecionado.getId() + ": " + selecionado.getNome());
    }

    @FXML
    void acaoRejeitarProduto(ActionEvent event) {
        Produto selecionado = tvProdutos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleção Necessária", "Selecione um produto para rejeitar.");
            return;
        }

        try {
            boolean atualizado = produtoDAO.atualizarStatus(selecionado.getId(), "REJEITADO");
            if (atualizado) {
                selecionado.setStatus("REJEITADO");
                tvProdutos.refresh();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Produto marcado como rejeitado.");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível rejeitar o produto: " + e.getMessage());
        }
    }

    @FXML
    void acaoDeletarProduto(ActionEvent event) {
        Produto selecionado = tvProdutos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleção Necessária", "Selecione um produto para excluir.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Excluir Produto");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente excluir o produto: " + selecionado.getNome() + "?");

        Optional<ButtonType> resposta = confirmacao.showAndWait();
        if (resposta.isPresent() && resposta.get() == ButtonType.OK) {
            try {
                boolean deletado = produtoDAO.deletar(selecionado.getId());
                if (deletado) {
                    listaProdutos.remove(selecionado);
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Produto removido com sucesso.");
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Não foi possível remover o produto.");
                }
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao deletar produto: " + e.getMessage());
            }
        }
    }

    @FXML
    void acaoBuscarProduto(ActionEvent event) {
        String termo = txtPesquisaProduto.getText().trim();
        try {
            if (termo.isEmpty()) {
                carregarProdutos();
            } else {
                List<Produto> filtrados = produtoDAO.buscarPorTermo(termo);
                listaProdutos.setAll(filtrados);
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Erro na Busca", "Erro ao filtrar produtos: " + e.getMessage());
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