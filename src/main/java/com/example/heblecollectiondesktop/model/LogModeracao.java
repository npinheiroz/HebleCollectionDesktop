package com.example.heblecollectiondesktop.model;

import java.time.LocalDateTime;

public class LogModeracao {
    private final int id;
    private final String funcionarioId;
    private final String acao;
    private final String detalhes;
    private final LocalDateTime dataAcao;

    public LogModeracao(int id, String funcionarioId, String acao, String detalhes, LocalDateTime dataAcao) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.acao = acao;
        this.detalhes = detalhes;
        this.dataAcao = dataAcao;
    }

    public int getId() { return id; }
    public String getFuncionarioId() { return funcionarioId; }
    public String getAcao() { return acao; }
    public String getDetalhes() { return detalhes; }
    public LocalDateTime getDataAcao() { return dataAcao; }
}