package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.model.Ticket;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.example.heblecollectiondesktop.database.TicketDAO;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControllerTicketFechados {
   @FXML
    private TableView<Ticket> tbTicketsFechados;

   @FXML
    private TableColumn<Ticket , Integer> colId;

   @FXML
    private TableColumn<Ticket , String> colAssunto;

    @FXML
    private TableColumn<Ticket , String> colDescricao;

    @FXML
    private TableColumn<Ticket , String> colStatus;


    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAssunto.setCellValueFactory(new PropertyValueFactory<>("assunto"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }


    }




