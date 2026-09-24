package com.example.heblecollectiondesktop.model;

public class Empresa {
    String nome;
    String cnpj;
    String estilo;

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

    public String getCategoria() {
        return estilo;
    }

    public void setEstilo(String estilo){
        this.estilo = estilo;
    }
}
