package com.example.heblecollectiondesktop.controller;

import com.example.heblecollectiondesktop.database.EmpresaDAO;
import com.example.heblecollectiondesktop.database.LogsDAO;
import com.example.heblecollectiondesktop.model.Empresa;
import com.example.heblecollectiondesktop.model.Funcionario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerEditarEmpresa {
    @FXML private TextField txNomeEmpresa;
    @FXML private TextField txCNPJ;
    @FXML private TextField txEstilo;

    private Empresa Empresaeditando;
    private Funcionario funcionarioLogado;
    private ControllerGerenciarEmpresas controllerPai;
    private final EmpresaDAO EditarEmpresaDAO = new EmpresaDAO();
    private final LogsDAO logsDAO = new LogsDAO();

    public void setEmpresa(Empresa empresa) {
        this.Empresaeditando = empresa;

        if (empresa != null) {
            txNomeEmpresa.setText(empresa.getNome());
            txCNPJ.setText(empresa.getCnpj());
            txEstilo.setText(empresa.getEstilo());
        }
    }

    public void setFuncionarioLogado(Funcionario funcionario) {
        this.funcionarioLogado = funcionario;
    }

    public void setControllerPai(ControllerGerenciarEmpresas controllerPai) {
        this.controllerPai = controllerPai;
    }

    @FXML
    public void EditarEmpresa(ActionEvent event) {
        String Nome = txNomeEmpresa.getText();
        String CNPJ = txCNPJ.getText();
        String Estilo = txEstilo.getText();

        if (Nome == null || Nome.trim().isEmpty() ||
                CNPJ == null || CNPJ.trim().isEmpty() ||
                Estilo == null || Estilo.trim().isEmpty()) {
            mostrarAlerta1("Campos Obrigatórios", "Por favor, preencha o nome, CNPJ e estilo da empresa.", "Alerta");
            return;
        }

        if (Empresaeditando == null) {
            mostrarAlerta1("Erro", "Nenhuma empresa foi selecionada para edição.", "Erro");
            return;
        }

        Empresaeditando.setNome(Nome);
        Empresaeditando.setCnpj(CNPJ);
        Empresaeditando.setEstilo(Estilo);

        boolean sucesso = EditarEmpresaDAO.atualizar(Empresaeditando);

        if (sucesso) {
            String matricula = (funcionarioLogado != null && funcionarioLogado.getMatricula() != null)
                    ? funcionarioLogado.getMatricula() : "SISTEMA";
            String alvo = Empresaeditando.getNome() + " (ID: " + Empresaeditando.getId() + ")";

            logsDAO.registrarLog(
                    matricula,
                    "EDICAO_EMPRESA",
                    alvo,
                    "Dados da empresa atualizados com sucesso."
            );

            mostrarAlerta1("Sucesso", "Empresa editada com sucesso!", "Informação");

            if (controllerPai != null) {
                controllerPai.carregarEmpresas();
            }

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

    private void mostrarAlerta1(String titulo, String mensagem, String TipoAlerta) {
        Alert.AlertType tipo = Alert.AlertType.INFORMATION;

        if ("Erro".equalsIgnoreCase(TipoAlerta)) {
            tipo = Alert.AlertType.ERROR;
        } else if ("Alerta".equalsIgnoreCase(TipoAlerta)) {
            tipo = Alert.AlertType.WARNING;
        } else if ("Confirmação".equalsIgnoreCase(TipoAlerta)) {
            tipo = Alert.AlertType.CONFIRMATION;
        }

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}