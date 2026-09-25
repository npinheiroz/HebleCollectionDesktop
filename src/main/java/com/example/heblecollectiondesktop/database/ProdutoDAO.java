package com.example.heblecollectiondesktop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.heblecollectiondesktop.model.Produto; // Certifique-se de ter este Model criado

public class ProdutoDAO {

    private final String url = "jdbc:mysql://localhost:3306/login_schema?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String usuarioDb = "root";
    private final String senhaDb = "heblecollection@_2026-2027";

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY id DESC";

        try (Connection conexao = DriverManager.getConnection(url, usuarioDb, senhaDb);
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto p = new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade_estoque"),
                        rs.getString("status")
                );
                lista.add(p);
            }
        }
        return lista;
    }

    public void atualizarStatus(int idProduto, String novoStatus) throws SQLException {
        String sql = "UPDATE produtos SET status = ? WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuarioDb, senhaDb);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, novoStatus);
            stmt.setInt(2, idProduto);
            stmt.executeUpdate();
        }
    }
}