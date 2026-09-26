package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.database.FuncionarioDAO;
import com.example.heblecollectiondesktop.model.Cargo;
import com.example.heblecollectiondesktop.model.Funcionario;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerEditarFuncionario {
    @FXML private TextField txEditarMatricula;
    @FXML private TextField txEditarSenha;
    @FXML private ComboBox<Cargo> cmbEditarCargo;
    private final FuncionarioDAO editarfuncionarioDAO = new FuncionarioDAO();
    private Funcionario funcionarioeditando;

    public void setFuncionario(Funcionario funcionario) {
        this.funcionarioeditando = funcionario;

        // Preenche os campos da tela com os dados atuais do funcionário
        txEditarMatricula.setText(funcionario.getMatricula());
        txEditarSenha.setText(funcionario.getSenha());
        cmbEditarCargo.setValue(funcionario.getCargo());
    }
     @FXML
    public void initialize () {
        if (cmbEditarCargo != null) {
            cmbEditarCargo.setItems(FXCollections.observableArrayList(Cargo.values()));
        }
    }

   public void EditarFuncionario (ActionEvent event) throws SQLException {
       String Matricula = txEditarMatricula.getText();
       String Senha = txEditarSenha.getText();
       Cargo cargo = cmbEditarCargo.getValue();

       if (Matricula.isEmpty() || Senha.isEmpty() || cargo == null) {
           mostrarAlerta( "Campos Obrigatórios", "Por favor, preencha a matrícula, senha e selecione um cargo.");
           return;
       }
       if (funcionarioeditando == null){
           mostrarAlerta("Erro", "Nenhum funcionario foi editado");
           return;
       }

       funcionarioeditando.setMatricula(Matricula);
       funcionarioeditando.setSenha(Senha);
       funcionarioeditando.setCargo(cargo);
       editarfuncionarioDAO.salvar(funcionarioeditando);
       mostrarAlerta("Funcionário editado","Funcionário editado com sucesso" );
       javafx.stage.Stage janelaAtual = (javafx.stage.Stage) txEditarMatricula.getScene().getWindow();
       janelaAtual.close();


   }
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }


}
