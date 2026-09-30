package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.database.TicketDAO;
import com.example.heblecollectiondesktop.model.Empresa;
import com.example.heblecollectiondesktop.model.Funcionario;
import com.example.heblecollectiondesktop.model.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerTicketsPendentes {
    @FXML
    private TableView <Ticket> TabelaTickets;
    @FXML
    private TableColumn <Ticket , Integer> colID;
    @FXML
    private TableColumn <Ticket , String> colAssunto;
    @FXML
    private TableColumn <Ticket , String> colDescricao;
    @FXML
    private TableColumn<Ticket , String> colStatus;
    private final TicketDAO TicketsPendentesDao = new TicketDAO();
    private final ObservableList<Ticket> listaTickets = FXCollections.observableArrayList();

    @FXML
    public void initialize( ) {
        if (colID != null) colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colAssunto != null) colAssunto.setCellValueFactory(new PropertyValueFactory<>("Assunto"));
        if (colDescricao != null) colDescricao.setCellValueFactory(new PropertyValueFactory<>("Descrição"));
        if (colStatus != null) colStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        TabelaTickets.setItems(listaTickets);
        carregarTickets();
    }

    public void carregarTickets() {
        try {
            ObservableList<Ticket> lista = FXCollections.observableArrayList(TicketsPendentesDao.listarTodos());
            if (TabelaTickets != null) {
                TabelaTickets.setItems(lista);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro de Conexão", "Não foi possível carregar as empresas: " + e.getMessage());
        }
    }

    public void AprovarTicket(){
        Ticket selecionado = TabelaTickets.getSelectionModel().getSelectedItem();
        if (selecionado == null){
            mostrarAlerta("Erro: Selecione um ticket", "Não possui ticket escolhido");
            return;
        }
        boolean sucesso = TicketsPendentesDao.atualizarStatus(selecionado.getId() , true);
        if (sucesso){
            mostrarAlerta("Ticket Aprovado","Ticket: "+selecionado.getId()+" Ticket Aprovado com sucesso");
        }
        else
        {
            mostrarAlerta("Erro", "Não foi possível aprovar a empresa selecionada.");
        }
    }


    public void RejeitarTicket(){
        Ticket Selecionado = TabelaTickets.getSelectionModel().getSelectedItem();
        if (Selecionado == null){
            mostrarAlerta("Erro: Selecione um ticket" , "Não possui ticket escolhido");
            return;
        }
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar rejeição");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja Rejeitar e excluir o cadastro "+ Selecionado.getId() + "?");
         Optional<ButtonType > resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean sucesso = TicketsPendentesDao.deletar(Selecionado.getId());

            if (sucesso) {
                mostrarAlerta("Sucesso", "Solicitação de ticket rejeitado com sucesso.");
                carregarTickets();
            } else {
                mostrarAlerta("Erro", "Não foi possível excluir o cadastro da empresa.");
            }
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
