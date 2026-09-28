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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

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
        // 1. Vincula as colunas aos atributos da classe Empresa (getters)
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

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente remover a empresa '" + selecionada.getNome() + "'?");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean sucesso = empresaDAO.deletar(selecionada.getId());
            if (sucesso) {
                mostrarAlerta("Sucesso", "Empresa removida com sucesso.", Alert.AlertType.INFORMATION);
                carregarEmpresas(); // Recarrega a lista atualizada
            } else {
                mostrarAlerta("Erro", "Não foi possível remover a empresa.", Alert.AlertType.ERROR);
            }
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
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void abrirModalDeletar(Empresa empresaselecionada) {
        if (empresaselecionada == null) {
            mostrarAlerta("Nenhum Funcionário Selecionado", "Por favor, selecione um funcionário na tabela para realizar a exclusão.", Alert.AlertType.WARNING);
            return;
        }

        try {
            URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/deletarEmpresa.fxml");


            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            ControllerDeletarEmpresa controller = loader.getController();

            String matriculaLogado = (funcionarioLogado != null) ? funcionarioLogado.getMatricula() : "SISTEMA";
            controller.setDados(empresaselecionada, this, matriculaLogado);

            Stage stage = new Stage();
            stage.setTitle("Confirmar Exclusão e Registrar Log");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.showAndWait();

            carregarEmpresas();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao Abrir Tela", "Não foi possível carregar a interface de exclusão: " + e.getMessage(), Alert.AlertType.WARNING);
        }
    }
}