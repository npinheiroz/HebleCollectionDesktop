package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.model.Funcionario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
<<<<<<< HEAD
import java.net.URL;

public class ControllerTicketsHub {

    @FXML
    private Pane containerCentral;

=======

public class ControllerTicketsHub {

    private Pane containerCentral;
>>>>>>> aa14548 (tickets)
    private Funcionario funcionarioLogado;

    public void setContainerCentral(Pane containerCentral) {
        this.containerCentral = containerCentral;
    }

    public void setFuncionarioLogado(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
    }

    @FXML
    private void abrirTicketsPendentes(ActionEvent event) {
<<<<<<< HEAD
        // Adicionada a barra '/' no início do caminho
        carregarView("/com/example/heblecollectiondesktop/view/GerenciarTicketsPendentes.fxml", loader -> {
            ControllerTicketsPendentes controller = loader.getController();
            if (controller != null) {
                controller.setContainerCentral(containerCentral);
                controller.setFuncionarioLogado(funcionarioLogado);
            }
        });
    }

    @FXML
    private void abrirTicketsEmAndamento(ActionEvent event) {
        carregarView("/com/example/heblecollectiondesktop/view/TicketsAndamento.fxml", loader -> {
            ControllerTicketsAndamento controller = loader.getController();
            if (controller != null) {
                controller.setContainerCentral(containerCentral);
                controller.setFuncionarioLogado(funcionarioLogado);
            }
        });
    }

    @FXML
    private void abrirTicketsFechados(ActionEvent event) {
        // Adicionada a barra '/' no início do caminho
        carregarView("/com/example/heblecollectiondesktop/view/ticketFechados.fxml", loader -> {
            ControllerTicketFechados controller = loader.getController();
            if (controller != null) {
                controller.setContainerCentral(containerCentral);
                controller.setFuncionarioLogado(funcionarioLogado);
            }
        });
    }

    private void carregarView(String caminhoFxml, ControllerInitializer initializer) {
        try {
            URL resource = getClass().getResource(caminhoFxml);
            if (resource == null) {
                System.err.println("ERRO: O arquivo FXML não foi encontrado no caminho: " + caminhoFxml);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent view = loader.load();

            if (initializer != null) {
                initializer.initialize(loader);
            }

            if (containerCentral != null) {
                containerCentral.getChildren().setAll(view);
            } else {
                System.err.println("AVISO: containerCentral está nulo.");
=======
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/heblecollectiondesktop/view/ticketsPendentes.fxml"));
            Parent view = loader.load();

            ControllerTicketsPendentes controller = loader.getController();
            controller.setContainerCentral(containerCentral);
            controller.setFuncionarioLogado(funcionarioLogado);

            if (containerCentral != null) {
                containerCentral.getChildren().setAll(view);
>>>>>>> aa14548 (tickets)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
    @FunctionalInterface
    private interface ControllerInitializer {
        void initialize(FXMLLoader loader);
=======
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
>>>>>>> aa14548 (tickets)
    }
}