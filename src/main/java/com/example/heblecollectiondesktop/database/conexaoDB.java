package com.example.heblecollectiondesktop.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class conexaoDB {

    private static final String URL = "jdbc:mysql://localhost:3306/login_schema";
    private static final String USUARIO = "root";
    private static final String SENHA = "heblecollection@_2026-2027";

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }


    public static void main(String[] args) {
        try (Connection conexao = getConexao()) {
            if (conexao != null) {
                System.out.println("Conexão com o banco realizada com sucesso!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}