package com.example.heblecollectiondesktop.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Optional;

import com.example.heblecollectiondesktop.database.CupomDAO;
import com.example.heblecollectiondesktop.model.Cupom;
import com.example.heblecollectiondesktop.model.Empresa;
import com.example.heblecollectiondesktop.model.Funcionario;

import com.example.heblecollectiondesktop.model.Gerente;
import javafx.beans.property.SimpleStringProperty;
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

public class ControllerGerenciarCupons {

    @FXML private TableView<Cupom> tabelaCupons;
    @FXML private TableColumn<Cupom, Integer> colId;
    @FXML private TableColumn<Cupom, String> colCodigo;
    @FXML private TableColumn<Cupom, String> colDesconto;
    @FXML private TableColumn<Cupom, String> colValorMinimo;
    @FXML private TableColumn<Cupom, String> colStatus;
    @FXML private TableColumn<Cupom, String> colValidade;

    @FXML private Button btnAdicionarCupom;
    @FXML private Button btnEditarCupom;
    @FXML private Button btnRemoverCupom;

    private Pane containerCentral;
    private Funcionario funcionarioLogado;
    private final CupomDAO cupomDAO = new CupomDAO();
    private final ObservableList<Cupom> listaCupons = FXCollections.observableArrayList();

    public void setContainerCentral(Pane containerCentral) {
        this.containerCentral = containerCentral;
    }

    public void setFuncionarioLogado(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
    }

    @FXML
    public void initialize() {
        configurarColunas();
        carregarTabela();
    }

    private void configurarColunas() {
        if (colId != null) colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colCodigo != null) colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));

        if (colDesconto != null) {
            colDesconto.setCellValueFactory(cell ->
                    new SimpleStringProperty(String.format("%.1f%%", cell.getValue().getDescontoPercentual()))
            );
        }

        if (colValorMinimo != null) {
            colValorMinimo.setCellValueFactory(cell ->
                    new SimpleStringProperty(String.format("R$ %.2f", cell.getValue().getValorMinimo()))
            );
        }

        if (colStatus != null) {
            colStatus.setCellValueFactory(cell ->
                    new SimpleStringProperty(cell.getValue().isAtivo() ? "Ativo" : "Inativo")
            );
        }

        if (colValidade != null) {
            colValidade.setCellValueFactory(cell -> {
                if (cell.getValue().getValidade() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    return new SimpleStringProperty(sdf.format(cell.getValue().getValidade()));
                }
                return new SimpleStringProperty("-");
            });
        }
    }

    public void carregarTabela() {
        try {
            listaCupons.setAll(cupomDAO.listarTodos());
            if (tabelaCupons != null) {
                tabelaCupons.setItems(listaCupons);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro", "Erro ao carregar lista de cupons: " + e.getMessage());
        }
    }

    @FXML
    void abrirTelaAdicionarCupom(ActionEvent event) {
        try {
            URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/FormularioCupom.fxml");
            if (url == null) {
                url = getClass().getResource("/com/example/heblecollectiondesktop/view/formularioCupom.fxml");
            }

            if (url == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro FXML", "Arquivo FormularioCupom.fxml não encontrado.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent view = loader.load();

            // Passa as referências necessárias para o controller do formulário (se houver)
            Object controller = loader.getController();
            if (controller != null) {
                try {
                    controller.getClass().getMethod("setContainerCentral", Pane.class).invoke(controller, containerCentral);
                    controller.getClass().getMethod("setFuncionarioLogado", Funcionario.class).invoke(controller, funcionarioLogado);
                } catch (Exception ignored) {}
            }

            if (containerCentral != null) {
                containerCentral.getChildren().setAll(view);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível carregar a tela de formulário: " + e.getMessage());
        }
    }

    @FXML
    void abrirSubjanelaEditarCupom(ActionEvent event) {
        if (tabelaCupons == null) return;

        Cupom selecionado = tabelaCupons.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleção necessária", "Selecione um cupom na tabela para editar.");
            return;
        }

        try {
            URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/EditarCupom.fxml");
            if (url == null) {
                url = getClass().getResource("/com/example/heblecollectiondesktop/view/EditarCupom.fxml");
            }

            if (url == null) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro FXML", "Arquivo EditarCupom.fxml não encontrado.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent subtela = loader.load();


            Object controller = loader.getController();
            if (controller != null) {
                try {
                    controller.getClass().getMethod("setCupom", Cupom.class).invoke(controller, selecionado);
                } catch (Exception ignored) {}
            }

            Stage janelaAtual = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Stage subjanela = new Stage();
            subjanela.initOwner(janelaAtual);
            subjanela.initModality(Modality.WINDOW_MODAL);
            subjanela.setScene(new Scene(subtela));
            subjanela.setTitle("Editar Cupom - " + selecionado.getCodigo());
            subjanela.setResizable(false);
            subjanela.showAndWait();


            carregarTabela();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Carregamento", "Falha ao abrir subjanela de edição: " + e.getMessage());
        }
    }

    @FXML
    void acaoRemoverCupom(ActionEvent event) {
        if (tabelaCupons == null) return;

        Cupom selecionado = tabelaCupons.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleção necessária", "Selecione um cupom na tabela para remover.");
            return;
        }

        try {
            URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/DeletarCupons.fxml");
            if (url == null) {
                url = getClass().getResource("/view/DeletarCupons.fxml");
            }

            if (url == null) {
                mostrarAlerta(Alert.AlertType.ERROR,"Erro FXML", "Arquivo EditarEmpresas.fxml não encontrado.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent subtela1 = loader.load();

            ControllerDeletarCupons controllerCupons = loader.getController();
            if (controllerCupons != null) {
                controllerCupons.setcupom(selecionado);
                controllerCupons.Setdados(selecionado,this,funcionarioLogado);
            }

            Button btnClicado1 = (Button) event.getSource();
            Stage janelaAtual = (Stage) btnClicado1.getScene().getWindow();

            Stage subjanela1 = new Stage();
            subjanela1.initOwner(janelaAtual);
            subjanela1.initModality(Modality.WINDOW_MODAL);
            subjanela1.setScene(new Scene(subtela1));
            subjanela1.setTitle("Deletar cupons");
            subjanela1.setResizable(false);
            subjanela1.showAndWait();

            carregarTabela();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR,"Erro de Carregamento", "Falha ao carregar a sub-tela: " + e.getMessage());
        }
        }


    @FXML
    void voltarAoHub(ActionEvent event) {
        if (containerCentral == null) return;

        try {
            URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/CuponsHub.fxml");
            if (url == null) {
                url = getClass().getResource("/com/example/heblecollectiondesktop/view/cuponsHub.fxml");
            }

            if (url != null) {
                FXMLLoader loader = new FXMLLoader(url);
                Parent hubView = loader.load();

                Object controller = loader.getController();
                if (controller != null) {
                    try {
                        controller.getClass().getMethod("setContainerCentral", Pane.class).invoke(controller, containerCentral);
                        controller.getClass().getMethod("setFuncionarioLogado", Funcionario.class).invoke(controller, funcionarioLogado);
                    } catch (Exception ignored) {}
                }

                containerCentral.getChildren().setAll(hubView);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Erro de Navegação", "Não foi possível voltar ao Hub de Cupons.");
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