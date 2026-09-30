package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.database.LogsDAO;
import com.example.heblecollectiondesktop.model.Funcionario;
import com.example.heblecollectiondesktop.model.LogModeracao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ControllerLogsModeracao {

    @FXML private TableView<LogModeracao> tabelaLogs;
    @FXML private TableColumn<LogModeracao, Integer> colId;
    @FXML private TableColumn<LogModeracao, LocalDateTime> colDataHora;
    @FXML private TableColumn<LogModeracao, String> colModerador;
    @FXML private TableColumn<LogModeracao, String> colAcao;
    @FXML private TableColumn<LogModeracao, String> colAlvoAfetado;
    @FXML private TableColumn<LogModeracao, String> colDetalhes;
    @FXML private TextField txtFiltro;

    private final LogsDAO logsDAO = new LogsDAO();
    private ObservableList<LogModeracao> listaLogs = FXCollections.observableArrayList();
    private FilteredList<LogModeracao> logsFiltrados;

    private Pane containerCentral;
    private Funcionario funcionarioLogado;

    public void setContainerCentral(Pane containerCentral) {
        this.containerCentral = containerCentral;
    }

    public void setFuncionarioLogado(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDataHora.setCellValueFactory(new PropertyValueFactory<>("dataAcao"));
        colModerador.setCellValueFactory(new PropertyValueFactory<>("funcionarioId"));
        colAcao.setCellValueFactory(new PropertyValueFactory<>("acao"));
        colAlvoAfetado.setCellValueFactory(new PropertyValueFactory<>("alvoAfetado"));
        colDetalhes.setCellValueFactory(new PropertyValueFactory<>("detalhes"));

        logsFiltrados = new FilteredList<>(listaLogs, p -> true);

        if (txtFiltro != null) {
            txtFiltro.textProperty().addListener((observable, oldValue, newValue) -> {
                logsFiltrados.setPredicate(log -> {
                    if (newValue == null || newValue.isBlank()) {
                        return true;
                    }

                    String termo = newValue.toLowerCase();

                    boolean bateModerador = log.getFuncionarioId() != null && log.getFuncionarioId().toLowerCase().contains(termo);
                    boolean bateAcao = log.getAcao() != null && log.getAcao().toLowerCase().contains(termo);
                    boolean bateAlvo = log.getAlvoAfetado() != null && log.getAlvoAfetado().toLowerCase().contains(termo);
                    boolean bateDetalhes = log.getDetalhes() != null && log.getDetalhes().toLowerCase().contains(termo);

                    return bateModerador || bateAcao || bateAlvo || bateDetalhes;
                });
            });
        }

        tabelaLogs.setItems(logsFiltrados);
        carregarLogs();
    }

    @FXML
    public void atualizarLogs() {
        carregarLogs();
    }

    private void carregarLogs() {
        try {
            listaLogs.setAll(logsDAO.listarTodos());
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro ao carregar logs", "Não foi possível carregar os registros de auditoria: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void voltarAoHub(ActionEvent event) {
        if (containerCentral == null) {
            exibirAlerta("Erro de Navegação", "Container central não configurado.", Alert.AlertType.ERROR);
            return;
        }

        try {
            URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/HubGeral.fxml");
            if (url == null) {
                url = getClass().getResource("/view/HubGeral.fxml");
            }

            if (url == null) {
                exibirAlerta("Erro FXML", "Arquivo HubGeral.fxml não encontrado.", Alert.AlertType.ERROR);
                return;
            }

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
        } catch (IOException e) {
            e.printStackTrace();
            exibirAlerta("Erro de Navegação", "Falha ao retornar ao painel principal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}