package com.example.heblecollectiondesktop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public boolean autenticar(String matricula, String senha) {
        String sql = "SELECT * FROM usuarios WHERE matricula = ? AND senha = ?";

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, matricula);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Erro na consulta SQL: " + e.getMessage());
            return false;
        }
    }
}