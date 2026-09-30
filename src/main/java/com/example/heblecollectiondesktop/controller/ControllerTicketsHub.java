package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.model.Funcionario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ControllerTicketsHub {

    private Pane containerCentral;
    private Funcionario funcionarioLogado;

    public void setContainerCentral(Pane containerCentral) {
        this.containerCentral = containerCentral;
    }

    public void setFuncionarioLogado(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
    }

    @FXML
    private void abrirTicketsPendentes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/heblecollectiondesktop/view/ticketsPendentes.fxml"));
            Parent view = loader.load();

            ControllerTicketsPendentes controller = loader.getController();
            controller.setContainerCentral(containerCentral);
            controller.setFuncionarioLogado(funcionarioLogado);

            if (containerCentral != null) {
                containerCentral.getChildren().setAll(view);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirTicketsFechados(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/heblecollectiondesktop/view/ticketFechados.fxml"));
            Parent view = loader.load();

            ControllerTicketFechados controller = loader.getController();
            controller.setContainerCentral(containerCentral);
            controller.setFuncionarioLogado(funcionarioLogado);

            if (containerCentral != null) {
                containerCentral.getChildren().setAll(view);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}