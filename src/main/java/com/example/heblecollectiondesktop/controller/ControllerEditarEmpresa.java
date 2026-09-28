package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.database.EmpresaDAO;
import com.example.heblecollectiondesktop.model.Cargo;
import com.example.heblecollectiondesktop.model.Empresa;
import com.example.heblecollectiondesktop.model.Funcionario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerEditarEmpresa {
    @FXML
    private TextField txNomeEmpresa;
    @FXML
    private TextField txCNPJ;
    @FXML
    private TextField txEstilo;
    private Empresa Empresaeditando;

    private final EmpresaDAO EditarEmpresaDAO = new EmpresaDAO();
    public void setEmpresa(Empresa empresa) {
        this.Empresaeditando = empresa;

        if (empresa != null) {
            txNomeEmpresa.setText(empresa.getNome());
            txCNPJ.setText(empresa.getCnpj());
            txEstilo.setText(empresa.getEstilo());
        }
    }

    @FXML
    public void EditarEmpresa(ActionEvent event) {
        String Nome = txNomeEmpresa.getText();
        String CNPJ= txCNPJ.getText();
        String Estilo = txEstilo.getText();

        if (Nome == null || Nome.trim().isEmpty() ||
                CNPJ == null || CNPJ.trim().isEmpty() ||
                Estilo == null || Estilo.trim().isEmpty())  {
            mostrarAlerta1("Campos Obrigatórios", "Por favor, preencha o nome, CNPJ e estilo da empresa.", "Aviso");
            return;
        }

        if (Empresaeditando == null) {
            mostrarAlerta1("Erro", "Nenhuma empresa foi selecionado para edição.", "Erro");
            return;
        }

        Empresaeditando.setNome(Nome);
        Empresaeditando.setCnpj(CNPJ);
        Empresaeditando.setEstilo(Estilo);

        boolean sucesso = EditarEmpresaDAO.atualizar(Empresaeditando);

        if (sucesso) {
            mostrarAlerta1("Sucesso", "Empresa editado com sucesso!" , "Confirmação");

            Stage janelaAtual = (Stage) txNomeEmpresa.getScene().getWindow();
            janelaAtual.close();
        } else {
            mostrarAlerta1("Erro de Persistência", "Não foi possível atualizar a empresa no banco de dados.", "Erro");
        }
    }
    @FXML
    public void Cancelar() {
        Stage janelaAtual = (Stage) txNomeEmpresa.getScene().getWindow();
        janelaAtual.close();
    }

    @FXML
    public void Limpar() {
        txNomeEmpresa.clear();
        txCNPJ.clear();
        txEstilo.clear();
    }
    private void mostrarAlerta1(String titulo, String mensagem , String TipoAlerta) {

        if (TipoAlerta == "Informação"){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensagem);
        }
        if (TipoAlerta == "Erro"){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensagem);
        }if (TipoAlerta == "Alerta"){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensagem);
        }if (TipoAlerta == "Confirmação"){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensagem);
        }

    }
}
