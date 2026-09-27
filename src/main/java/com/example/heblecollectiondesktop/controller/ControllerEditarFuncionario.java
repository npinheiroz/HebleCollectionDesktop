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
import javafx.stage.Stage;

public class ControllerEditarFuncionario {

    @FXML private TextField txEditarMatricula;
    @FXML private TextField txEditarSenha;
    @FXML private ComboBox<Cargo> cmbEditarCargo;

    private final FuncionarioDAO editarfuncionarioDAO = new FuncionarioDAO();
    private Funcionario funcionarioeditando;

    public void setFuncionario(Funcionario funcionario) {
        this.funcionarioeditando = funcionario;

        if (funcionario != null) {
            txEditarMatricula.setText(funcionario.getMatricula());
            txEditarSenha.setText(funcionario.getSenha());
            cmbEditarCargo.setValue(funcionario.getCargo());
        }
    }

    @FXML
    public void initialize() {
        if (cmbEditarCargo != null) {
            cmbEditarCargo.setItems(FXCollections.observableArrayList(Cargo.values()));
        }
    }

    @FXML
    public void EditarFuncionario(ActionEvent event) {
        String matricula = txEditarMatricula.getText();
        String senha = txEditarSenha.getText();
        Cargo cargo = cmbEditarCargo.getValue();

        if (matricula == null || matricula.trim().isEmpty() ||
                senha == null || senha.trim().isEmpty() ||
                cargo == null) {
            mostrarAlerta("Campos Obrigatórios", "Por favor, preencha a matrícula, senha e selecione um cargo.", Alert.AlertType.WARNING);
            return;
        }

        if (funcionarioeditando == null) {
            mostrarAlerta("Erro", "Nenhum funcionário foi selecionado para edição.", Alert.AlertType.ERROR);
            return;
        }

        funcionarioeditando.setMatricula(matricula);
        funcionarioeditando.setSenha(senha);
        funcionarioeditando.setCargo(cargo);

        boolean sucesso = editarfuncionarioDAO.atualizar(funcionarioeditando);

        if (sucesso) {
            mostrarAlerta("Sucesso", "Funcionário editado com sucesso!", Alert.AlertType.INFORMATION);

            Stage janelaAtual = (Stage) txEditarMatricula.getScene().getWindow();
            janelaAtual.close();
        } else {
            mostrarAlerta("Erro de Persistência", "Não foi possível atualizar o funcionário no banco de dados.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void Cancelar() {
        Stage janelaAtual = (Stage) txEditarMatricula.getScene().getWindow();
        janelaAtual.close();
    }

    @FXML
    public void Limpar() {
        txEditarMatricula.clear();
        txEditarSenha.clear();
        cmbEditarCargo.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}