package com.example.heblecollectiondesktop.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.example.heblecollectiondesktop.database.CupomDAO;
import com.example.heblecollectiondesktop.database.ProdutoDAO;
import com.example.heblecollectiondesktop.database.TicketDAO;
import com.example.heblecollectiondesktop.model.Cargo;
import com.example.heblecollectiondesktop.model.Cupom;
import com.example.heblecollectiondesktop.model.Funcionario;
import com.example.heblecollectiondesktop.model.Produto;
import com.example.heblecollectiondesktop.model.Ticket;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ControllerDashboard implements Initializable {

    @FXML private Label lblUsuarioLogado;
    @FXML private Label lblTotalProdutos;
    @FXML private Label lblCuponsAtivos;
    @FXML private Label lblTicketsAbertos;
    @FXML private Label lblStatusDetalhado;

    @FXML private Button btnVisaoGeral;
    @FXML private Button btnProdutos;
    @FXML private Button btnCupons;
    @FXML private Button btnTickets;
    @FXML private Button btnModeracao;
    @FXML private Button btnSair;

    @FXML private VBox containerAtividades;
    @FXML private Pane painelConteudoCentral;

    private Node vistaInicialDashboard;
    private Funcionario funcionarioLogado;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final CupomDAO cupomDAO = new CupomDAO();
    private final TicketDAO ticketDAO = new TicketDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (btnModeracao != null) {
            btnModeracao.setVisible(false);
            btnModeracao.setManaged(false);
        }
    }

    public void setFuncionarioLogado(Funcionario funcionario) {
        this.funcionarioLogado = funcionario;

        if (painelConteudoCentral != null && !painelConteudoCentral.getChildren().isEmpty()) {
            this.vistaInicialDashboard = painelConteudoCentral.getChildren().get(0);
        }

        atualizarInterface();
        carregarMetricasDoBanco();
    }

    private void atualizarInterface() {
        if (funcionarioLogado != null) {
            boolean isGerente = (funcionarioLogado.getCargo() == Cargo.GERENTE);
            String papel = isGerente ? "GERENTE" : "FUNCIONÁRIO";

            lblUsuarioLogado.setText("Operador: " + funcionarioLogado.getMatricula() + " [" + papel + "]");

            if (btnModeracao != null) {
                btnModeracao.setVisible(isGerente);
                btnModeracao.setManaged(isGerente);
            }
        }
    }

    private void carregarMetricasDoBanco() {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            lblTotalProdutos.setText(produtos.size() + " itens");

            List<Cupom> cupons = cupomDAO.listarTodos();
            long cuponsAtivos = cupons.stream().filter(Cupom::isAtivo).count();
            lblCuponsAtivos.setText(cuponsAtivos + " ativos");

            List<Ticket> tickets = ticketDAO.listarTodos();
            lblTicketsAbertos.setText(tickets.size() + " registrados");

            if (funcionarioLogado != null) {
                lblStatusDetalhado.setText(
                        "Sessão ativa para matrícula " + funcionarioLogado.getMatricula() + ".\n" +
                                "Conexão MySQL estável em login_schema.\n" +
                                "Módulos disponíveis: Produtos, Cupons e Ticketing."
                );
            }

        } catch (Exception e) {
            lblStatusDetalhado.setText("Erro ao carregar dados do banco: " + e.getMessage());
        }
    }

    @FXML
    private void handleVisaoGeral(ActionEvent event) {
        if (painelConteudoCentral != null && vistaInicialDashboard != null) {
            painelConteudoCentral.getChildren().setAll(vistaInicialDashboard);
        }
        carregarMetricasDoBanco();
    }

    @FXML
    private void handleProdutos(ActionEvent event) {
        mostrarAlerta("Módulo Estoque", "Aba de Estoque e Produtos selecionada.");
    }

    @FXML
    private void handleCupons(ActionEvent event) {
        mostrarAlerta("Módulo Cupons", "Aba de Cupons Promocionais selecionada.");
    }

    @FXML
    private void handleTickets(ActionEvent event) {
        mostrarAlerta("Módulo Tickets", "Aba de Suporte e Chamados selecionada.");
    }

    @FXML
    private void handleModeracao(ActionEvent event) {
        System.out.println(">>> 1. Botão Moderação Clicado!");

        if (funcionarioLogado == null) {
            System.out.println("❌ ERRO: funcionarioLogado está NULL.");
            mostrarAlerta("Erro de Sessão", "Nenhum usuário logado detectado.");
            return;
        }

        System.out.println(">>> 2. Cargo do Usuário: " + funcionarioLogado.getCargo());

        if (funcionarioLogado.getCargo() == Cargo.GERENTE) {
            try {
                URL url = getClass().getResource("/com/example/heblecollectiondesktop/view/moderacaoHub.fxml");
                if (url == null) {
                    url = getClass().getResource("/view/moderacaoHub.fxml");
                }

                if (url == null) {
                    System.out.println("❌ ERRO: FXML moderacaoHub.fxml NÃO ENCONTRADO nos recursos!");
                    mostrarAlerta("Erro", "Não foi possível encontrar moderacaoHub.fxml.");
                    return;
                }

                System.out.println(">>> 3. FXML encontrado em: " + url.toExternalForm());

                FXMLLoader loader = new FXMLLoader(url);
                Parent hubView = loader.load();
                System.out.println(">>> 4. FXML carregado com sucesso pelo FXMLLoader.");

                ControllerModeracaoHub controllerHub = loader.getController();
                if (controllerHub != null) {
                    controllerHub.setContainerCentral(painelConteudoCentral);
                    controllerHub.setFuncionarioLogado(funcionarioLogado);
                    System.out.println(">>> 5. Referência do containerCentral e Usuário repassados para ControllerModeracaoHub.");
                } else {
                    System.out.println("⚠️ AVISO: ControllerModeracaoHub veio NULL do loader.");
                }

                // INSERÇÃO DA TELA NO CONTAINER CENTRAL:
                if (painelConteudoCentral != null) {
                    painelConteudoCentral.getChildren().setAll(hubView);
                    System.out.println("✅ 6. Visão inserida no painelConteudoCentral com sucesso!");
                } else {
                    System.out.println("❌ ERRO CRÍTICO: painelConteudoCentral está NULL!");
                }

            } catch (IOException e) {
                System.out.println("❌ EXCEÇÃO ao carregar o FXML:");
                e.printStackTrace();
                mostrarAlerta("Erro de Carregamento", "Falha ao carregar a tela de moderação: " + e.getMessage());
            }
        } else {
            mostrarAlerta("Acesso Negado", "Apenas Gerentes têm acesso a esta área.");
        }
    }

    @FXML
    private void handleSair(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/heblecollectiondesktop/login-view.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Heble Collection - Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void fecharAplicacao(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Encerrar Sistema");
        alerta.setHeaderText(null);
        alerta.setContentText("Deseja realmente fechar o sistema?");

        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
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