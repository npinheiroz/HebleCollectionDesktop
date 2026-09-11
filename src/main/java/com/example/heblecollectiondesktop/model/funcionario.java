package com.example.heblecollectiondesktop.model;

public class funcionario {
    private int id;
    private String matricula;
    private String senha;

    public funcionario() {
    }

    public funcionario(int id, String matricula, String senha) {
        this.id = id;
        this.matricula = matricula;
        this.senha = senha;
    }


    public funcionario (String matricula, String senha) {
        this.matricula = matricula;
        this.senha = senha;
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