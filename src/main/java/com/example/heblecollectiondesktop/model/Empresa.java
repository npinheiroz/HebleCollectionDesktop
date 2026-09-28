package com.example.heblecollectiondesktop.model;

public class Empresa {
    private Integer id;
    private String nome;
    private String cnpj;
    private String estilo;
    private boolean aprovado;


    public Empresa() {
    }

    public Empresa(Integer id, String nome, String cnpj, String estilo, boolean aprovado) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.estilo = estilo;
        this.aprovado = aprovado;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }
}