package com.example.heblecollectiondesktop.model;

import java.time.LocalDateTime;

public class LogModeracao {
    private int id;
    private String funcionarioId;
    private String acao;
    private String alvoAfetado;
    private String detalhes;
    private LocalDateTime dataAcao;

    public LogModeracao(int id, String funcionarioId, String acao, String alvoAfetado, String detalhes, LocalDateTime dataAcao) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.acao = acao;
        this.alvoAfetado = alvoAfetado;
        this.detalhes = detalhes;
        this.dataAcao = dataAcao;
    }

    public String getFuncionarioId() { return funcionarioId; }
    public void setFuncionarioId(String funcionarioId) { this.funcionarioId = funcionarioId; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }

    public String getAlvoAfetado() { return alvoAfetado; }
    public void setAlvoAfetado(String alvoAfetado) { this.alvoAfetado = alvoAfetado; }

    public String getDetalhes() { return detalhes; }
    public void setDetalhes(String detalhes) { this.detalhes = detalhes; }

    public LocalDateTime getDataAcao() { return dataAcao; }
    public void setDataAcao(LocalDateTime dataAcao) { this.dataAcao = dataAcao; }
}