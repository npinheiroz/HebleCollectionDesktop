package com.example.heblecollectiondesktop.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerDashboard implements Initializable {

    @FXML
    private BarChart<String, Number> graficoVendas;

    @FXML
    private VBox containerMelhoresProdutos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarGraficoVendas();
        carregarMelhoresProdutos();
    }

    private void configurarGraficoVendas() {
        XYChart.Series<String, Number> serieGastos = new XYChart.Series<>();
        serieGastos.setName("Gastos");
        serieGastos.getData().add(new XYChart.Data<>("segunda", 13.5));
        serieGastos.getData().add(new XYChart.Data<>("terça", 16.0));
        serieGastos.getData().add(new XYChart.Data<>("quarta", 5.5));
        serieGastos.getData().add(new XYChart.Data<>("quinta", 15.0));
        serieGastos.getData().add(new XYChart.Data<>("sexta", 12.0));
        serieGastos.getData().add(new XYChart.Data<>("sabado", 16.0));
        serieGastos.getData().add(new XYChart.Data<>("domingo", 20.0));

        XYChart.Series<String, Number> serieGanhos = new XYChart.Series<>();
        serieGanhos.setName("Ganhos");
        serieGanhos.getData().add(new XYChart.Data<>("segunda", 12.0));
        serieGanhos.getData().add(new XYChart.Data<>("terça", 11.5));
        serieGanhos.getData().add(new XYChart.Data<>("quarta", 22.0));
        serieGanhos.getData().add(new XYChart.Data<>("quinta", 6.0));
        serieGanhos.getData().add(new XYChart.Data<>("sexta", 11.0));
        serieGanhos.getData().add(new XYChart.Data<>("sabado", 13.5));
        serieGanhos.getData().add(new XYChart.Data<>("domingo", 11.0));

        graficoVendas.getData().addAll(serieGastos, serieGanhos);
    }

    private void carregarMelhoresProdutos() {
        adicionarLinhaProduto("01", "Calça tuffo", 0.45, "#0EA5E9", "45%");
        adicionarLinhaProduto("02", "Cueca de tromba", 0.29, "#10B981", "29%");
        adicionarLinhaProduto("03", "Camisa valdiney", 0.18, "#8B5CF6", "18%");
        adicionarLinhaProduto("04", "Casaco de crepper", 0.25, "#F97316", "25%");
    }

    private void adicionarLinhaProduto(String id, String nome, double progresso, String corHex, String textoBadge) {
        HBox linha = new HBox(10);
        linha.setAlignment(Pos.CENTER_LEFT);

        Label lblId = new Label(id);
        lblId.setPrefWidth(40);
        lblId.setStyle("-fx-text-fill: #64748B;");

        Label lblNome = new Label(nome);
        lblNome.setPrefWidth(160);
        lblNome.setStyle("-fx-text-fill: #1E293B; -fx-font-weight: bold;");

        ProgressBar barra = new ProgressBar(progresso);
        barra.setPrefWidth(180);
        barra.setPrefHeight(6);
        barra.setStyle("-fx-accent: " + corHex + ";");

        Region espacador = new Region();
        HBox.setHgrow(espacador, Priority.ALWAYS);

        Label badge = new Label(textoBadge);
        badge.setStyle(
                "-fx-border-color: " + corHex + "; " +
                        "-fx-text-fill: " + corHex + "; " +
                        "-fx-border-radius: 6; " +
                        "-fx-padding: 2 8 2 8; " +
                        "-fx-font-size: 11px;"
        );

        linha.getChildren().addAll(lblId, lblNome, barra, espacador, badge);
        containerMelhoresProdutos.getChildren().add(linha);
    }
}