package com.example.heblecollectiondesktop.model;

public class Gerente extends Funcionario {

    public Gerente() {
        super();
        setCargo(Pessoa.GERENTE);
    }

    public Gerente(int id, String matricula, String senha) {
        super(id, matricula, senha);
        setCargo(Pessoa.GERENTE);
    }

    // Adicione aqui métodos e atributos exclusivos do Gerente
    // Exemplo: private double bonusAnual;
}