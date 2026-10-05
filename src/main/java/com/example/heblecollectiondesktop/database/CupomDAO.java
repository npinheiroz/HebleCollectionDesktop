package com.example.heblecollectiondesktop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.heblecollectiondesktop.model.Cupom;

public class CupomDAO {

    public List<Cupom> listarTodos() throws SQLException {
        List<Cupom> lista = new ArrayList<>();
        String sql = "SELECT id, codigo, desconto_percentual, valor_minimo, ativo, validade FROM cupons ORDER BY id DESC";

        try (Connection conexao = conexaoDB.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearResultSetParaCupom(rs));
            }
        }
        return lista;
    }

    public Cupom buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT id, codigo, desconto_percentual, valor_minimo, ativo, validade FROM cupons WHERE codigo = ?";

        try (Connection conexao = conexaoDB.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaCupom(rs);
                }
            }
        }
        return null;
    }

    public Cupom buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, codigo, desconto_percentual, valor_minimo, ativo, validade FROM cupons WHERE id = ?";

        try (Connection conexao = conexaoDB.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaCupom(rs);
                }
            }
        }
        return null;
    }

    public boolean salvar(Cupom cupom) throws SQLException {
        String sql = "INSERT INTO cupons (codigo, desconto_percentual, valor_minimo, ativo, validade) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = conexaoDB.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, cupom.getCodigo());
            stmt.setDouble(2, cupom.getDescontoPercentual());
            stmt.setDouble(3, cupom.getValorMinimo());
            stmt.setBoolean(4, cupom.isAtivo());
            stmt.setDate(5, cupom.getValidade() != null ? new java.sql.Date(cupom.getValidade().getTime()) : null);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean atualizar(Cupom cupom) throws SQLException {
        String sql = "UPDATE cupons SET codigo = ?, desconto_percentual = ?, valor_minimo = ?, ativo = ?, validade = ? WHERE id = ?";

        try (Connection conexao = conexaoDB.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, cupom.getCodigo());
            stmt.setDouble(2, cupom.getDescontoPercentual());
            stmt.setDouble(3, cupom.getValorMinimo());
            stmt.setBoolean(4, cupom.isAtivo());
            stmt.setDate(5, cupom.getValidade() != null ? new java.sql.Date(cupom.getValidade().getTime()) : null);
            stmt.setInt(6, cupom.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletar(int id) throws SQLException {
        String sql = "DELETE FROM cupons WHERE id = ?";

        try (Connection conexao = conexaoDB.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletar(Cupom cupom) throws SQLException {
        if (cupom == null) return false;
        return deletar(cupom.getId());
    }

    public boolean excluir(int id) throws SQLException {
        return deletar(id);
    }

    private Cupom mapearResultSetParaCupom(ResultSet rs) throws SQLException {
        return new Cupom(
                rs.getInt("id"),
                rs.getString("codigo"),
                rs.getDouble("desconto_percentual"),
                rs.getDouble("valor_minimo"),
                rs.getBoolean("ativo"),
                rs.getDate("validade")
        );
    }
}