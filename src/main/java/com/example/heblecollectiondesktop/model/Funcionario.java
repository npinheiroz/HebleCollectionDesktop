package com.example.heblecollectiondesktop.model;

public class Funcionario {
    private int id;
    private String matricula;
    private String senha;
    private Cargo cargo;

    public Funcionario(int id, String matricula, String senha, Cargo cargo) {
        this.id = id;
        this.matricula = matricula;
        this.senha = senha;
        this.cargo = cargo;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }
}