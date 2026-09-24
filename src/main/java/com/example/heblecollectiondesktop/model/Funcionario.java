package com.example.heblecollectiondesktop.model;

public class Funcionario {
    private int id;
    private String matricula;
    private String senha;
    private Pessoa cargo;

    public Funcionario() {
    }

    public Pessoa getCargo() {
        return cargo;
    }

    public void setCargo(Pessoa cargo) {
        this.cargo = cargo;
    }

    public Funcionario(int id, String matricula, String senha) {
        this.id = id;
        this.matricula = matricula;
        this.senha = senha;
        this.cargo=Pessoa.FUNCIONARIO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", matricula='" + matricula + '\'' +
                '}';
    }
}