package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.database.LogsDAO;
import com.example.heblecollectiondesktop.database.TicketDAO;
import com.example.heblecollectiondesktop.model.Funcionario;
import com.example.heblecollectiondesktop.model.Ticket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class ControllerTickestAndamento {

    @FXML private TableView<Ticket> ticketsEmAndamento;
    @FXML private TableColumn<Ticket, Integer> colID;
    @FXML private TableColumn<Ticket, String> colAssunto;
    @FXML private TableColumn<Ticket, String> colDescricao;
    @FXML private TableColumn<Ticket, Boolean> colStatus;
    @FXML private TableColumn<Ticket, Integer> colFuncionarioId;

    private final TicketDAO ticketDAO = new TicketDAO();
    private final LogsDAO logsDAO = new LogsDAO();
    private final ObservableList<Ticket> listaTickets = FXCollections.observableArrayList();

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
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAssunto.setCellValueFactory(new PropertyValueFactory<>("assunto"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colFuncionarioId.setCellValueFactory(new PropertyValueFactory<>("funcionarioId"));

        carregarTickets();
    }

    public void carregarTickets() {
        try {
            listaTickets.setAll(ticketDAO.listarTodos());
            ticketsEmAndamento.setItems(listaTickets);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível carregar os tickets: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void aprovarTicket(ActionEvent event) {
        Ticket ticketSelecionado = ticketsEmAndamento.getSelectionModel().getSelectedItem();

        if (ticketSelecionado == null) {
            mostrarAlerta("Atenção", "Selecione um ticket na tabela para aprovar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            boolean sucesso = ticketDAO.atualizarStatus(ticketSelecionado.getId(), true);

            if (sucesso) {
                registrarLog("APROVACAO_TICKET", ticketSelecionado, "Ticket aprovado no painel.");
                mostrarAlerta("Sucesso", "Ticket #" + ticketSelecionado.getId() + " aprovado com sucesso!", Alert.AlertType.INFORMATION);
                carregarTickets();
            } else {
                mostrarAlerta("Erro", "Falha ao atualizar o status do ticket.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao aprovar ticket: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void negarTicket(ActionEvent event) {
        Ticket ticketSelecionado = ticketsEmAndamento.getSelectionModel().getSelectedItem();

        if (ticketSelecionado == null) {
            mostrarAlerta("Atenção", "Selecione um ticket na tabela para negar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            boolean sucesso = ticketDAO.atualizarStatus(ticketSelecionado.getId(), false);

            if (sucesso) {
                registrarLog("REJEICAO_TICKET", ticketSelecionado, "Ticket negado pela moderação.");
                mostrarAlerta("Sucesso", "Ticket #" + ticketSelecionado.getId() + " negado com sucesso.", Alert.AlertType.INFORMATION);
                carregarTickets();
            } else {
                mostrarAlerta("Erro", "Falha ao alterar o status do ticket.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao negar ticket: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void deletarTicket(ActionEvent event) {
        Ticket ticketSelecionado = ticketsEmAndamento.getSelectionModel().getSelectedItem();

        if (ticketSelecionado == null) {
            mostrarAlerta("Atenção", "Selecione um ticket na tabela para deletar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            boolean sucesso = ticketDAO.deletar(ticketSelecionado.getId());

            if (sucesso) {
                registrarLog("EXCLUSAO_TICKET", ticketSelecionado, "Ticket removido do sistema.");
                mostrarAlerta("Sucesso", "Ticket #" + ticketSelecionado.getId() + " excluído com sucesso!", Alert.AlertType.INFORMATION);
                carregarTickets();
            } else {
                mostrarAlerta("Erro", "Não foi possível deletar o ticket.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Erro ao deletar ticket: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void registrarLog(String acao, Ticket ticket, String observacao) {
        String alvo = "Ticket #" + ticket.getId() + " - " + ticket.getAssunto();

        try {
            if (funcionarioLogado != null) {
                logsDAO.registrarLog(funcionarioLogado, acao, alvo, observacao);
            } else {
                logsDAO.registrarLog("SISTEMA", acao, alvo, observacao);
            }
        } catch (Exception e) {
            System.err.println("Erro ao registrar log: " + e.getMessage());
        }
    }

    @FXML
    private void voltarAoHub(ActionEvent event) {
        try {
            URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/ticketsHub.fxml");
            if (url == null) {
                url = getClass().getResource("/view/ticketsHub.fxml");
            }

            if (url == null) {
                mostrarAlerta("Erro FXML", "Arquivo ticketsHub.fxml não encontrado.", Alert.AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent hubView = loader.load();

            ControllerTicketsHub controllerHub = loader.getController();
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
            mostrarAlerta("Erro de Navegação", "Falha ao retornar ao hub: " + e.getMessage(), Alert.AlertType.ERROR);
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