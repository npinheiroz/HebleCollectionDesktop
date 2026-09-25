package com.example.heblecollectiondesktop.model;

public class Gerente extends Funcionario {
    public Gerente(int id, String matricula, String senha) {
        super(id, matricula, senha, Cargo.GERENTE);
    }

    public Gerente(int id, String matricula, String senha, Cargo cargo) {
        super(id, matricula, senha, cargo);
    }
}