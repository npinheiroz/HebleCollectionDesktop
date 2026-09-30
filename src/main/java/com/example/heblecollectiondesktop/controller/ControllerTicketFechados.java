package com.example.heblecollectiondesktop.controller;

import java.sql.SQLException;
import java.util.List;

import com.example.heblecollectiondesktop.database.TicketDAO;
import com.example.heblecollectiondesktop.model.Ticket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControllerTicketFechados {

    @FXML
    private TableView<Ticket> tbTicketsFechados;

    @FXML
    private TableColumn<Ticket, Integer> colId;

    @FXML
    private TableColumn<Ticket, String> colAssunto;

    @FXML
    private TableColumn<Ticket, String> colDescricao;

    @FXML
    private TableColumn<Ticket, String> colStatus;

    // Instância do DAO para uso na classe
    private final TicketDAO ticketDAO = new TicketDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAssunto.setCellValueFactory(new PropertyValueFactory<>("assunto"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


        carregarTickets();
    }

    public void carregarTickets() {
        try {

            List<Ticket> listaBanco = ticketDAO.listarTodos();

            ObservableList<Ticket> listaObservable = FXCollections.observableArrayList(listaBanco);
            tbTicketsFechados.setItems(listaObservable);

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao carregar tickets", "Não foi possível carregar os dados do banco: " + e.getMessage());
        }
    }

    private void mostrarAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

