package com.example.heblecollectiondesktop.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.heblecollectiondesktop.database.FuncionarioDAO;
import com.example.heblecollectiondesktop.model.Cargo;
import com.example.heblecollectiondesktop.model.Funcionario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControllerGerenciarFuncionarios implements Initializable {

    @FXML private TableView<Funcionario> tabelaFuncionarios;
    @FXML private TableColumn<Funcionario, Integer> colId;
    @FXML private TableColumn<Funcionario, String> colMatricula;
    @FXML private TableColumn<Funcionario, Cargo> colCargo;

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private final ObservableList<Funcionario> listaFuncionarios = FXCollections.observableArrayList();

    private Pane containerCentral;
    private Funcionario funcionarioLogado;

    public void setContainerCentral(Pane containerCentral) {
        this.containerCentral = containerCentral;
    }

    public void setFuncionarioLogado(Funcionario funcionario) {
        this.funcionarioLogado = funcionario;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabela();
        carregarFuncionarios();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        tabelaFuncionarios.setItems(listaFuncionarios);
    }

    public void carregarFuncionarios() {
        try {
            listaFuncionarios.clear();
            listaFuncionarios.addAll(funcionarioDAO.listarTodos());
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Erro de Banco de Dados", "Falha ao carregar lista de funcionários: " + e.getMessage());
        }
    }

    @FXML
    private void abrirModalCadastro(ActionEvent event) {
        try {
            // Tenta localizar o FXML usando ambas as convenções de nome de arquivo
            URL fxmlLocation = getClass().getResource("/com/example/heblecollectiondesktop/view/cadastroFuncionarios.fxml");
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getResource("/com/example/heblecollectiondesktop/view/cadastro_funcionario.fxml");
            }
            if (fxmlLocation == null) {
                fxmlLocation = getClass().getResource("/view/cadastroFuncionarios.fxml");
            }

            if (fxmlLocation == null) {
                mostrarAlerta("Erro FXML", "Arquivo FXML de cadastro não encontrado nos recursos.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            ControllerCadastroFuncionarios controllerCadastro = loader.getController();
            if (controllerCadastro != null) {
                controllerCadastro.setControllerPai(this);
            }

            Stage modalStage = new Stage();
            modalStage.setTitle("Heble Collection - Novo Funcionário");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro de Carregamento", "Falha ao carregar a interface de cadastro. Verifique a linha 13 do FXML.");
        }
    }

    @FXML
    private void voltarAoHub(ActionEvent event) {
        try {
            URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/moderacaoHub.fxml");
            if (url == null) {
                url = getClass().getResource("/view/moderacaoHub.fxml");
            }

            if (url == null) {
                mostrarAlerta("Erro FXML", "Arquivo moderacaoHub.fxml não encontrado.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent hubView = loader.load();

            ControllerModeracaoHub controllerHub = loader.getController();
            if (controllerHub != null) {
                controllerHub.setContainerCentral(containerCentral);
                if (funcionarioLogado != null) {
                    controllerHub.setFuncionarioLogado(funcionarioLogado);
                }
            }

            if (containerCentral != null) {
                containerCentral.getChildren().setAll(hubView);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro de Navegação", "Não foi possível retornar ao Hub: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}