package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.database.EmpresaDAO;
import com.example.heblecollectiondesktop.model.Empresa;
import com.example.heblecollectiondesktop.model.Funcionario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ControllerGerenciarEmpresas {

    @FXML
    private TableView<Empresa> TabelaEmpresas;

    @FXML
    private TableColumn<Empresa, Integer> colID;

    @FXML
    private TableColumn<Empresa, String> colNome;

    @FXML
    private TableColumn<Empresa, String> colCNPJ;

    @FXML
    private TableColumn<Empresa, String> colEstilo;

    @FXML
    private Button btnAprovarEmpresas;

    @FXML
    private Button btnEditarEmpresas;

    @FXML
    private Button btnDeletarEmpresa;

    private Pane containerCentral;
    private Funcionario funcionarioLogado;
    private final EmpresaDAO empresaDAO = new EmpresaDAO();

    public void setContainerCentral(Pane containerCentral) {
        this.containerCentral = containerCentral;
    }

    public void setFuncionarioLogado(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
    }

    @FXML
    public void initialize() {
        if (colID != null) colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colNome != null) colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        if (colCNPJ != null) colCNPJ.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
        if (colEstilo != null) colEstilo.setCellValueFactory(new PropertyValueFactory<>("estilo"));

        carregarEmpresas();
    }

    public void carregarEmpresas() {
        try {
            ObservableList<Empresa> lista = FXCollections.observableArrayList(empresaDAO.listarAprovadas());
            if (TabelaEmpresas != null) {
                TabelaEmpresas.setItems(lista);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro de Conexão", "Não foi possível carregar as empresas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void irParaAprovarEmpresa(ActionEvent event) {
        if (containerCentral == null) {
            mostrarAlerta("Erro de Navegação", "O containerCentral não foi configurado.", Alert.AlertType.ERROR);
            return;
        }

        try {
            URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/AprovarEmpresas.fxml");
            if (url == null) {
                url = getClass().getResource("/view/AprovarEmpresas.fxml");
            }

            if (url == null) {
                mostrarAlerta("Erro FXML", "Arquivo AprovarEmpresas.fxml não encontrado.", Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent aprovarView = loader.load();

            ControllerAprovarEmpresa controllerAprovar = loader.getController();
            if (controllerAprovar != null) {
                controllerAprovar.setContainerCentral(containerCentral);
                if (funcionarioLogado != null) {
                    controllerAprovar.setFuncionarioLogado(funcionarioLogado);
                }
            }

            containerCentral.getChildren().setAll(aprovarView);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao carregar FXML", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void deletarEmpresa(ActionEvent event) {
        Empresa selecionada = TabelaEmpresas.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            mostrarAlerta("Atenção", "Selecione uma empresa na tabela para deletar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/DeletarEmpresa.fxml");
            if (url == null) {
                url = getClass().getResource("/view/DeletarEmpresa.fxml");
            }

            if (url == null) {
                mostrarAlerta("Erro FXML", "Arquivo DeletarEmpresa.fxml não encontrado.", Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent modalRoot = loader.load();

            ControllerDeletarEmpresa controllerDeletar = loader.getController();
            if (controllerDeletar != null) {
                // Passa o objeto Funcionario diretamente no lugar da String nomeUsuario
                controllerDeletar.setDados(selecionada, this, funcionarioLogado);
            }

            Stage modalStage = new Stage();
            modalStage.setTitle("Confirmar Exclusão de Empresa");
            modalStage.setScene(new Scene(modalRoot));
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setResizable(false);
            modalStage.showAndWait();

            carregarEmpresas();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao Abrir Modal", "Não foi possível carregar a tela de confirmação: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void AbrirEditarEmpresa(ActionEvent event) {
        Empresa empresaSelecionada = TabelaEmpresas.getSelectionModel().getSelectedItem();

        if (empresaSelecionada == null) {
            mostrarAlerta("Aviso", "Por favor, selecione uma empresa na tabela para editar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/EditarEmpresas.fxml");
            if (url == null) {
                url = getClass().getResource("/view/EditarEmpresas.fxml");
            }

            if (url == null) {
                mostrarAlerta("Erro FXML", "Arquivo EditarEmpresas.fxml não encontrado.", Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent subtela = loader.load();

            ControllerEditarEmpresa controllerEditar = loader.getController();
            if (controllerEditar != null) {
                controllerEditar.setEmpresa(empresaSelecionada);
                if (funcionarioLogado != null) {
                    controllerEditar.setFuncionarioLogado(funcionarioLogado);
                }
            }

            Button btnClicado = (Button) event.getSource();
            Stage janelaAtual = (Stage) btnClicado.getScene().getWindow();

            Stage subjanela = new Stage();
            subjanela.initOwner(janelaAtual);
            subjanela.initModality(Modality.WINDOW_MODAL);
            subjanela.setScene(new Scene(subtela));
            subjanela.setTitle("Editar Empresa");
            subjanela.setResizable(false);
            subjanela.showAndWait();

            carregarEmpresas();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro de Carregamento", "Falha ao carregar a tela de edição: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void voltarAoHub(ActionEvent event) {
        if (containerCentral == null) return;

        try {
            URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/moderacaoHub.fxml");
            if (url == null) {
                url = getClass().getResource("/view/moderacaoHub.fxml");
            }

            if (url != null) {
                FXMLLoader loader = new FXMLLoader(url);
                Parent hubView = loader.load();

                ControllerModeracaoHub controllerHub = loader.getController();
                if (controllerHub != null) {
                    controllerHub.setContainerCentral(containerCentral);
                    controllerHub.setFuncionarioLogado(funcionarioLogado);
                }

                containerCentral.getChildren().setAll(hubView);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro de Navegação", "Não foi possível voltar ao Hub: " + e.getMessage(), Alert.AlertType.ERROR);
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