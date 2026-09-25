package com.example.heblecollectiondesktop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.heblecollectiondesktop.model.Cargo;
import com.example.heblecollectiondesktop.model.Funcionario;
import com.example.heblecollectiondesktop.model.Gerente;

public class FuncionarioDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/login_schema?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO_DB = "root";
    private static final String SENHA_DB = "heblecollection@_2026-2027";

    public Funcionario autenticar(String matricula, String senha) throws SQLException {
        String sql = "SELECT * FROM funcionarios WHERE matricula = ? AND senha = ? LIMIT 1";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO_DB, SENHA_DB);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, matricula);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("idfuncionarios");
                    String mat = rs.getString("matricula");
                    String pass = rs.getString("senha");
                    String cargoBanco = rs.getString("cargo");

                    Cargo cargoEnum = (cargoBanco != null)
                            ? Cargo.valueOf(cargoBanco.toUpperCase())
                            : Cargo.FUNCIONARIO;

                    if (cargoEnum == Cargo.GERENTE) {
                        return new Gerente(id, mat, pass);
                    } else {
                        return new Funcionario(id, mat, pass, cargoEnum);
                    }
                }
            }
        }
        return null;

    }
    public java.util.List<Funcionario> listarTodos() throws SQLException {
        java.util.List<Funcionario> lista = new java.util.ArrayList<>();
        String sql = "SELECT * FROM funcionarios ORDER BY idfuncionarios DESC";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO_DB, SENHA_DB);
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("idfuncionarios");
                String mat = rs.getString("matricula");
                String pass = rs.getString("senha");
                String cargoBanco = rs.getString("cargo");

                Cargo cargoEnum = (cargoBanco != null)
                        ? Cargo.valueOf(cargoBanco.toUpperCase())
                        : Cargo.FUNCIONARIO;

                if (cargoEnum == Cargo.GERENTE) {
                    lista.add(new Gerente(id, mat, pass));
                } else {
                    lista.add(new Funcionario(id, mat, pass, cargoEnum));
                }
            }
        }
        return lista;
    }

    public void salvar(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO funcionarios (matricula, senha, cargo) VALUES (?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(URL, USUARIO_DB, SENHA_DB);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, funcionario.getMatricula());
            stmt.setString(2, funcionario.getSenha());
            stmt.setString(3, funcionario.getCargo().name());

            stmt.executeUpdate();
        }

    }
}