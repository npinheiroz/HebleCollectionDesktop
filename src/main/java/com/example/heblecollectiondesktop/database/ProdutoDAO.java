package com.example.heblecollectiondesktop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.heblecollectiondesktop.model.Produto;

public class ProdutoDAO {

    public List<Produto> listarTodos() {
        String sql = "SELECT * FROM login_schema.produtos ORDER BY id DESC";
        List<Produto> lista = new ArrayList<>();

        try (Connection conexao = conexaoDB.getConexao();
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

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todos os produtos: " + e.getMessage(), e);
        }

        return lista;
    }

    public List<Produto> buscarPorTermo(String termo) {
        String sql = "SELECT * FROM login_schema.produtos WHERE nome LIKE ? OR descricao LIKE ? ORDER BY id DESC";
        List<Produto> lista = new ArrayList<>();

        try (Connection conexao = conexaoDB.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            String searchPattern = "%" + termo + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
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

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar produtos por termo: " + e.getMessage(), e);
        }

        return lista;
    }

    public boolean atualizarStatus(int idProduto, String novoStatus) {
        String sql = "UPDATE login_schema.produtos SET status = ? WHERE id = ?";

        try (Connection conexao = conexaoDB.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, novoStatus);
            stmt.setInt(2, idProduto);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status do produto: " + e.getMessage(), e);
        }
    }

    public boolean deletar(int idProduto) {
        String sql = "DELETE FROM login_schema.produtos WHERE id = ?";

        try (Connection conexao = conexaoDB.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar produto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}