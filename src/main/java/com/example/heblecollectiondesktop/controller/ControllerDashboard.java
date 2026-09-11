package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.model.funcionario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerDashboard implements Initializable {

    @FXML
    private Label lblUsuarioLogado;

    private funcionario funcionarioLogado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicializações visuais da Dashboard (gráficos, tabelas, etc.) entram aqui
    }

    public void setFuncionarioLogado(funcionario funcionario) {
        this.funcionarioLogado = funcionario;
        if (lblUsuarioLogado != null && funcionario != null) {
            lblUsuarioLogado.setText("Operador: " + funcionario.getMatricula());
        }
    }

    public funcionario getFuncionarioLogado() {
        return funcionarioLogado;
    }
}