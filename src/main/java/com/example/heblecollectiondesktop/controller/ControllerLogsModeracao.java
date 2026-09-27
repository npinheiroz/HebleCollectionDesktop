package com.example.heblecollectiondesktop.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.example.heblecollectiondesktop.database.LogsDAO;
import com.example.heblecollectiondesktop.model.Funcionario;
import com.example.heblecollectiondesktop.model.LogModeracao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class ControllerLogsModeracao implements Initializable {

    @FXML private TableView<LogModeracao> tabelaLogs;
    @FXML private TableColumn<LogModeracao, Integer> colId;
    @FXML private TableColumn<LogModeracao, LocalDateTime> colDataHora;
    @FXML private TableColumn<LogModeracao, String> colModerador;
    @FXML private TableColumn<LogModeracao, String> colAcao;
    @FXML private TableColumn<LogModeracao, String> colDetalhes;
    @FXML private TextField txtFiltro;

    private final LogsDAO logDAO = new LogsDAO();
    private final ObservableList<LogModeracao> listaLogs = FXCollections.observableArrayList();
    private FilteredList<LogModeracao> listaFiltrada;

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
        configurarFiltro();
        carregarLogs();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colModerador.setCellValueFactory(new PropertyValueFactory<>("funcionarioId"));
        colAcao.setCellValueFactory(new PropertyValueFactory<>("acao"));
        colDetalhes.setCellValueFactory(new PropertyValueFactory<>("detalhes"));
        colDataHora.setCellValueFactory(new PropertyValueFactory<>("dataAcao"));
        colDataHora.setCellFactory(column -> new TableCell<LogModeracao, LocalDateTime>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        listaFiltrada = new FilteredList<>(listaLogs, p -> true);
        tabelaLogs.setItems(listaFiltrada);
    }

    private void configurarFiltro() {
        txtFiltro.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(log -> {
                if (newValue == null || newValue.isBlank()) {
                    return true;
                }
                String termo = newValue.toLowerCase();

                boolean bateuModerador = log.getFuncionarioId() != null && log.getFuncionarioId().toLowerCase().contains(termo);
                boolean bateuAcao = log.getAcao() != null && log.getAcao().toLowerCase().contains(termo);
                boolean bateuDetalhes = log.getDetalhes() != null && log.getDetalhes().toLowerCase().contains(termo);

                return bateuModerador || bateuAcao || bateuDetalhes;
            });
        });
    }

    @FXML
    public void carregarLogs() {
        try {
            listaLogs.clear();
            listaLogs.addAll(logDAO.listarTodos());
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao carregar logs: " + e.getMessage());
        }
    }

    @FXML
    private void atualizarLogs(ActionEvent event) {
        carregarLogs();
    }

    @FXML
    private void voltarAoHub(ActionEvent event) {
        try {
            URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/moderacaoHub.fxml");
            if (url == null) {
                url = getClass().getResource("/view/moderacaoHub.fxml");
            }

            if (url == null) {
                mostrarAlerta("Arquivo moderacaoHub.fxml não encontrado.");
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
            mostrarAlerta("Não foi possível retornar ao Hub: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logs de Moderação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}